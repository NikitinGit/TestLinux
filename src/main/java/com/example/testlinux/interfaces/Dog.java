package com.example.testlinux.interfaces;

import org.hibernate.proxy.HibernateProxy;

public class Dog extends Animal {
    @Override
    public void execute() {
        System.out.println("Dog Woof");
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof HibernateProxy) {
            return ((HibernateProxy) o).getHibernateLazyInitializer().isReadOnly();
        }

        return true;
//        return ((HibernateProxy) o).getHibernateLazyInitializer().isReadOnly();
    }

    public void methodOfDog() {
        System.out.println("methodOfDog()");
    }
}
