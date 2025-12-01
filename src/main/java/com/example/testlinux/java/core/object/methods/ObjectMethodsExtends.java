package com.example.testlinux.java.core.object.methods;

public class ObjectMethodsExtends extends ObjectMethods {

    private String name;

    public ObjectMethodsExtends() {
        name = "isEmptyExtended";
        System.out.println("ObjectMethodsExtends name; " + name);
    }
    @Override
    public Object clone() {
        return this;
    }
    public ObjectMethods getParentObject() {
        return super.getObject();
    }
}
