package com.example.testlinux.interfaces;

import org.hibernate.proxy.HibernateProxy;

public class Cat extends Animal {
    @Override
    public void execute() {
        System.out.println("Cat Woof");
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof HibernateProxy) {
            return ((HibernateProxy) o).getHibernateLazyInitializer().isReadOnly();
        }

        return true;
//        return ((HibernateProxy) o).getHibernateLazyInitializer().isReadOnly();
    }

    public void methodOfCat() {
        System.out.println("methodOfCat()");
    }
}