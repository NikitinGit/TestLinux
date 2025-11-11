package com.example.testlinux.polymorphism;

public class PrivateOverride {
    private final void f() {
        System.out.println("private void f()");
    }

    public static void main (String[] args) {
        PrivateOverride po = new Derived();
        po.f();
    }
}

class Derived extends PrivateOverride {
    public void f() {
        System.out.println("public void f()");
    }
    public void f2() {
        System.out.println("public void f()");
    }
}
