package com.example.testlinux.java.core.patterns.prototype;

/**
 * Прототип — порождающий паттерн.
 * Позволяет копировать объекты, не зная их конкретного класса.
 *
 * Клиент получает Shape (интерфейс) и вызывает clone() — возвращается
 * новый объект того же типа, что и оригинал, с тем же состоянием.
 */
public interface Shape {
    Shape clone();          // фабричный метод копирования (не java.lang.Object#clone!)
    void draw();
}