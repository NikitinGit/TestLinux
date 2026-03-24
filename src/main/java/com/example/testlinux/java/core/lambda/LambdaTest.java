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
    }
}
