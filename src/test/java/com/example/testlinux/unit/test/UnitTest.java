package com.example.testlinux.unit.test;

import com.example.testlinux.domain.UserNew;
import com.example.testlinux.dto.UserDto;
import com.example.testlinux.repository.UserNewRepository;
import com.example.testlinux.service.UserService;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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

@ExtendWith(MockitoExtension.class)
//@PropertySource("classpath:application.properties") //not working
@TestPropertySource(locations = "classpath:application.properties")
@Slf4j
public class UnitTest {
    @Mock
    private UserNewRepository userNewRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void sum(){
        assertEquals(8, userService.getSum(5, 3));
        userService.getUsers();
    }

    @Test
    void getUser(){
        List<UserNew> userNews = userNewRepository.findAllUsers();
        ArrayList<UserDto> usersDto = new ArrayList<>();
        for(UserNew userNew : userNews){
            System.out.println("UserService getUsers() User.getName(); " + userNew.getName());
            usersDto.add(new UserDto(userNew.getName()));
        }
    }

    @Test
    void getValuesOfResponse1() throws JsonProcessingException {
        try {
            String url = "https://api.yookassa.ru/v3/payments";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Установка метода запроса
            con.setRequestMethod("POST");
            System.out.println(con.getRequestMethod());
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Basic " +
                    Base64.getEncoder().encodeToString("465439:test_Jr8CqgCR_H777fgC41j4SSfmZM5GFpVXEuAHMSTqgxI".getBytes()));
            con.setRequestProperty("Idempotence-Key", UUID.randomUUID().toString());

            // Данные платежа
            String jsonInputString = "{\"amount\":{\"value\":\"1000.00\",\"currency\":\"RUB\"},"
                    + "\"capture\":true,"
                    + "\"confirmation\":{\"type\":\"redirect\",\"return_url\":\"https://example.com/success\"},"
                    + "\"description\":\"Заказ №1\"}";

            // Отправка данных
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            // Обработка ответа (не показано для краткости)


            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
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
    void getValuesOfResponseITWORK(){
        try {
            String url = "https://api.yookassa.ru/v3/payments";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Установка метода запроса
            con.setRequestMethod("POST");

            // Установка заголовков
            con.setRequestProperty("Content-Type", "application/json");

            // Убедитесь, что вы используете правильные shop_id и secret_key
            String auth = "465439:test_Jr8CqgCR_H777fgC41j4SSfmZM5GFpVXEuAHMSTqgxI"; // Замените на ваши учетные данные
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes("UTF-8"));
            con.setRequestProperty("Authorization", "Basic " + encodedAuth);

            // Генерация уникального Idempotence-Key для каждого запроса
            con.setRequestProperty("Idempotence-Key", UUID.randomUUID().toString());

            // Данные платежа
            String jsonInputString = "{\"amount\":{\"value\":\"1000.00\",\"currency\":\"RUB\"},"
                    + "\"capture\":true,"
                    + "\"confirmation\":{\"type\":\"redirect\",\"return_url\":\"https://example.com/success\"},"
                    + "\"description\":\"Заказ №1\"}";

            // Отправка данных
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Получение кода ответа от сервера
            int responseCode = con.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            // Обработка ответа
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
            // Выводим ответ сервера
            System.out.println("Response: " + response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    void getValuesOfResponse3() throws JsonProcessingException {
        try {
            String url = "https://api.yookassa.ru/v3/payments";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // Установка метода запроса
            con.setRequestMethod("POST");
            System.out.println(con.getRequestMethod());

            // Установка заголовков
            con.setRequestProperty("Content-Type", "application/json");

            // Убедитесь, что вы используете правильные shop_id и secret_key
            String auth = "465439:test_Jr8CqgCR_H777fgC41j4SSfmZM5GFpVXEuAHMSTqgxI"; // Замените на ваши учетные данные
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes("UTF-8"));
            con.setRequestProperty("Authorization", "Basic " + encodedAuth);

            // Генерация уникального Idempotence-Key для каждого запроса
            con.setRequestProperty("Idempotence-Key", UUID.randomUUID().toString());

            // Данные платежа с информацией о чеке
            String jsonInputString = "{\"amount\":{\"value\":\"1000.00\",\"currency\":\"RUB\"},"
                    + "\"capture\":true,"
                    + "\"confirmation\":{\"type\":\"redirect\",\"return_url\":\"https://example.com/success\"},"
                    + "\"description\":\"Заказ №1\","
                    + "\"receipt\":{"
                    + "\"items\":["
                    + "{"
                    + "\"description\":\"Товар 1\","
                    + "\"quantity\":1,"
                    + "\"amount\":{\"value\":\"1000.00\",\"currency\":\"RUB\"},"
                    + "\"vat_code\":1" // Укажите код НДС (например, 1 - 20%)
                    + "}"
                    + "]"
                    + "}}";

            // Отправка данных
            con.setDoOutput(true);
            try (OutputStream os = con.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // Получение кода ответа от сервера
            int responseCode = con.getResponseCode();
            System.out.println("Response Code : " + responseCode);

            // Обработка ответа
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

            // Выводим ответ сервера
            System.out.println("Response: " + response.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
