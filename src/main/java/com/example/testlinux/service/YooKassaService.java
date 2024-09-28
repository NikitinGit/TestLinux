package com.example.testlinux.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;

import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class YooKassaService {

    private static final String URL = "https://api.yookassa.ru/v3/payments";
    private static final String AUTH = "465439:test_Jr8CqgCR_H777fgC41j4SSfmZM5GFpVXEuAHMSTqgxI"; // Замените на ваши учетные данные

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

            //////////////////////////////

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

}
