package com.example.testlinux.customization;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

public class DigitType {

    // Ctrl правый +Shift левый +- (минус) — свернуть все блоки в файле
    public static void main(String[] test) {
        Integer a = 23458;
        Integer b = 23458;
        int c = a;
        System.out.println("Правило простое: если хотя бы одна сторона == — примитив, обёртка распаковывается. " +
                "\na.equals(23458); " + a.equals(23458) +
                "\n, a.equals(b); " + a.equals(b) +
                "\n, c == a; " + (c == a) +
                "\n, c == b; " + (c == b) +
                "\n, c == 23458; " + (c == 23458) +
                "\n, b == a; " + (b == a) +
                "\n, unBoxingIsWork(a, b); " + unBoxingIsWork(a, b) +
                "\n, b == 23458; " + (b == 23458)
        );
        booleanToByte();
        arithmeticPromotion();
        bigDecimalPrecision();
        integerCache();
        shortPromotion();
        longOverflowAndCache();
        charAsNumber();
        intOverflow();
        division();
        narrowingCast();
        floatPrecision();
        wrapperConversion();
        autoboxingAndUnboxing();
        parseStringToNumber();
        numberSystems();
    }

    static boolean unBoxingIsWork(int a, int b) {
        return a == b;
    }
    // Можно ли напрямую присвоить boolean в byte? Если нет — как правильно преобразовать?
    static void booleanToByte() {
        boolean a = true;
        // byte b = a; - не работает: incompatible types
        byte b = a ? (byte) 1 : (byte) 0;
        System.out.println("boolean->byte: " + b);
    }

    // Что происходит с типом при арифметике byte+int, byte+float, byte-double? Почему суффикс d у double-литерала избыточен?
    static void arithmeticPromotion() {
        byte b = 1;
        int c = b + 2;          // byte + int = int
        float d = b + 5.2f;     // byte + float = float
        double e = b - 7.2f;    // byte - float = float -> widening -> double
        double f = b - 7.2d;    // d/D — избыточно, double и так по умолчанию
        double f2 = b - 7.2;    // то же самое без суффикса
        System.out.println("byte+int: " + c);
        System.out.println("byte+float: " + d);
        System.out.println("byte-float->double: " + e);
        System.out.println("byte-7.2d: " + f + " == byte-7.2: " + f2);
    }

    // Почему BigDecimal.valueOf(float) и BigDecimal.valueOf(double) дают разные результаты для одного числа? Какой точнее?
    static void bigDecimalPrecision() {
        byte b = 1;
        BigDecimal fromFloat  = BigDecimal.valueOf(b - 7.2f); // float менее точный
        BigDecimal fromDouble = BigDecimal.valueOf(b - 7.2);  // double точнее
        System.out.println("BigDecimal от float:  " + fromFloat);
        System.out.println("BigDecimal от double: " + fromDouble);
    }

    // Integer 127==127 и 128==128 — что вернёт == и equals? Почему разный результат? Что будет после ~i+1 и ~i+2?
    static void integerCache() {
        Integer i = 127;
        Integer j = 127;
        System.out.println("127 == : " + (i == j));       // true (кэш -128..127)
        System.out.println("127 equals: " + (i.equals(j)));

        i = 128;
        j = 128;
        System.out.println("128 == : " + (i == j));       // false (вне кэша)
        System.out.println("128 equals: " + (i.equals(j)));

        i = ~i + 1; // ~128 + 1 = -128 (в кэше!)
        j = ~j + 1;
        System.out.println("~128+1 == : " + (i == j) + ", i=" + i);   // true
        System.out.println("~128+1 equals: " + (i.equals(j)));

        i = ~i + 2; // ~(-128) + 2 = 129 (вне кэша)
        j = ~j + 2;
        System.out.println("~(-128)+2 == : " + (i == j) + ", i=" + i); // false
        System.out.println("~(-128)+2 equals: " + (i.equals(j)));
    }

    // Можно ли сложить два short и записать в short без cast? Почему возникает ошибка компиляции?
    static void shortPromotion() {
        short s1 = 100;
        short s2 = 200;
        // short s3 = s1 + s2; // ошибка: s1+s2 уже int (arithmetic promotion)
        short s3 = (short) (s1 + s2);
        System.out.println("short+short->cast->short: " + s3);
    }

    // Нужен ли суффикс L для 9_999_999_999? Что при Long.MAX_VALUE+1? Работает ли кэш == для Long как для Integer?
    static void longOverflowAndCache() {
        // long big = 9999999999; // ошибка компиляции: int не вмещает
        long big = 9_999_999_999L;
        System.out.println("long литерал: " + big);

        long maxLong = Long.MAX_VALUE;
        System.out.println("Long.MAX_VALUE + 1: " + (maxLong + 1)); // Long.MIN_VALUE

        Long l1 = 127L;
        Long l2 = 127L;
        System.out.println("Long 127 == : " + (l1 == l2));  // true (кэш)
        l1 = 128L;
        l2 = 128L;
        System.out.println("Long 128 == : " + (l1 == l2));  // false (вне кэша)
    }

    // char — числовой тип? Какое значение у 'A'? Что вернёт 'A'+'B' — char или int?
    static void charAsNumber() {
        char ch = 'A';
        int chInt = ch;              // 'A' = 65
        char chNext = (char)(ch + 1);// 'B'
        System.out.println("'A' как int: " + chInt);
        System.out.println("'A' + 1 = '" + chNext + "'");

        // char + char = int, не char!
        // char sum = 'A' + 'B'; // ошибка компиляции
        int charSum = 'A' + 'B'; // 65 + 66 = 131
        System.out.println("'A' + 'B' = " + charSum);
    }

    // Что вернёт Integer.MAX_VALUE+1? Что вернёт Math.abs(Integer.MIN_VALUE) — почему это ловушка?
    static void intOverflow() {
        int maxInt = Integer.MAX_VALUE;
        System.out.println("MAX_VALUE + 1: " + (maxInt + 1)); // MIN_VALUE

        // Math.abs(MIN_VALUE) нельзя представить как положительный int — остаётся отрицательным!
        System.out.println("Math.abs(MIN_VALUE): " + Math.abs(Integer.MIN_VALUE));
    }

    // Что вернёт 7/2? Как получить 3.5? Что при делении double на 0.0? Чем NaN особенен при ==?
    static void division() {
        System.out.println("7 / 2 = " + (7 / 2));              // 3 (не 3.5!)
        System.out.println("7 / 2.0 = " + (7 / 2.0));          // 3.5
        System.out.println("(double)7/2 = " + ((double)7 / 2)); // 3.5

        System.out.println("1.0/0.0 = " + (1.0 / 0.0));    // Infinity
        System.out.println("-1.0/0.0 = " + (-1.0 / 0.0));  // -Infinity
        System.out.println("0.0/0.0 = " + (0.0 / 0.0));    // NaN

        double nan = Double.NaN;
        System.out.println("NaN == NaN: " + (nan == nan));              // false!
        System.out.println("Double.isNaN: " + Double.isNaN(nan));       // true
        System.out.println("Objects.equals(NaN,NaN): " + Objects.equals(nan, nan)); // true
    }

    // Что будет при (byte)300? Как вычислить? Что при (int)9.99 — округление или отсечение?
    static void narrowingCast() {
        int big = 300;
        byte narrowed = (byte) big; // 300 % 256 = 44
        System.out.println("(byte) 300 = " + narrowed);

        double precise = 9.99;
        int truncated = (int) precise; // дробная часть отбрасывается, не округляется
        System.out.println("(int) 9.99 = " + truncated); // 9
    }

    // Что вернёт 0.1+0.2 == 0.3? Как правильно складывать и сравнивать дробные числа точно?
    static void floatPrecision() {
        System.out.println("0.1+0.2 == 0.3: " + (0.1 + 0.2 == 0.3));  // false
        System.out.println("0.1+0.2 = " + (0.1 + 0.2));                // 0.30000000000000004
        System.out.println("BigDecimal: " + new BigDecimal("0.1").add(new BigDecimal("0.2"))); // 0.3
    }

    // Почему (Double)integerObj — ошибка, а (double)integerObj — нет? Какой самый читаемый способ конвертировать Integer в Double?
    static void wrapperConversion() {
        Integer val = 625;

        // (Double) val — ошибка: Integer и Double разные ветки, не кастятся напрямую
        // val нужно сначала распаковать в примитив

        double via_cast   = (double) val;       // unboxing -> widening -> autoboxing
        double via_int    = (double) val.intValue();
        double via_method = val.doubleValue();  // самый читаемый (метод из Number)
        System.out.println("(double) val: " + via_cast);
        System.out.println("intValue->cast: " + via_int);
        System.out.println("doubleValue(): " + via_method);
    }

    // Что такое autoboxing и unboxing? Что при unboxing null? Как обёртки связаны через Number?
    static void autoboxingAndUnboxing() {
        Integer boxed = 42;      // autoboxing: int -> Integer (Integer.valueOf(42))
        int primitive = boxed;   // unboxing:   Integer -> int (boxed.intValue())
        System.out.println("autoboxing/unboxing: " + boxed + " / " + primitive);

        Integer nullInt = null;
        try {
            int x = nullInt; // NullPointerException при unboxing null!
        } catch (NullPointerException npe) {
            System.out.println("unboxing null -> NullPointerException");
        }

        // все обёртки наследуют Number
        Number n = boxed;
        System.out.println("Number.intValue: " + n.intValue());
        System.out.println("Number.doubleValue: " + n.doubleValue());
    }

    // Чем parseInt() отличается от valueOf()? Что кидает NFE? Нужен ли trim() перед парсингом?
    static void parseStringToNumber() {
        // parseInt -> примитив, valueOf -> обёртка (использует кэш)
        int fromParse     = Integer.parseInt("42");
        Integer fromValue = Integer.valueOf("42");
        System.out.println("parseInt: " + fromParse + ", valueOf: " + fromValue);

        long parsedLong     = Long.parseLong("9999999999");
        double parsedDouble = Double.parseDouble("3.14");
        float parsedFloat   = Float.parseFloat("3.14");
        byte parsedByte     = Byte.parseByte("127");
        short parsedShort   = Short.parseShort("32767");
        System.out.println("parseLong: " + parsedLong);
        System.out.println("parseDouble: " + parsedDouble);
        System.out.println("parseFloat: " + parsedFloat);
        System.out.println("parseByte: " + parsedByte);
        System.out.println("parseShort: " + parsedShort);

        // пробелы — нужен trim()
        int trimmed = Integer.parseInt("  42  ".trim());
        System.out.println("с trim: " + trimmed);

        // "abc" -> NumberFormatException
        try {
            Integer.parseInt("abc");
        } catch (NumberFormatException nfe) {
            System.out.println("NFE: 'abc' не является числом");
        }

        // "3.14" -> NumberFormatException в parseInt (нужен parseDouble)
        try {
            Integer.parseInt("3.14");
        } catch (NumberFormatException nfe) {
            System.out.println("NFE: '3.14' не является int, используй parseDouble");
        }

        // BigDecimal из строки — точный способ для дробных
        BigDecimal precise = new BigDecimal("0.1").add(new BigDecimal("0.2"));
        System.out.println("BigDecimal из строк: " + precise); // 0.3
    }

    // Как парсить hex/binary/octal строку в int и конвертировать обратно?
    static void numberSystems() {
        int hex    = Integer.parseInt("FF", 16);    // 255
        int binary = Integer.parseInt("1010", 2);   // 10
        int octal  = Integer.parseInt("17", 8);     // 15
        System.out.println("hex 'FF' -> " + hex);
        System.out.println("binary '1010' -> " + binary);
        System.out.println("octal '17' -> " + octal);

        System.out.println("255 -> hex: " + Integer.toHexString(255));
        System.out.println("10 -> binary: " + Integer.toBinaryString(10));
        System.out.println("15 -> octal: " + Integer.toOctalString(15));

        // Long вмещает base-32 строку до 12 символов (2^60 < Long.MAX_VALUE = 2^63)
        long result = Long.parseLong("PUPKISVISTKI", 32);
        System.out.println(result); // 895_866_606_700_506

        // Или через BigInteger — без ограничений на размер
        BigInteger big = new BigInteger("PUPKISVISTKI", 32);
        System.out.println(big);
    }
}