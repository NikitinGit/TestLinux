package com.example.testlinux.java.core.hashcodetest;

import java.util.HashMap;
import java.util.Objects;

/**
 * Что такое коллизия и как HashMap её разруливает.
 *
 * Коллизия = два разных ключа (по equals) попали в один бакет.
 * Причины:
 *   - одинаковый hashCode (плохой hash)            -> "хеш-коллизия"
 *   - разный hashCode, но одинаковый бакет         -> "коллизия индексов"
 *     (например h=1 и h=17 при capacity=16 оба -> бакет 1)
 *
 * Что делает HashMap при коллизии (Java 8+):
 *   - Сначала в бакете лежит односвязный список Node.
 *   - При вставке проходит по списку:
 *       если для какого-то узла  hash совпадает И equals == true  -> ПЕРЕЗАПИСЬ value.
 *       иначе                                                     -> добавляет в конец.
 *   - Если в одном бакете накопилось >= TREEIFY_THRESHOLD (8) узлов
 *     И table.length >= MIN_TREEIFY_CAPACITY (64), бакет ПЕРЕСТРАИВАЕТСЯ в красно-чёрное дерево.
 *     В дереве поиск O(log n) вместо O(n) — защита от DoS-атаки через подбор хешей.
 *   - При уменьшении ниже UNTREEIFY_THRESHOLD (6) дерево обратно становится списком.
 *
 * Поиск (get):
 *   - вычислили бакет
 *   - идём по списку/дереву, ищем узел, у которого hash совпадает И equals == true
 *   - если такого нет -> ключа в карте нет
 */
public class _02_CollisionsAndBuckets {

    /** Ключ с принудительным хешем — удобно симулировать коллизии. */
    static final class FixedHashKey {
        final String id;
        final int hash;
        FixedHashKey(String id, int hash) { this.id = id; this.hash = hash; }
        @Override public int hashCode() { return hash; }
        @Override public boolean equals(Object o) {
            return o instanceof FixedHashKey k && Objects.equals(id, k.id);
        }
        @Override public String toString() { return id; }
    }

    public static void main(String[] args) {
        // 1) Хеш-коллизия: 10 ключей с одинаковым хешем
        HashMap<FixedHashKey, Integer> map = new HashMap<>();
        for (int i = 0; i < 10; i++) {
            map.put(new FixedHashKey("k" + i, 777), i);
        }
        System.out.println("size = " + map.size());                          // 10
        System.out.println("все в одном бакете, но equals различает ключи");

        // 2) Коллизия индекса при РАЗНЫХ хешах:
        // capacity=16, mask=0xF. Хеши 1 и 17 (=16+1) дают один и тот же бакет.
        int mask = 16 - 1;
        int h1 = 1, h2 = 17;
        int sp1 = h1 ^ (h1 >>> 16);
        int sp2 = h2 ^ (h2 >>> 16);
        System.out.println("\nbucket(h=" + h1 + ") = " + (sp1 & mask));      // 1
        System.out.println("bucket(h=" + h2 + ") = " + (sp2 & mask));        // 1 — тот же бакет

        // То же самое — но на настоящем HashMap. Два РАЗНЫХ ключа с РАЗНЫМИ хешами,
        // но из-за формулы (hash & 15) оба попадают в бакет №1.
        HashMap<FixedHashKey, String> indexCollisionMap = new HashMap<>(16);
        FixedHashKey k1  = new FixedHashKey("alpha", 1);
        FixedHashKey k17 = new FixedHashKey("beta",  17);
        indexCollisionMap.put(k1,  "value-for-hash-1");
        indexCollisionMap.put(k17, "value-for-hash-17");

        System.out.println("\n=== Коллизия индекса в реальном HashMap ===");
        System.out.println("k1.hashCode()  = " + k1.hashCode()
                + ", bucket = " + ((k1.hashCode() ^ (k1.hashCode() >>> 16)) & mask));
        System.out.println("k17.hashCode() = " + k17.hashCode()
                + ", bucket = " + ((k17.hashCode() ^ (k17.hashCode() >>> 16)) & mask));
        System.out.println("size = " + indexCollisionMap.size());                   // 2 — оба лежат
        System.out.println("get(k1)  = " + indexCollisionMap.get(k1));              // value-for-hash-1
        System.out.println("get(k17) = " + indexCollisionMap.get(k17));             // value-for-hash-17
        // hashCode у них разный -> hash-проверка отсекает чужой узел в одном бакете,
        // даже до equals. equals срабатывает уже при совпадении хеша.

        // Контрольная проверка: чужой ключ из того же бакета (другой id, другой hash) -> null
        System.out.println("get(новый, h=33) = "
                + indexCollisionMap.get(new FixedHashKey("gamma", 33)));            // null

        // 3) Симулируем поведение бакета как односвязного списка
        SimplifiedBucket bucket = new SimplifiedBucket();
        bucket.put(new FixedHashKey("a", 777), "A");
        bucket.put(new FixedHashKey("b", 777), "B");
        bucket.put(new FixedHashKey("c", 777), "C");
        // тот же по equals ключ -> перезапись
        bucket.put(new FixedHashKey("b", 777), "B2");
        System.out.println("\nsimplified bucket dump:");
        bucket.dump();
        System.out.println("get a = " + bucket.get(new FixedHashKey("a", 777)));
        System.out.println("get b = " + bucket.get(new FixedHashKey("b", 777)));  // B2
        System.out.println("get x = " + bucket.get(new FixedHashKey("x", 777)));  // null

        // 4) Попытка достать несуществующий ключ -> null. Распаковка в int дала бы NPE,
        // поэтому возвращаем Integer и сравниваем с null отдельно.
        Integer missing = map.get(new FixedHashKey("bnbnb", 755));
        System.out.println("missing key -> " + missing);                         // null
    }

    /** Минимальная имитация одного бакета HashMap (без деревьев и ресайза). */
    static final class SimplifiedBucket {
        Node head;
        static final class Node {
            final int hash;
            final Object key;
            Object value;
            Node next;
            Node(int h, Object k, Object v) { hash = h; key = k; value = v; }
        }

        void put(Object key, Object value) {
            int h = key.hashCode();
            for (Node n = head; n != null; n = n.next) {
                if (n.hash == h && (n.key == key || n.key.equals(key))) {   // <- equals здесь!
                    n.value = value;                                        // перезапись
                    return;
                }
            }
            // не нашли — добавляем в начало
            Node fresh = new Node(h, key, value);
            fresh.next = head;
            head = fresh;
        }

        Object get(Object key) {
            int h = key.hashCode();
            for (Node n = head; n != null; n = n.next) {
                if (n.hash == h && (n.key == key || n.key.equals(key))) {
                    return n.value;
                }
            }
            return null;
        }

        void dump() {
            for (Node n = head; n != null; n = n.next) {
                System.out.println("  " + n.key + " -> " + n.value + " (hash=" + n.hash + ")");
            }
        }
    }
}
