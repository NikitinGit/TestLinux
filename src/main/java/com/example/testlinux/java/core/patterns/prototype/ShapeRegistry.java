package com.example.testlinux.java.core.patterns.prototype;

import java.util.HashMap;
import java.util.Map;

/**
 * Реестр прототипов — типичное расширение паттерна.
 *
 * Хранит "эталонные" заготовки. Клиенту достаточно сказать "дай мне red-circle" —
 * реестр вернёт КОПИЮ заготовки, оригинал остаётся в реестре нетронутым.
 *
 * Так можно избавиться от if/switch при создании объектов — реестр сам знает,
 * какой прототип клонировать по ключу.
 */
public class ShapeRegistry {

    private final Map<String, Shape> prototypes = new HashMap<>();

    public void register(String key, Shape prototype) {
        prototypes.put(key, prototype);
    }

    public Shape get(String key) {
        Shape prototype = prototypes.get(key);
        if (prototype == null) {
            throw new IllegalArgumentException("Unknown prototype: " + key);
        }
        return prototype.clone();   // отдаём копию, эталон не трогаем
    }
}