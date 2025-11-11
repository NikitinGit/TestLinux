package com.example.testlinux.dirtest;


public class TestDirectory {

    private void f() {
        System.out.println("TestDirectory private void f()");
    }

    public static void main (String[] args) {
        var td = new TestDirectory();
        td.f();
    }
}
