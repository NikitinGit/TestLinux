package com.example.testlinux.interfaces;

import java.io.IOException;

@FunctionalInterface
interface MyFunction {
    void execute() throws IOException; // Метод объявляет проверяемое исключение
}
