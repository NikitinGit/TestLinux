package com.example.testlinux.interfaces;


public class Animal implements MyFunction{

    private int field;

    public void setField(int field) {
        this.field = field;
    }

    public int getField() {
        return field;
    }

    @Override
    public void execute(){
        System.out.println("Animal execute");
    }
}
