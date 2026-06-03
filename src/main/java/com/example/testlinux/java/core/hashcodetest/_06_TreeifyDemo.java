package com.example.testlinux.java.core.hashcodetest;

import java.util.HashMap;

/**
 * Демонстрация перехода бакета в красно-чёрное дерево (treeify, Java 8+).
 *
 * Пороги в HashMap:
 *   TREEIFY_THRESHOLD       = 8     // в бакете >= 8 узлов -> попытка превратить в дерево
 *   MIN_TREEIFY_CAPACITY    = 64    // НО только если table.length >= 64
 *   UNTREEIFY_THRESHOLD     = 6     // при сжатии < 6 -> обратно в список
 *
 * Если capacity < 64, вместо treeify HashMap делает resize (увеличивает массив, надеясь, что
 * после перераспределения коллизий станет меньше). Поэтому для триггера дерева нужно:
 *   - заранее задать capacity >= 64
 *   - заколотить ВСЕ ключи в один бакет (одинаковый hash)
 *   - положить хотя бы 8 штук
 *   - желательно, чтобы ключи реализовывали Comparable — иначе TreeNode хуже балансируется
 *
 * Заглянуть напрямую в table HashMap из JDK 17 без --add-opens нельзя.
 * Поэтому показываем ВРЕМЕННУЮ разницу: при одинаковых ключах,
 * но РАЗНЫХ свойствах get() работает быстрее в дереве, чем в линейном списке.
 *
 * Если хочешь увидеть TreeNode "глазами" — раскомментируй reflection-блок и запусти с
 *     --add-opens java.base/java.util=ALL-UNNAMED
 */
public class _06_TreeifyDemo {

    /** Все ключи попадут в один бакет — одинаковый hashCode. Реализуем Comparable. */
    static final class CollidingKey implements Comparable<CollidingKey> {
        final int id;
        CollidingKey(int id) { this.id = id; }
        @Override public int hashCode() { return 42; }   // принудительная коллизия
        @Override public boolean equals(Object o) {
            return o instanceof CollidingKey k && id == k.id;
        }
        @Override public int compareTo(CollidingKey o) { return Integer.compare(id, o.id); }
        @Override public String toString() { return "k" + id; }
    }

    /** То же, но БЕЗ Comparable — дерево будет, но менее эффективно. */
    static final class NotComparableKey {
        final int id;
        NotComparableKey(int id) { this.id = id; }
        @Override public int hashCode() { return 42; }
        @Override public boolean equals(Object o) {
            return o instanceof NotComparableKey k && id == k.id;
        }
    }

    public static void main(String[] args) {
        int n = 5000;

        // 1) Все ключи в один бакет. capacity >= 64 -> бакет превратится в дерево.
        HashMap<CollidingKey, Integer> collidingMap = new HashMap<>(64);
        for (int i = 0; i < n; i++) collidingMap.put(new CollidingKey(i), i);

        long t1 = System.nanoTime();
        for (int i = 0; i < n; i++) collidingMap.get(new CollidingKey(i));
        long colliding = System.nanoTime() - t1;

        // 2) Хороший хеш — каждый ключ в свой бакет, список длиной 1 в каждом бакете.
        HashMap<Integer, Integer> goodMap = new HashMap<>(8192);
        for (int i = 0; i < n; i++) goodMap.put(i, i);

        long t2 = System.nanoTime();
        for (int i = 0; i < n; i++) goodMap.get(i);
        long good = System.nanoTime() - t2;

        // 3) Та же коллизия, но без Comparable — дерево всё равно строится, но медленнее.
        HashMap<NotComparableKey, Integer> noCmpMap = new HashMap<>(64);
        for (int i = 0; i < n; i++) noCmpMap.put(new NotComparableKey(i), i);

        long t3 = System.nanoTime();
        for (int i = 0; i < n; i++) noCmpMap.get(new NotComparableKey(i));
        long noCmp = System.nanoTime() - t3;

        System.out.println("=== get() x " + n + ", ns ===");
        System.out.printf("обычный (хорошие хеши)        : %12d%n", good);
        System.out.printf("колизии + Comparable (дерево) : %12d%n", colliding);
        System.out.printf("колизии без Comparable        : %12d%n", noCmp);

        System.out.println("\nТеоретическая стоимость поиска:");
        System.out.println("  идеальные хеши -> O(1)");
        System.out.println("  список в бакете -> O(n) [до treeify]");
        System.out.println("  дерево в бакете -> O(log n) [после treeify, если capacity >= 64]");

        // Подсказка про reflection:
        System.out.println("\nЧтобы заглянуть в table[bucket] и увидеть TreeNode напрямую,");
        System.out.println("запусти этот класс с JVM-флагом:");
        System.out.println("  --add-opens java.base/java.util=ALL-UNNAMED");
        System.out.println("Тогда reflection в JDK 17 пропустит обращение к приватному полю HashMap.table.");
    }

    /** Вспомогалка для случая, когда захочешь распечатать содержимое полей HashMap. */
    @SuppressWarnings("unused")
    private static String describe(HashMap<?, ?> map, int hash) {
        try {
            var f = HashMap.class.getDeclaredField("table");
            f.setAccessible(true);
            Object[] t = (Object[]) f.get(map);
            if (t == null) return "(no table)";
            int idx = (hash ^ (hash >>> 16)) & (t.length - 1);
            Object n = t[idx];
            return n == null ? "(empty)" : n.getClass().getSimpleName();
        } catch (Exception e) {
            return "reflection недоступен: " + e.getMessage();
        }
    }
}
