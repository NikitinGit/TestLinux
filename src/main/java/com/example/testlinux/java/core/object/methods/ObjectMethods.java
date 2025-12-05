package com.example.testlinux.java.core.object.methods;

public class ObjectMethods implements Cloneable {

    Integer number;
    final String name;

    public ObjectMethods() {
        name = "isEmpty";
    }

    @Override
    public String toString(){
        return name + "; " + number;
    }
    public ObjectMethods getObject() {
        System.out.println("ObjectMethods getObject(); " + this);
        return this;
    }
}
