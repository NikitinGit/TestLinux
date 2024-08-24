package com.example.testlinux.unit.test.web;

public class MyService {
    private final HttpClient httpClient;
    private final WebSocketClient webSocketClient;

    public MyService(HttpClient httpClient, WebSocketClient webSocketClient) {
        System.out.println("MyService(HttpClient httpClient, WebSocketClient webSocketClient)");
        this.httpClient = httpClient;
        this.webSocketClient = webSocketClient;
    }

    public void sendRequest(String url) {
        httpClient.sendRequest(url);
    }

    public String fetchDataHttp(String url) {
        System.out.println("MyService fetchData(String url)");
        return httpClient.sendRequest(url);
    }
    public String fetchDataWebSocket(String url) {
        System.out.println("MyService fetchDataWebSocket(String url)");
        return webSocketClient.sendRequest(url);
    }
}
