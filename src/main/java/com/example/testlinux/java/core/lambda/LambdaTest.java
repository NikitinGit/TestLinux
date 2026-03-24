package com.example.testlinux.java.core.lambda;

public class LambdaTest {

    public static void main (String[] test) {
        Operationable op = Integer::sum;
        int a = op.sum(3, 5);
        System.out.println("a; " + a);

        op = (b, c) -> b * c;
        int test1 = op.sum(3, 5);
        System.out.println("test1; " + test1);
    }
}
