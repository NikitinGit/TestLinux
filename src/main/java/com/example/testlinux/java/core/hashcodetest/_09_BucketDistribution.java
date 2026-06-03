package com.example.testlinux.java.core.hashcodetest;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;

/**
 * Реальное распределение элементов по бакетам в HashMap.
 *
 * Главные мысли:
 *   - "1 элемент в бакете" недостижимо: hash имеет 2^32 значений, бакетов 16/32/64 — сжатие
 *     огромное, коллизии статистически неизбежны (парадокс дней рождения).
 *   - HashMap спроектирован под коллизии: список → дерево после treeify-порога.
 *   - "Максимальная производительность" = амортизированный O(1), и она у тебя уже есть,
 *     даже когда в бакетах по 2-3 элемента.
 *
 * Сценарии в этом классе:
 *   1) идеальный hash (Integer), capacity=16, size=12       — пара коллизий есть
 *   2) тот же hash, capacity=128 для тех же 12 элементов    — почти все в разных бакетах
 *   3) сломанный hash (всё в один бакет)                    — деградация
 *   4) парадокс дней рождения для разных N
 *
 * Запускать с JVM-флагом:
 *   --add-opens java.base/java.util=ALL-UNNAMED
 */
public class _09_BucketDistribution {

    public static void main(String[] args) throws Exception {
        boolean a = "Aa".hashCode() == "BB".hashCode();       // оба = 2112
        boolean b = "FB".hashCode() == "Ea".hashCode();       // оба = 2236
        boolean c = "BB".hashCode() == "Aa".hashCode();       // повтор
        System.out.println("Aa".hashCode());
        System.out.println("BB".hashCode());
        System.out.println("a; " + a + ", b; " + b + ",c: " + c);
        if (!reflectionAvailable()) {
            System.out.println("⚠ Рефлексия в java.util закрыта (JDK 17+).");
            System.out.println("  Добавь в Run Configuration -> Modify options -> Add VM options:");
            System.out.println("      --add-opens java.base/java.util=ALL-UNNAMED");
            System.out.println("  Без этого распределение по бакетам показать нельзя.");
            System.out.println("  Парадокс дней рождения (раздел 5) работает без рефлексии — он ниже:\n");
            int[] sizes = {8, 16, 32, 64, 128};
            for (int n : sizes) birthdayParadox(n);
            return;
        }

        // 1) Идеальный hash, маленькая capacity -> неизбежные коллизии
        System.out.println("=== 1) HashMap(16) + 12 случайных Integer ===");
        HashMap<Integer, String> small = new HashMap<>(16);
        Random rnd = new Random(42);
        for (int i = 0; i < 12; i++) small.put(rnd.nextInt(), "v" + i);
        printDistribution(small);

        // 2) Тот же набор, но capacity побольше -> почти всегда 1 на бакет
        System.out.println("\n=== 2) HashMap(128) + те же 12 элементов ===");
        HashMap<Integer, String> large = new HashMap<>(128);
        Random rnd2 = new Random(42);
        for (int i = 0; i < 12; i++) large.put(rnd2.nextInt(), "v" + i);
        printDistribution(large);

        // 3) Сломанный hashCode — все в один бакет
        System.out.println("\n=== 3) HashMap(16) + 12 ключей с одинаковым hashCode ===");
        HashMap<BadKey, String> bad = new HashMap<>(16);
        for (int i = 0; i < 12; i++) bad.put(new BadKey(i), "v" + i);
        printDistribution(bad);

        // 4) Тот же сломанный hashCode, но 30 элементов и capacity ≥ 64 -> treeify
        System.out.println("\n=== 4) HashMap(64) + 30 коллидирующих ключей (Comparable) -> дерево ===");
        HashMap<CollidingComparableKey, String> tree = new HashMap<>(64);
        for (int i = 0; i < 30; i++) tree.put(new CollidingComparableKey(i), "v" + i);
        printDistribution(tree);

        // 5) Парадокс дней рождения: для N элементов в N бакетах считаем,
        //    какая доля бакетов получает >= 2 элемента (при идеальном hash).
        System.out.println("\n=== 5) Парадокс дней рождения: N элементов в N бакетах ===");
        int[] sizes = {8, 16, 32, 64, 128};
        for (int n : sizes) birthdayParadox(n);
    }

    /** Сломанный ключ: hashCode = 0 для всех. */
    static final class BadKey {
        final int id;
        BadKey(int id) { this.id = id; }
        @Override public int hashCode() { return 0; }                           // все в один бакет!
        @Override public boolean equals(Object o) {
            return o instanceof BadKey k && id == k.id;
        }
        @Override public String toString() { return "BadKey(" + id + ")"; }
    }

    /** Коллидирующий, но Comparable — после 8 в бакете JDK сможет построить дерево. */
    static final class CollidingComparableKey implements Comparable<CollidingComparableKey> {
        final int id;
        CollidingComparableKey(int id) { this.id = id; }
        @Override public int hashCode() { return 42; }                          // все в один бакет
        @Override public boolean equals(Object o) {
            return o instanceof CollidingComparableKey k && id == k.id;
        }
        @Override public int compareTo(CollidingComparableKey o) {
            return Integer.compare(id, o.id);
        }
    }

    // ---------- помощники ----------

    /** Проверяет, открыт ли доступ к приватным полям HashMap через рефлексию. */
    private static boolean reflectionAvailable() {
        try {
            Field f = HashMap.class.getDeclaredField("table");
            f.setAccessible(true);
            return true;
        } catch (RuntimeException | NoSuchFieldException e) {
            return false;
        }
    }

    /** Печатает: сколько бакетов содержат 0, 1, 2, ... элементов, и тип узлов. */
    private static void printDistribution(HashMap<?, ?> map) throws Exception {
        Field tableField = HashMap.class.getDeclaredField("table");
        tableField.setAccessible(true);
        Object[] table = (Object[]) tableField.get(map);
        if (table == null) {
            System.out.println("  table=null (ленивая инициализация, ещё ни одного put)");
            return;
        }

        TreeMap<Integer, Integer> counts = new TreeMap<>();          // длина бакета -> сколько бакетов
        String nodeKind = "—";
        int maxBucket = 0;

        for (Object head : table) {
            int len = 0;
            Object cur = head;
            while (cur != null) {
                if (len == 0) nodeKind = cur.getClass().getSimpleName();
                len++;
                Field next = findNextField(cur.getClass());
                next.setAccessible(true);
                cur = next.get(cur);
            }
            counts.merge(len, 1, Integer::sum);
            if (len > maxBucket) maxBucket = len;
        }

        System.out.printf("  capacity=%d, size=%d, тип узлов в бакетах: %s%n",
                table.length, map.size(), nodeKind);
        counts.forEach((bucketSize, howMany) ->
                System.out.printf("    бакетов с %d элементами: %d%n", bucketSize, howMany));
        System.out.println("    максимум в одном бакете: " + maxBucket);
    }

    /** Поле next живёт в Node или TreeNode — ищем по иерархии. */
    private static Field findNextField(Class<?> c) throws NoSuchFieldException {
        for (Class<?> cur = c; cur != null; cur = cur.getSuperclass()) {
            try { return cur.getDeclaredField("next"); } catch (NoSuchFieldException ignored) { }
        }
        throw new NoSuchFieldException("next");
    }

    /**
     * Сколько коллизий в среднем при N элементах в N бакетах с идеальным случайным hash?
     * Считаем через симуляцию.
     */
    private static void birthdayParadox(int n) {
        int trials = 10_000;
        Random rnd = new Random(1);
        long totalCollidingBuckets = 0;
        long totalEmptyBuckets = 0;
        long maxBucketAcrossTrials = 0;

        for (int t = 0; t < trials; t++) {
            int[] buckets = new int[n];
            for (int i = 0; i < n; i++) buckets[rnd.nextInt(n)]++;
            int collidingBuckets = 0;
            int emptyBuckets = 0;
            int max = 0;
            for (int v : buckets) {
                if (v == 0) emptyBuckets++;
                if (v >= 2) collidingBuckets++;
                if (v > max) max = v;
            }
            totalCollidingBuckets += collidingBuckets;
            totalEmptyBuckets += emptyBuckets;
            if (max > maxBucketAcrossTrials) maxBucketAcrossTrials = max;
        }

        double avgCollidingPct = 100.0 * totalCollidingBuckets / (trials * (double) n);
        double avgEmptyPct = 100.0 * totalEmptyBuckets / (trials * (double) n);
        System.out.printf("  N=%-4d: пустых бакетов в среднем %.1f%%, с коллизиями %.1f%%, " +
                        "макс. бакет за %d прогонов: %d%n",
                n, avgEmptyPct, avgCollidingPct, trials, maxBucketAcrossTrials);
    }
}