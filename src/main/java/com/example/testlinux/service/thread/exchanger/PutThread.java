package com.example.testlinux.service.thread.exchanger;

import java.util.concurrent.Exchanger;

public class PutThread implements Runnable{

    Exchanger<Integer> exchanger;
    int message;

    PutThread(Exchanger<Integer> ex, int count){
        this.exchanger=ex;
        message = count;
    }
    public void run(){
        try{
            message=exchanger.exchange(message + 1);
            System.out.println("PutThread has received: " + message);
        }
        catch(InterruptedException ex){
            System.out.println(ex.getMessage());
        }
    }
}