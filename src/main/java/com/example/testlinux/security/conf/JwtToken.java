package com.example.testlinux.security.conf;

import com.example.testlinux.exceptions.AuthManipulationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import com.google.common.hash.Hashing;

@Slf4j
@Data
public class JwtToken {

    public static final String PAYLOAD_USER_ID = "id";
    public static final String PAYLOAD_SUBJECT = "sub";
    public static final String PAYLOAD_ROLE = "role";
    public static final String PAYLOAD_ORIGINAL_USER_ID = "original_id";
    public static final String PAYLOAD_ORIGINAL_ROLE = "original_role";
    public static final String PAYLOAD_ORIGINAL_EXPIRATION = "original_exp";

    private static final ZoneId ZONE_ID_MOSCOW = ZoneId.of("Europe/Moscow");

    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    private static final Base64.Encoder BASE64URL_ENCODER = Base64.getUrlEncoder().withoutPadding();

    private static final Base64.Decoder BASE64URL_DECODER = Base64.getUrlDecoder();

    private String header;
    private String payload;
    private String signature;

    private String headerEncoded;
    private String payloadEncoded;
    private String signatureEncoded;

    private Map<String, Object> payloadMap = new HashMap<>();

    private boolean isValid;

    private Integer expiration;

    private LocalDateTime expirationDate;

    private Integer originalUserId;

    private String originalRole;

    private Integer originalExpiration;

    private LocalDateTime originalExpirationDate;

    private boolean expired;

    private static final String BEARER = "Bearer";


    public static String createToken(String userId, String role, int expiresInSeconds, String secret) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // Header
            Map<String, Object> headerMap = new HashMap<>();
            headerMap.put("alg", "HS256");
            headerMap.put("typ", "JWT");
            String headerJson = mapper.writeValueAsString(headerMap);
            String headerEncoded = encodeBase64Url(headerJson.getBytes(UTF_8));

            // Payload
            long now = Instant.now().getEpochSecond();
            long exp = now + expiresInSeconds;
            Map<String, Object> payloadMap = new HashMap<>();
            payloadMap.put(PAYLOAD_USER_ID, Integer.parseInt(userId));
            payloadMap.put(PAYLOAD_SUBJECT, userId);
            payloadMap.put(PAYLOAD_ROLE, role);
            payloadMap.put("iat", now);
            payloadMap.put("exp", exp);
            String payloadJson = mapper.writeValueAsString(payloadMap);
            String payloadEncoded = encodeBase64Url(payloadJson.getBytes(UTF_8));

            // Signature
            String toSign = headerEncoded + "." + payloadEncoded;
            String signatureEncoded = generateBase64UrlEncodedSignature(toSign, secret);

            return headerEncoded + "." + payloadEncoded + "." + signatureEncoded;

        } catch (Exception e) {
            throw new RuntimeException("Could not create JWT", e);
        }
    }

    public JwtToken(String jwtToken, boolean containsBearer, String secret, Set<String> oldSecrets) {

        if(jwtToken == null || jwtToken.isEmpty() || jwtToken.equals("Bearer null") || jwtToken.equals("Bearer undefined")){
            this.setValid(false);
        } else {

            int numberOfDots = StringUtils.countMatches(jwtToken, ".");

            if (numberOfDots != 2 || (containsBearer && !jwtToken.contains(BEARER))) {
                throw new AuthManipulationException("Auth token has not the correct JWT-Format: " + jwtToken);
            }

            if(containsBearer) {
                jwtToken = jwtToken.split(BEARER)[1].trim();
            }

            String[] tokenParts = jwtToken.split("\\.");

            try {

                this.headerEncoded = tokenParts[0];
                this.payloadEncoded = tokenParts[1];
                this.signatureEncoded = tokenParts[2];

                this.header = decodeBase64Url(headerEncoded);
                this.payload = decodeBase64Url(payloadEncoded);
                this.signature = decodeBase64Url(signatureEncoded);

            } catch (ArrayIndexOutOfBoundsException aioobe) {
                throw new AuthManipulationException(aioobe);
            }

            if (isNullOrEmpty(this.header)) {
                throw new AuthManipulationException("Auth token header is empty");
            }

            if (isNullOrEmpty(this.payload)) {
                throw new AuthManipulationException("Auth token payload is empty");
            }

            if (isNullOrEmpty(this.signature)) {
                throw new AuthManipulationException("Auth token signature is empty");
            }

            try {
                boolean signatureValid = validateToken(jwtToken, secret, oldSecrets);

                this.payloadMap = convertStringToMap(this.getPayload());
                Number expirationNumber = payloadMap.get("exp") instanceof Number number ? number : null;
                this.expiration = expirationNumber == null ? null : expirationNumber.intValue();
                this.expirationDate = expiration == null ? null : epochMillisToLocaldatetime(expiration);

                Number originalUserIdNumber = payloadMap.get(PAYLOAD_ORIGINAL_USER_ID) instanceof Number number ? number : null;
                this.originalUserId = originalUserIdNumber == null ? null : originalUserIdNumber.intValue();
                this.originalRole = payloadMap.get(PAYLOAD_ORIGINAL_ROLE) == null ? null : String.valueOf(payloadMap.get(PAYLOAD_ORIGINAL_ROLE));
                Number originalExpirationNumber = payloadMap.get(PAYLOAD_ORIGINAL_EXPIRATION) instanceof Number number ? number : null;
                this.originalExpiration = originalExpirationNumber == null ? null : originalExpirationNumber.intValue();
                this.originalExpirationDate = originalExpiration == null ? null : epochMillisToLocaldatetime(originalExpiration);

                this.expired = expiration != null && expiration <= Instant.now().getEpochSecond();
                this.isValid = signatureValid && canBeUsed();
                if (signatureValid && !this.isValid) {
                    log.warn("token expired: " + this);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private boolean isNullOrEmpty(String value){
        return value == null || value.isEmpty();
    }

    private boolean validateToken(String jwtToken, String secret, Set<String> oldSecrets) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {

        boolean tokenIsValid = validateToken(jwtToken, secret);

        if (tokenIsValid) {
            log.debug("Current Secret - JWT token is valid: " + jwtToken);
            return true;
        } else {
            log.debug("Current Secret - JWT token not valid: " + jwtToken + " -> trying old secrets");
            int i = 1;
            for (String currentSecret : oldSecrets) {
                boolean currentTokenIsValid = validateToken(jwtToken, currentSecret);
                log.debug("Old Secret " + i + ": JWT token is valid: " + currentTokenIsValid);
                if (currentTokenIsValid) {
                    return true;
                }
                i++;
            }
        }

        return false;
    }

    private boolean validateToken(String jwtToken, String secret) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        String signatureCurrent = this.signatureEncoded;
        String encodedString = this.headerEncoded + "." + this.payloadEncoded;

        String signatureExpected = generateBase64UrlEncodedSignature(encodedString, secret);

        log.debug("Token-Validation: Current signature: " + signatureCurrent);
        log.debug("Token-Validation: Expected (Generated) signature: " + signatureExpected);

        boolean tokenIsValid = signatureExpected.equals(signatureCurrent);
        log.debug("Token " + jwtToken + " is valid: " + tokenIsValid);

        return tokenIsValid;
    }

    private static String generateBase64UrlEncodedSignature(String message, String secret) throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {
        log.debug("Token-Salt (secret): " + secret);
        if (secret == null || secret.isEmpty()) {
            log.debug("Token-Salt secret is empty. Ignoring secret");
            return "";
        }
        byte[] guavaHash = Hashing.hmacSha256(secret.getBytes(UTF_8)).hashString(message, UTF_8).asBytes();
        return encodeBase64Url(guavaHash);
    }


    private static String encodeBase64Url(byte[] message) {
        byte[] encodedMessage = BASE64URL_ENCODER.encode(message);
        return new String(encodedMessage, UTF_8);
    }

    private String decodeBase64Url(String value){
        return new String(BASE64URL_DECODER.decode(value), StandardCharsets.UTF_8);
    }

    private final ObjectMapper objectMapper = new ObjectMapper();

    private HashMap<String, Object> convertStringToMap(String decodedString) throws JsonProcessingException {
        TypeReference<HashMap<String, Object>> typeReference = new TypeReference<>() {};
        return objectMapper.readValue(decodedString, typeReference);
    }

    private LocalDateTime epochMillisToLocaldatetime(long epochMillisTimestamp){
        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epochMillisTimestamp), ZONE_ID_MOSCOW);
    }

    public boolean originalIsValid() {
        return originalExpiration != null && originalExpiration > Instant.now().getEpochSecond();
    }

    public boolean canBeUsed() {
        return !expired || originalIsValid();
    }

    public boolean mustBeSwitchedToOriginal() {
        return expired && originalIsValid();
    }

    public Integer getUserId() {
        Object idRaw = payloadMap.containsKey(PAYLOAD_USER_ID) ? payloadMap.get(PAYLOAD_USER_ID) : payloadMap.get(PAYLOAD_SUBJECT);
        return idRaw instanceof Number number ? number.intValue() : (idRaw == null ? null : Integer.valueOf(String.valueOf(idRaw)));
    }

    public String getRole() {
        return payloadMap.get(PAYLOAD_ROLE) == null ? null : String.valueOf(payloadMap.get(PAYLOAD_ROLE));
    }

    @Override
    public String toString() {
        return "JwtToken{" +
                "header='" + header + '\'' +
                ", payload='" + payload + '\'' +
                ", signature='" + signature + '\'' +
                ", headerEncoded='" + headerEncoded + '\'' +
                ", payloadEncoded='" + payloadEncoded + '\'' +
                ", signatureEncoded='" + signatureEncoded + '\'' +
                '}';
    }
}
