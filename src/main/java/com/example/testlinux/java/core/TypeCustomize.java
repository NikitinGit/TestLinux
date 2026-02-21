package com.example.testlinux.java.core;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TypeCustomize {

    public static void main(String[] st) {
        long xn = 2_000_000_000L;  // младшие 32 бита начинаются с 0...
        int y = (int) xn;          // ...положительное
        System.out.println("y; " + y);

        String test = "25".repeat(125);
        System.out.println("test; " + test);

        BigInteger bI = BigInteger.valueOf(Long.MAX_VALUE).multiply(BigInteger.valueOf(1000000));
        System.out.println("1. " + bI);
        System.out.println("2. " + Long.MAX_VALUE);

        long maxLong = Long.MAX_VALUE;
        System.out.println("TypeCustomize maxLong; " + maxLong);

        double dubleFromLong = Long.MAX_VALUE;
        System.out.println("TypeCustomize dubleFromLong; " + dubleFromLong);

        dubleFromLong = maxLong;
        System.out.println("TypeCustomize dubleFromLong; " + dubleFromLong);

        boolean double9 = dubleFromLong < 9.223372036854777E18;
        System.out.println("TypeCustomize double9; " + double9);

        int fInt1 = (int) dubleFromLong;
        System.out.println("TypeCustomize fInt1; " + fInt1);

        long big = 3_000_000_000L;
        int i = (int) big;   // получишь отрицательное число
        System.out.println("TypeCustomize i; " + i);

        double a = 0.1 + 0.2;
        double x = 0.3;
        System.out.println("TypeCustomize a == x; " + (a == x));

        BigDecimal b = new BigDecimal("0.1");
        BigDecimal c = new BigDecimal("0.2");
        BigDecimal d = b.add(c); // 0.3 точно
        System.out.println("b.add(c) d; " + d);

        b = new BigDecimal("25.8");
        c = new BigDecimal("25.5");
        d = b.subtract(c);
        System.out.println("b.subtract(c) d; " + d);

        double e = Double.MAX_VALUE + 10000000005d;
        System.out.println("e: " + e + "\ne > Double.MAX_VALUE: " + (e > Double.MAX_VALUE));

        if (d.compareTo(new BigDecimal("0.3")) == 0) {
            System.out.println("TypeCustomize c.compareTo(new BigDecimal" );
        }

        boolean equalsBig = d.equals(b.add(c));
        System.out.println("TypeCustomize equalsBig; " + equalsBig);

        int longInt = (int)(Long.MAX_VALUE - 255555555L);
        System.out.println("longInt; " + longInt);

        //int exp = Math.toIntExact(Long.MAX_VALUE);
    }
}
