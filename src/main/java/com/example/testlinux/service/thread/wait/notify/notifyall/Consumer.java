package com.example.testlinux.service.thread.wait.notify.notifyall;

public class Consumer implements Runnable {
    private Store store;
    public Consumer(Store store) {
        this.store = store;
    }
    public void run () {
        for (int i = 0; i < 10; i++) {
            store.get();
        }
    }
}
