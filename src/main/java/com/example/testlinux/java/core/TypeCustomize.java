package com.example.testlinux.java.core;

import java.math.BigDecimal;

public class TypeCustomize {

    public static void main(String[] st) {
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

        if (d.compareTo(new BigDecimal("0.3")) == 0) {
            System.out.println("TypeCustomize c.compareTo(new BigDecimal" );
        }

        boolean equalsBig = d.equals(b.add(c));
        System.out.println("TypeCustomize equalsBig; " + equalsBig);
    }
}
