package com.example.testlinux.unit.test.web;

public class HttpClient {
    public HttpClient(){
        System.out.println("HttpClient()");
    }
    public String sendRequest(String url) {
        System.out.println("HttpClient sendRequest()");
        return "HttpClient response from " + url;
    }
}
