package com.example.testlinux.interfaces;

import lombok.Data;
import lombok.Getter;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

public class Lambda {
    public static void main(String[] args) throws IOException {
        Integer iOb3 = 127;
        Integer iOb4 = 127;
        System.out.println(iOb3 == iOb4);
        staticFunction();
        objectFunction();
        anonymityExtends();
        lambdaExample();
        anonymityInterface();
        anonymityExtendsInterface();
    }

    private static void lambdaExample() throws IOException {
        int bn = 0;
        MyFunctionalInterface lambda = () -> {
            final int n = 5;
            System.out.println("lambda; final n: " + n + ", bn; " + bn);
        };

        lambda.execute();

        lambda.defaultN1();
        lambda.defaultN2();//        lambda.staticN1(); - не робит  lambda.staticN2(); - не робит

        MyFunctionalInterface.staticN1();
        MyFunctionalInterface.staticN2();

        lambda = () -> System.out.println("min");
        lambda.execute();
    }

    private static void anonymityExtendsInterface() throws IOException {
        MyFunction myDog = new Dog() {
            int fieldLocal;
            @Override
            public void execute() {
                fieldLocal++;
                System.out.println("anonymityExtendsInterface() fieldLocal; " + fieldLocal);
            }
        };

        myDog.execute();
    }

    private static void anonymityInterface() throws IOException {
        MyFunction myFunc3 = new MyFunction() {
            @Override
            public void execute() {
                System.out.println("MyFunction Interface myFunc3.execute()");
            }
        };

        myFunc3.execute();
    }

    private static int fieldStatic = 5;
    public static void setFieldStatic(int field) {
        fieldStatic = field;
    }

    public static int getFieldStatic() {
        return fieldStatic;
    }

    public int fieldObject;
    public int getFieldObject() {
        fieldObject += 7;
        return fieldObject;
    }
    private static void anonymityExtends() throws IOException {
        int bin = 5;
        AtomicInteger atomicInteger = new AtomicInteger();
        Lambda lambda = new Lambda();

        Animal dog = new Dog() {
            private int fieldIsMissing;

            public void setField(int field) {
                this.fieldIsMissing = field;
                fieldStatic++;
            }

            @Override
            public void execute() {
                fieldStatic++;
                setFieldStatic(125);
                lambda.fieldObject = 125;
                lambda.fieldObject += 100;

                fieldIsMissing = atomicInteger.incrementAndGet();

                System.out.println("anonymityExtends() fieldStatic; " + fieldStatic +
                        ", getFieldStatic(); " + getFieldStatic() + ", fieldIsMissing; " + fieldIsMissing);
                System.out.println("anonymityExtends() lambda.fieldObject; " + lambda.fieldObject);
            }
        };



        dog.execute();
        dog.setField(25);

        MyFunctionalInterface dog2 = () -> {
            fieldStatic++;

            atomicInteger.incrementAndGet();

            System.out.println("MyFunctionalInterface fieldStatic; " + fieldStatic + ", bin; " + bin);
            lambda.fieldObject = 75;
            lambda.fieldObject += 100;
            System.out.println("MyFunctionalInterface lambda.fieldObject; " + lambda.fieldObject);
        };

        dog2.execute();

        MyFunctionalInterface objMethod = lambda::getFieldObject;
        lambda.fieldObject = 125;
        objMethod.execute();

        System.out.println("atomicInteger(); " + atomicInteger.get() + ", objMethod lambda.fieldObject; " + lambda.fieldObject);
    }

    public static void staticFunction() throws IOException {
        MyFunctionalInterface functionalInterface = StaticFunctions::sampleN1;
        functionalInterface.execute();
    }

    public static void objectFunction() throws IOException {
        MyFunctionalInterface functionalInterface = new StaticFunctions()::sampleN2;
        functionalInterface.execute();
    }
}
