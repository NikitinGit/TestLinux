package com.example.testlinux.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;

@Slf4j
@Service
public class YooKassaService {
    private final String URL = "https://api.yookassa.ru/v3/payments";
    private final String shopId = "465439";
    private final String secretKey = "test_pTudOuL99eJ5nQ-aUYGKt7-_nVaNNWqga3y7GU4y4ok";
    private final String AUTH = shopId + ":" + secretKey;
    private boolean bin;

    public void registerPayment() {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Установка заголовков
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            String encodedAuth = Base64.getEncoder().encodeToString(AUTH.getBytes(StandardCharsets.UTF_8));
            headers.set("Authorization", "Basic " + encodedAuth);
            headers.set("Idempotence-Key", UUID.randomUUID().toString());

            double payment = 555.55123345567d;
            double roundPayment = Math.round(payment * 100.0 ) / 100.0;
            // Данные платежа
            String jsonInputString = "{\"amount\":{\"value\":" + roundPayment + ",\"currency\":\"RUB\"},"
                    + "\"capture\":true,"
                    + "\"confirmation\":{\"type\":\"redirect\",\"return_url\":\"https://example.com/success\"},"
                    + "\"description\":\"Заказ №5\"}";

            HttpEntity<String> requestEntity = new HttpEntity<>(jsonInputString, headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(URL, HttpMethod.POST,
                    requestEntity, String.class);
            System.out.println("Response Code : " + responseEntity.getStatusCodeValue());
            System.out.println("Response: " + responseEntity.getBody());

            try {
                ObjectMapper objectMapper = new ObjectMapper();
                PaymentResponse response = objectMapper.readValue(responseEntity.getBody(), PaymentResponse.class);

                // Получение значений
                String value = response.getAmount().getValue();
                String confirmationUrl = response.getConfirmation().getConfirmationUrl();
                String status = response.getStatus();

                System.out.println("Value: " + value);
                System.out.println("Type: " + response.getConfirmation().getType());
                System.out.println("Confirmation URL: " + confirmationUrl);
                System.out.println("Status: " + status);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getPaymentDetails(String paymentId) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // Установка заголовков
            HttpHeaders headers = new HttpHeaders();
            String encodedAuth = Base64.getEncoder().encodeToString(AUTH.getBytes(StandardCharsets.UTF_8));
            headers.set("Authorization", "Basic " + encodedAuth);

            // Создание объекта запроса
            HttpEntity<String> requestEntity = new HttpEntity<>(headers);

            // Отправка GET-запроса
            ResponseEntity<String> responseEntity = restTemplate.exchange(URL +"/" + paymentId, HttpMethod.GET,
                    requestEntity, String.class);

            // Получение ответа
            System.out.println("Response Code : " + responseEntity.getStatusCodeValue());
            System.out.println("Response: " + responseEntity.getBody());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private final List<String> allowedIps = List.of(
            "77.75.156.11",
            "77.75.156.35"
    );

    //
    private final List<String> allowedCidrIps = List.of(
            "185.71.76.0/27", // Доступные хосты: 185.71.76.1 - 185.71.76.30
            "185.71.77.0/27",
            "77.75.153.0/25", // Доступные хосты: 77.75.153.1 - 77.75.153.126
            "77.75.154.128/25"
    );

    private final String ipV6 = "2a02:5180::/32";

    public boolean isIpAllowed(String ipOfSender) {
        try {
            if (ipV6.substring(0, 11).contains(ipOfSender.substring(0, 11))){
                return true;
            }

            for (String allowedIp : allowedIps) {
                if (allowedIp.contains(ipOfSender)){
                    return true;
                }
            }

            for (String allowedIp : allowedCidrIps) {
                if (allowedIp.substring(0, 10).contains(ipOfSender.substring(0, 10))){
                    return true;
                }
            }


        } catch (Exception e) {
            log.error("isIpAllowed Exception ipOfSender; {}", ipOfSender);
            e.printStackTrace();
        }

        return false; // IP not allowed
    }
}
