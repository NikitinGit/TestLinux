package com.example.testlinux.java.core.hashcodetest;

import java.util.Objects;

/**
 * Один путь put(key, value) от начала до конца — на собственной мини-карте.
 *
 * Эта карта повторяет логику HashMap из JDK по шагам:
 *   1. Если table ещё не создан -> создать (capacity = 16).
 *   2. Посчитать h = spread(key.hashCode()).
 *   3. i = h & (table.length - 1) — индекс бакета.
 *   4. Если table[i] == null  -> просто положить новый узел.
 *      Иначе пройти по цепочке:
 *        - если нашли узел с тем же hash И equals == true -> ПЕРЕЗАПИСЬ value, вернуть старое;
 *        - если дошли до конца -> добавить в конец, увеличить size.
 *   5. Если size > threshold -> resize: новый массив в 2 раза больше, перераскладываем элементы.
 *
 * Деревья здесь опущены — это уже оптимизация поверх той же логики.
 */
public class _07_StepByStepPut {

    public static void main(String[] args) {
        MiniMap<String, Integer> map = new MiniMap<>();

        // Несколько обычных вставок
        map.put("a", 1);
        map.put("b", 2);
        map.put("c", 3);

        // Перезапись по существующему ключу
        Integer old = map.put("a", 100);
        System.out.println("старое значение для 'a' = " + old);          // 1

        // get / containsKey
        System.out.println("get('a')        = " + map.get("a"));         // 100
        System.out.println("get('missing')  = " + map.get("missing"));   // null
        System.out.println("size            = " + map.size);             // 3

        // Спровоцируем resize: capacity по умолчанию 16, threshold = 12.
        for (int i = 0; i < 14; i++) map.put("x" + i, i);
        System.out.println("после массовой вставки: size=" + map.size + ", capacity=" + map.table.length);
    }

    /** Мини-HashMap. Только для учебных целей: без деревьев и без fail-fast iterator. */
    static final class MiniMap<K, V> {

        static final int DEFAULT_CAPACITY = 16;
        static final float LOAD_FACTOR    = 0.75f;

        Node<K, V>[] table;
        int size;
        int threshold;

        @SuppressWarnings("unchecked")
        V put(K key, V value) {
            if (table == null) {
                table = (Node<K, V>[]) new Node[DEFAULT_CAPACITY];
                threshold = (int) (DEFAULT_CAPACITY * LOAD_FACTOR);
            }
            int h = spread(Objects.hashCode(key));
            int i = h & (table.length - 1);

            // Идём по цепочке в бакете
            for (Node<K, V> n = table[i]; n != null; n = n.next) {
                if (n.hash == h && Objects.equals(n.key, key)) {
                    V oldVal = n.value;
                    n.value = value;                 // ПЕРЕЗАПИСЬ
                    return oldVal;
                }
            }

            // Не нашли — добавляем в начало бакета
            Node<K, V> fresh = new Node<>(h, key, value, table[i]);
            table[i] = fresh;
            size++;

            if (size > threshold) resize();
            return null;
        }

        V get(K key) {
            if (table == null) return null;
            int h = spread(Objects.hashCode(key));
            int i = h & (table.length - 1);
            for (Node<K, V> n = table[i]; n != null; n = n.next) {
                if (n.hash == h && Objects.equals(n.key, key)) return n.value;
            }
            return null;
        }

        /** В JDK называется "spread" — размешивает старшие биты в младшие. */
        static int spread(int h) {
            return h ^ (h >>> 16);
        }

        @SuppressWarnings("unchecked")
        private void resize() {
            Node<K, V>[] oldTable = table;
            int newCap = oldTable.length * 2;
            Node<K, V>[] newTable = (Node<K, V>[]) new Node[newCap];
            int mask = newCap - 1;
            for (Node<K, V> head : oldTable) {
                for (Node<K, V> n = head; n != null; ) {
                    Node<K, V> next = n.next;
                    int i = n.hash & mask;
                    n.next = newTable[i];
                    newTable[i] = n;
                    n = next;
                }
            }
            table = newTable;
            threshold = (int) (newCap * LOAD_FACTOR);
            System.out.println("  >> resize -> capacity=" + newCap + ", threshold=" + threshold);
        }

        static final class Node<K, V> {
            final int hash;
            final K key;
            V value;
            Node<K, V> next;
            Node(int hash, K key, V value, Node<K, V> next) {
                this.hash = hash; this.key = key; this.value = value; this.next = next;
            }
        }
    }
}
