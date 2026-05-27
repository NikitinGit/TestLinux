package com.example.testlinux.java.core.patterns.prototype;

public class Circle implements Shape {

    private int radius;
    private String color;

    public Circle(int radius, String color) {
        this.radius = radius;
        this.color = color;
    }

    // конструктор копирования — основа Prototype в Java
    private Circle(Circle source) {
        this.radius = source.radius;
        this.color = source.color;
    }

    @Override
    public Shape clone() {
        return new Circle(this);   // делегируем конструктору копирования
    }

    @Override
    public void draw() {
        System.out.println("Circle r=" + radius + ", color=" + color);
    }
}