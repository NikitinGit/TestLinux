package com.example.testlinux.java.core.patterns.prototype;

public class PrototypeMain {

    public static void main(String[] args) {
        demoDirectClone();
        System.out.println("---");
        demoRegistry();
    }

    /**
     * Прямое клонирование: есть готовый объект, нужен его дубликат.
     * Клиент работает только с интерфейсом Shape — не знает конкретного типа.
     */
    private static void demoDirectClone() {
        Shape original = new Circle(10, "red");
        Shape copy = original.clone();          // клиент не знает, что это Circle

        original.draw();    // Circle r=10, color=red
        copy.draw();        // Circle r=10, color=red
        System.out.println("same object? " + (original == copy));   // false
    }

    /**
     * Реестр прототипов: заранее зарегистрировали "эталоны",
     * клиент получает копии по ключу — без if/switch и без new ConcreteClass().
     */
    private static void demoRegistry() {
        ShapeRegistry registry = new ShapeRegistry();
        registry.register("red-circle", new Circle(5, "red"));
        registry.register("blue-rect",  new Rectangle(4, 8, "blue"));

        Shape s1 = registry.get("red-circle");
        Shape s2 = registry.get("red-circle");  // отдельная копия!
        Shape s3 = registry.get("blue-rect");

        s1.draw();    // Circle r=5, color=red
        s2.draw();    // Circle r=5, color=red
        s3.draw();    // Rectangle 4x8, color=blue
        System.out.println("s1 == s2? " + (s1 == s2));   // false — две независимые копии
    }
}