package com.example.testlinux.unit.test;

import com.example.testlinux.domain.UserNew;
import com.example.testlinux.dto.UserDto;
import com.example.testlinux.repository.UserNewRepository;
import com.example.testlinux.service.UserService;

import com.example.testlinux.service.YooKassaService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;
/*import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;*/
/*import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
//@PropertySource("classpath:application.properties") //not working
//@TestPropertySource(locations = "classpath:application.properties")
@Slf4j
public class UnitTest {

    @InjectMocks
    private UserService userService;

    @InjectMocks
    private YooKassaService yooKassaService;

    @Test
    void sum(){
        assertEquals(8, userService.getSum(5, 3));
        userService.getUsers();
    }

    @Test
    void addUser() {
        userService.addUser(new UserNew("Nikitin 2","nikitin2.@gmail.com"));
        userService.getUsers();
    }

    @Test
    void getValuesOfResponseITWORK(){
        try {
            String url = "https://api.yookassa.ru/v3/payments";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");

            String auth = "465439:test_Jr8CqgCR_H777fgC41j4SSfmZM5GFpVXEuAHMSTqgxI";
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes("UTF-8"));
            con.setRequestProperty("Authorization", "Basic " + encodedAuth);

            con.setRequestProperty("Idempotence-Key", UUID.randomUUID().toString());

            String jsonInputString = "{\"amount\":{\"value\":\"1000.00\",\"currency\":\"RUB\"},"
                    + "\"capture\":true,"
                    + "\"confirmation\":{\"type\":\"redirect\",\"return_url\":\"https://example.com/success\"},"
                    + "\"description\":\"Заказ №1\"}";

            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            BufferedReader in;
            if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                in = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println("Response: " + response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void testRestTemplateYouKassa(){
        yooKassaService.registerPayment();
        //"id" : "2e89b7b0-000f-5000-a000-10d595a2ce4d"
    }

    @Test
    void testGetPaymentDataYouKassa(){
        //"id" : "2e8a2610-000f-5000-a000-122fc0aa0cea"
        yooKassaService.getPaymentDetails("2e8a2610-000f-5000-a000-122fc0aa0cea");
        yooKassaService.getPaymentDetails("2e88d1c7-000f-5000-9000-16526fd4dad5");
        //        2e88d1c7-000f-5000-9000-16526fd4dad5
    }

    @Test
    void testHandleNotification(){
        List<String> allowedIps = List.of(
                "77.75.156.11",
                "77.75.156.35",
                "185.71.76.5", // Доступные хосты: 185.71.76.1 - 185.71.76.30
                "185.71.76.2", // Доступные хосты: 185.71.76.1 - 185.71.76.30
                "185.71.76.25", // Доступные хосты: 185.71.76.1 - 185.71.76.30
                "185.71.77.2",
                "77.75.153.21", // Доступные хосты: 77.75.153.1 - 77.75.153.126
                "77.75.154.129",
                "77.75.154.222",
                "2a02:5180::ffff"
        );
        for(String allowedIp : allowedIps) {
            assertTrue(yooKassaService.isIpAllowed(allowedIp));
        }
    }

    /*@Test
    public Map<String, String> getValuesOfResponse(FighterPaymentDto clientDto) throws JsonProcessingException {
        String orderNumber = UUID.randomUUID().toString().replace("-", "");
        log.info("orderNumber; {}", orderNumber);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> testReg = new HashMap<>();
        testReg.put("userName", SberPayData.LOGIN);
        testReg.put("password", SberPayData.PASSWORD);
        testReg.put("orderNumber", orderNumber);
        testReg.put("returnUrl", "https://strikerstat.com/sberPay/urlSuccess.php?id=" + clientDto.getIdEvent());
        testReg.put("failUrl", "https://strikerstat.com/sberPay/urlFail.php?id=" + clientDto.getIdEvent());
        testReg.put("amount", clientDto.getDeposit() * 100);

        String url = "https://securepayments.sberbank.ru/payment/rest/register.do";

        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        testReg.forEach((key, value) -> formData.add(key, value.toString()));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(formData, headers);
        String response = restTemplate.postForObject(url, request, String.class);
        log.info("PayService restTemplate.postForObject response; {}", response);

        ObjectMapper mapper = new ObjectMapper();

        return mapper.readValue(response, new TypeReference<Map<String, String>>() {});
    }*/
}
