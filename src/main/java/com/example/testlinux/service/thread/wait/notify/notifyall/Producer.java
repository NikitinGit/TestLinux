package com.example.testlinux.service.thread.wait.notify.notifyall;

public class Producer implements Runnable{
    private Store store;
    public Producer(Store store) {
        this.store = store;
    }
    public void run () {
        for (int i = 0; i < 10; i++) {
            store.put();
        }
    }
}
