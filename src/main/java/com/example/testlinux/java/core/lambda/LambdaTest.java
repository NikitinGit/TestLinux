package com.example.testlinux.java.core.lambda;

import java.util.function.*;

public class LambdaTest {

    static int b = 2;
    int c = 3;
    public static void main (String[] test) {
        functionInterfaces();

        Operationable op = Integer::sum;
        int a = op.getIntNumber(3, 5);
        System.out.println("a; " + a);

        op = Integer::divideUnsigned;
        int test2 = op.getIntNumber(15, 2);
        System.out.println("test2; " + test2);

        op = (b, c) -> b * c;
        int test1 = op.getIntNumber(3, 5);
        System.out.println("test1; " + test1);

        //AbstractClassMethod acm = LambdaTest::testAbstract; - не работает
        op = StaticMethod::test1;
        System.out.println("op = StaticMethod::test1; op.getIntNumber(3, 5); " + op.getIntNumber(3, 5));

        StaticMethod sm = new StaticMethod();
        op = sm::test2;
        System.out.println("op = sm::test2; op.getIntNumber(3, 5); " + op.getIntNumber(3, 5));

        GenericInterface<String> gin1 = (b, c) -> b + c;
        System.out.println("GenericInterface<String> gin1; op.getIntNumber(3, 5); " + gin1.test("3bign", "5"));
        GenericInterface<Double> gin2 = (b, c) -> b - c;
        System.out.println("GenericInterface<String> gin2; op.getIntNumber(3, 5); " + gin2.test(28.75d, 0.5d));

        GenericInterface<StaticMethod> gin3 = (b, c) -> b;
        GenericInterface<StaticMethod> gin4 = (b, c) -> new StaticMethod();

        ConstructorInterface ct = ConstructorTest::new;
        ct.test(125);

        LambdaTest lt = new LambdaTest();
        op = (x, y) -> {
            b++;
            b += ++lt.c;
            x += b;
            y *= x;

            return y;
        };

        System.out.println("op.getIntNumber(5, 2); " + op.getIntNumber(5, 2));

    }

    static void functionInterfaces() {
        Predicate<Integer> testInt = x -> x % 3 == 1;
        boolean test2 = testInt.test(2);
        System.out.println("test2; " + test2);

        boolean test4 = testInt.test(4);
        System.out.println("test4: " + test4);

        BinaryOperator<Integer> multiply = (x, y) -> x*y;
        System.out.println("multiply.apply(3, 5)); " + multiply.apply(3, 5)); // 15

        UnaryOperator<Integer> square = x -> x*x;
        System.out.println("square.apply(5); " + square.apply(5)); // 25

        Function1 f1 = new Function1("new Function1");

        Function<Function1, Function2> f1ToF2 = x -> new Function2(x.getName() + " from lambda nikitin");
        var f2 = f1ToF2.apply(f1);
        System.out.println("var f2 = f1ToF2.apply(f1) , f2.getName(); " + f2.getName());

        f1ToF2 = Function1::getF2;// or f1ToF2 = x -> x.getF2();
        System.out.println("f1ToF2.apply(f1).getName(); " + f1ToF2.apply(f1).getName());

        BiFunction<Integer, Function1, Function2> intWithF1ToF2 = (x, y) ->
                new Function2(y.getName() + ": " + (x * 2));
        var f3 = intWithF1ToF2.apply(25, f1);
        System.out.println("BiFunction f3; " + f3.getName());

        Consumer<Function2> consumerF2 = x -> x.setName("consumerF2");
        consumerF2.accept(f2);
        System.out.println("f2.getName; " + f2.getName());

    }

    public ConstructorTest testConstruct(int i) {
        i += 25;
        return new ConstructorTest(i);
    }

    public static int testAbstract(int x, int y) {
        return x + y;
    }

    static class StaticMethod {

        static int test1(int x, int y) {
            return x + 2 * y;
        }

        int test2(int x, int y) {
            return x * 2 + y * 2;
        }
    }
}

