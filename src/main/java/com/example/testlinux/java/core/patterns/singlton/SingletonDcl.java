package com.example.testlinux.java.core.patterns.singlton;

/**
 * 1) Double-Checked Locking (DCL) — ленивый, потокобезопасный.
 * <p>
 * volatile ОБЯЗАТЕЛЕН: запрещает переупорядочивание при публикации (safe publication).
 * Без него другой поток может на 1-й проверке увидеть НЕнулевую ссылку на ещё
 * не достроенный объект (`new` = выделить память → конструктор → присвоить ссылку,
 * и шаги 2–3 могут переставиться).
 * <p>
 * Минусы: многословно, легко ошибиться (забыл volatile → сломано). Сейчас чаще
 * предпочитают holder-идиому или enum.
 */
public class SingletonDcl {

    private static volatile SingletonDcl instance;   // ← volatile ОБЯЗАТЕЛЕН

    private SingletonDcl() {
        System.out.println("SingletonDcl: конструктор (создан)");
    }

    public static SingletonDcl get() {
        if (instance == null) {                       // 1-я проверка — БЕЗ блокировки (быстрый путь)
            synchronized (SingletonDcl.class) {
                if (instance == null) {               // 2-я проверка — ПОД блокировкой
                    instance = new SingletonDcl();    // создаём ровно один раз
                }
            }
        }
        return instance;
    }
}
