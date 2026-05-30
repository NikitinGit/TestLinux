package com.example.testlinux.interfaces;

import org.hibernate.proxy.HibernateProxy;

public class Snake extends Animal {
    @Override
    public void execute() {
        System.out.println("Cat Snake");
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof HibernateProxy) {
            return ((HibernateProxy) o).getHibernateLazyInitializer().isReadOnly();
        }

        return true;
//        return ((HibernateProxy) o).getHibernateLazyInitializer().isReadOnly();
    }

    public void methodOfSnake() {
        System.out.println("methodOfSnake()");
    }
}