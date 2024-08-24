package com.example.testlinux.unit.test.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MyServiceTest {
    @Mock
    private HttpClient httpClient; // Имитация HttpClient
    //HttpClient httpClient = mock(HttpClient.class);

    @Mock
    private WebSocketClient webSocketClient;

    @InjectMocks
    private MyService myService; // Внедрение имитации в MyService

    @Test
    public void testFetchDataHttp() {
        // Настройка поведения имитации
        String url = "https://test25.com";
        when(httpClient.sendRequest(anyString())).thenAnswer(invocation -> {
            String url2 = invocation.getArgument(0);
            System.out.println("HttpClient sendRequest() called with URL: " + url2);
            return "response from " + url2;
        });

        when(webSocketClient.sendRequest(anyString())).thenAnswer(invocation -> {
            String url2 = invocation.getArgument(0);
            System.out.println("webSocketClient sendRequest() called with URL: " + url2);
            return "response from " + url2;
        });
        //httpClient.sendRequest(url);// не входит в тело метода и не вызывает логики when(httpClient.sendRequest(url)).thenReturn("mock response");

        String result = myService.fetchDataHttp(url);
        String result2 = myService.fetchDataWebSocket(url + " - 2");
        //assertEquals("mock response", result);
    }
}
