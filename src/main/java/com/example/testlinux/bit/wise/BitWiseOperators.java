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

        System.out.println("~d + 1; " + (~d + 1));

        int f = -5;
        System.out.println("~f + 1; " + (~f + 1));

        int red = 255;
        int green = 128;
        int blue = 64;

        // Сдвигаем и объединяем
        int rgb1 = (red << 16) | (green << 8) | blue;
        int rgb2 = (red << 16) + (green << 8) + blue;
        System.out.println("rgb1; " + rgb1 + "\nrgb2; " + rgb2);

        int mask = 0xFF;
        System.out.println("Integer.toBinaryString(mask); " + Integer.toBinaryString(mask));

        System.out.println("Integer.toBinaryString(rgb1); " + Integer.toBinaryString(rgb1));

        int green2 = (rgb1 >> 8) & mask;
        System.out.println("Integer.toBinaryString(green2); " + Integer.toBinaryString(green2));

        int blue2 = rgb1 & mask;
        System.out.println("blue2; " + blue2);
    }
}
