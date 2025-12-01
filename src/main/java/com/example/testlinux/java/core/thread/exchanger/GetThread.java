package com.example.testlinux.java.core.thread.exchanger;

import java.util.concurrent.Exchanger;

public class GetThread implements Runnable {

    Exchanger<Integer> exchanger;
    Integer message;

    GetThread(Exchanger<Integer> ex,  int count) {
        this.exchanger = ex;
        message = count;
    }

    public void run() {
        try {
            message = exchanger.exchange(message + 1);
            System.out.println("GetThread has received: " + message);
        } catch (InterruptedException ex) {
            System.out.println(ex.getMessage());
        }
    }
}