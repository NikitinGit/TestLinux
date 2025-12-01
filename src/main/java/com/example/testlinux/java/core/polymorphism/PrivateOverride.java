package com.example.testlinux.java.core.polymorphism;

public class PrivateOverride {
    private void f() {
        System.out.println("private void f(); " + hashCode());
    }

    public static void main (String[] args) {
        PrivateOverride po = new PrivateOverride();
        po.f();

        Derived derived = new Derived();
        derived.f();

        PrivateOverride test = (PrivateOverride)derived;
        Derived test1 = (Derived) test; //ERROR

        System.out.println("PrivateOverride test equals Derived test1; " + test.equals(po));
        test.f();
    }
}

class Derived extends PrivateOverride {
    public void f() {
        System.out.println("public void f()  + hashCode(); "  + hashCode());
    }
}
