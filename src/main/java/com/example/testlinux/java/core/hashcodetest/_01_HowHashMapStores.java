package com.example.testlinux.java.core.hashcodetest;

import java.util.HashMap;
import java.util.Objects;

/**
 * Как HashMap решает, КУДА положить элемент.
 *
 * Внутри HashMap — массив (table) бакетов. Размер всегда степень двойки (по умолчанию 16).
 * Алгоритм добавления put(key, value):
 *
 *   1) int h  = key.hashCode();          // прикладной хеш, любой int
 *   2) int hh = h ^ (h >>> 16);          // "spread" — размешиваем верхние биты к нижним
 *   3) int i  = hh & (table.length - 1); // индекс бакета: дешёвая замена hh % table.length,
 *                                        // работает только когда length — степень двойки
 *
 * А equals использует только когда дошёл до конкретного бакета,
 * чтобы отличить "тот самый ключ" от "ключа с тем же хешем".
 *
 * ВАЖНО: hashCode НЕ обязан быть уникальным. Он лишь говорит,
 * "в каком бакете искать". Равенство решает equals.
 */
public class _01_HowHashMapStores {

    static final class Key {
        final String name;
        final int    forcedHash;

        Key(String name, int forcedHash) {
            this.name = name;
            this.forcedHash = forcedHash;
        }

        @Override public int hashCode() { return forcedHash; }   // принудительный хеш для демонстрации
        @Override public boolean equals(Object o) {
            return o instanceof Key k && Objects.equals(name, k.name);
        }
        @Override public String toString() { return name + "(h=" + forcedHash + ")"; }
    }

    public static void main(String[] args) {
        // capacity = 16 -> бакетов 16, mask = 15 (двоичная 0000_1111)
        int capacity = 16;
        int mask = capacity - 1;

        // Покажем, как hashCode превращается в индекс бакета
        int[] sampleHashes = {0, 1, 16, 17, 32, -1, Integer.MAX_VALUE, Integer.MIN_VALUE};
        System.out.println("=== hashCode -> bucket index (capacity=" + capacity + ") ===");
        for (int h : sampleHashes) {
            int spread = h ^ (h >>> 16);          // тот же шаг, что внутри HashMap
            int bucket = spread & mask;
            System.out.printf("h=%-12d spread=%-12d bucket=%d%n", h, spread, bucket);
        }

        // Два разных ключа с одинаковым hashCode -> один и тот же бакет, но НЕ один и тот же entry
        Key a = new Key("A", 42);
        Key b = new Key("B", 42);    // намеренная коллизия по хешу

        HashMap<Key, String> map = new HashMap<>();
        map.put(a, "first");
        map.put(b, "second");

        System.out.println("\n=== Коллизия по hashCode, но разные ключи ===");
        System.out.println("size = " + map.size());          // 2 — equals различает их
        System.out.println("get(A) = " + map.get(a));        // first
        System.out.println("get(B) = " + map.get(b));        // second

        // Дубликат по equals -> перезапись значения, размер не растёт
        Key aAgain = new Key("A", 42);
        map.put(aAgain, "overwritten");
        System.out.println("\n=== Тот же по equals ключ -> перезапись ===");
        System.out.println("size = " + map.size());          // всё ещё 2
        System.out.println("get(A) = " + map.get(a));        // overwritten
    }
}
