package com.example.testlinux.unit.test.web;

public class WebSocketClient {
    public String sendRequest(String url) {
        System.out.println("WebSocketClient sendRequest()");
        return "WebSocketClient response from " + url;
    }
}
