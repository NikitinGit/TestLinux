package com.example.testlinux.java.core.generic;

public class  TestExtendsMethodTypes{
    public <T extends ParentClass> T someType(boolean returnParent) {
        if (returnParent) {
            return (T) new ParentClass(); // Приведение типов, но это небезопасно
        } else {
            return (T) new ChildClass(); // Приведение типов, но это небезопасно
        }
    }
}
