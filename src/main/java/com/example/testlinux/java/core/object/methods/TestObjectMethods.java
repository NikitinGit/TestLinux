package com.example.testlinux.java.core.object.methods;

public class TestObjectMethods {

    public static void main(String[] args) {
        var childObj1 = new ObjectMethodsExtends();
        var parentObj1 = childObj1.getParentObject();

        System.out.println("MAIN 1 (parentObj1 instanceof ObjectMethodsExtends); " + (parentObj1 instanceof ObjectMethodsExtends)
        + " childObj1.equals(parentObj1); " + childObj1.equals(parentObj1));

        var childObj2 = new ObjectMethodsExtends();
        var parentObj2 = childObj2.getParentObject();
        var parentObj3 = new ObjectMethods();

        System.out.println("MAIN 2 (parentObj3 instanceof ObjectMethodsExtends); " + (parentObj1 instanceof ObjectMethodsExtends)
                + " childObj1.equals(parentObj1); " + childObj1.equals(parentObj2));

        var original = new ObjectMethods();
        var clone = original.clone();
        System.out.println("MAIN original.equals(clone); " + original.equals(clone));
    }

}
