package com.example.testlinux.customization;

import java.math.BigDecimal;

public class DigitType {

    public static void main(String[] test) {
        boolean a = true;
        // byte b = a; - не работает
        byte b = a ? (byte) 1 : (byte)0;
        System.out.println("b; " + b);

        int c = b + 2;
        System.out.println("c; " + c);
        float d = b + 5.2f;
        System.out.println("d; " + d);

        double e = b - 7.2f;
        System.out.println("e; " + e);

        double f = b - 7.2d; // d - D лишнее
        System.out.println("f; " + f);

        f = b - 7.2;
        System.out.println("f; " + f);

        BigDecimal g = BigDecimal.valueOf(b - 7.2f);
        System.out.println("g; " + g);

        BigDecimal h = BigDecimal.valueOf(b - 7.2d);
        System.out.println("h; " + h);

        h = BigDecimal.valueOf(b - 7.2);
        System.out.println("h; " + h);

        Integer i = 127;
        Integer j = 127;
        System.out.println("i == j; " + (i == j));
        System.out.println("i.equals(j); " + (i.equals(j)));

        i = 128;
        j = 128;
        System.out.println("i == j; " + (i == j));
        System.out.println("i.equals(j); " + (i.equals(j)));

        i = ~i + 1;
        j = ~j + 1;
        System.out.println("i == j; " + (i == j) + ", i: " + i);
        System.out.println("i.equals(j); " + (i.equals(j)));

        i = ~i + 2;
        j = ~j + 2;
        System.out.println("i == j; " + (i == j) + ", i: " + i);
        System.out.println("i.equals(j); " + (i.equals(j)));

        // ===== short =====
        // short, как и byte, в арифметике авто-расширяется до int
        short s1 = 100;
        short s2 = 200;
        // short s3 = s1 + s2; // ошибка: результат s1+s2 уже int
        short s3 = (short) (s1 + s2);
        System.out.println("s3: " + s3);

        // ===== long =====
        // суффикс L обязателен для больших литералов (иначе ошибка компиляции)
        // long overflow = 9999999999; // ошибка: int не вмещает
        long big = 9_999_999_999L; // _ для читаемости
        System.out.println("big: " + big);

        // переполнение long (беззвучное, как у int)
        long maxLong = Long.MAX_VALUE;
        System.out.println("Long.MAX_VALUE + 1: " + (maxLong + 1)); // станет Long.MIN_VALUE

        // Long кэш так же как Integer: только -128..127 через ==
        Long l1 = 127L;
        Long l2 = 127L;
        System.out.println("Long 127 == : " + (l1 == l2));   // true
        l1 = 128L;
        l2 = 128L;
        System.out.println("Long 128 == : " + (l1 == l2));   // false

        // ===== char =====
        // char - беззнаковый 16-битный числовой тип (0..65535)
        char ch = 'A';
        int chInt = ch;           // A = 65
        char chNext = (char)(ch + 1); // 'B'
        System.out.println("char 'A' как int: " + chInt);
        System.out.println("'A' + 1 = " + chNext);

        // char + char = int (не char!)
        // char sum = 'A' + 'B'; // ошибка
        int charSum = 'A' + 'B'; // 65 + 66 = 131
        System.out.println("'A' + 'B' = " + charSum);

        // ===== int overflow =====
        int maxInt = Integer.MAX_VALUE; // 2_147_483_647
        System.out.println("MAX_VALUE + 1: " + (maxInt + 1)); // -2_147_483_648 (MIN_VALUE)

        // Math.abs(MIN_VALUE) — ловушка! результат отрицательный
        System.out.println("Math.abs(MIN_VALUE): " + Math.abs(Integer.MIN_VALUE)); // -2147483648

        // ===== деление =====
        // int / int = int (дробная часть отбрасывается, не округляется)
        System.out.println("7 / 2 = " + (7 / 2));        // 3, не 3.5
        System.out.println("7 / 2.0 = " + (7 / 2.0));    // 3.5
        System.out.println("(double)7 / 2 = " + ((double)7 / 2)); // 3.5

        // деление int на 0 — исключение; double на 0.0 — Infinity/NaN
        System.out.println("1.0 / 0.0 = " + (1.0 / 0.0));   // Infinity
        System.out.println("-1.0 / 0.0 = " + (-1.0 / 0.0)); // -Infinity
        System.out.println("0.0 / 0.0 = " + (0.0 / 0.0));   // NaN

        // NaN не равен самому себе!
        double nan = Double.NaN;
        System.out.println("NaN == NaN: " + (nan == nan));           // false
        System.out.println("Double.isNaN(nan): " + Double.isNaN(nan)); // true

        // ===== сужающее приведение (narrowing cast) =====
        // данные теряются молча
        int big2 = 300;
        byte narrowed = (byte) big2; // 300 % 256 = 44
        System.out.println("(byte) 300 = " + narrowed); // 44

        double precise = 9.99;
        int truncated = (int) precise; // дробная часть отбрасывается
        System.out.println("(int) 9.99 = " + truncated); // 9

        // ===== float точность =====
        // 0.1 + 0.2 != 0.3 из-за представления в двоичной системе
        System.out.println("0.1 + 0.2 == 0.3: " + (0.1 + 0.2 == 0.3)); // false
        System.out.println("0.1 + 0.2 = " + (0.1 + 0.2));              // 0.30000000000000004
        // правильное сравнение double — через epsilon или BigDecimal
        System.out.println("BigDecimal: " + (new BigDecimal("0.1").add(new BigDecimal("0.2")))); // 0.3

        // ===== классы-обёртки =====
        Integer test1 = 625;
        Float test2 = 875f;
        Double test3 = 775.0;

        // (Double) test1 — ошибка компиляции: Inconvertible types
        // Integer и Double — разные классы, не связанные между собой.
        // Java позволяет кастить только по иерархии наследования.
        // Integer extends Number, Double extends Number — но друг другу они не родственники.

        // (double) test1 — работает: сначала unboxing Integer->int, потом widening int->double
        test3 = (double) test1; // Integer -> int -> double -> Double (autoboxing)
        System.out.println("test3: " + test3); // 625.0

        // аналогично через intValue() — явный unboxing
        test3 = (double) test1.intValue();
        System.out.println("test3 через intValue: " + test3); // 625.0

        // конвертация между обёртками — только через примитивы или Number
        test3 = test1.doubleValue(); // Number.doubleValue() — самый читаемый способ
        System.out.println("test3 через doubleValue: " + test3); // 625.0

        test2 = test1.floatValue();
        System.out.println("test2 через floatValue: " + test2); // 625.0

        // autoboxing / unboxing
        int primitive = test1;       // unboxing: Integer -> int
        Integer boxed = primitive;   // autoboxing: int -> Integer
        System.out.println("unboxing/autoboxing: " + primitive + " / " + boxed);

        // ловушка: NullPointerException при unboxing null
        Integer nullInt = null;
        try {
            int x = nullInt; // NullPointerException!
        } catch (NullPointerException npe) {
            System.out.println("unboxing null -> NullPointerException");
        }

        // иерархия: все обёртки наследуют Number
        Number n = test1;  // Integer -> Number (полиморфизм)
        System.out.println("Number.intValue: " + n.intValue());
        System.out.println("Number.doubleValue: " + n.doubleValue());
    }
}
