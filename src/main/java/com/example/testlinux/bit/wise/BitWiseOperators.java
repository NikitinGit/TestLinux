package com.example.testlinux.bit.wise;

public class BitWiseOperators {

    public static void main(String[] text) {
        int a = 8; // В битах: 0000...00001000
        int b = a >>> 2;
        System.out.println("b; " + b);

        b = a >>> 1;
        System.out.println("b; " + b);

        int c = -2;
        System.out.println("c >>> 1; " + (c >>> 1));

        int d = 5;
        System.out.println("~d; " + (~d));
        System.out.println("Двоичный вид (~d): " + Integer.toBinaryString((~d)));

    }
}
