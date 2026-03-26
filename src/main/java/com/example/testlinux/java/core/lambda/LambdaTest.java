package com.example.testlinux.java.core.lambda;

public class LambdaTest {

    public static void main (String[] test) {
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

