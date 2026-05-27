package com.example.testlinux.java.core.patterns.prototype;

public class Rectangle implements Shape {

    private int width;
    private int height;
    private String color;

    public Rectangle(int width, int height, String color) {
        this.width = width;
        this.height = height;
        this.color = color;
    }

    private Rectangle(Rectangle source) {
        this.width = source.width;
        this.height = source.height;
        this.color = source.color;
    }

    @Override
    public Shape clone() {
        return new Rectangle(this);
    }

    @Override
    public void draw() {
        System.out.println("Rectangle " + width + "x" + height + ", color=" + color);
    }
}