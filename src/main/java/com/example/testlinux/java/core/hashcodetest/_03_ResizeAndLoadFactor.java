package com.example.testlinux.java.core.hashcodetest;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Когда HashMap увеличивает внутренний массив (rehash / resize).
 *
 * - Изначальная capacity = 16 (если не задал в конструкторе).
 * - loadFactor по умолчанию = 0.75f.
 * - threshold = (int)(capacity * loadFactor). Для 16 -> 12.
 * - Как только size > threshold, capacity удваивается, и ВСЕ элементы перераскладываются
 *   по новым бакетам (бакет считается заново: hash & (newCapacity-1)).
 *
 * Зачем такой коэффициент 0.75:
 *   - меньше (0.5) -> меньше коллизий, но больше памяти и более частые resize
 *   - больше (1.0) -> экономнее по памяти, но коллизии копятся
 *   - 0.75 — компромисс по умолчанию.
 *
 * Если знаешь заранее размер — задавай initialCapacity:
 *   new HashMap<>(expectedSize / 0.75 + 1)  // чтобы избежать ресайза вообще
 *
 * Ниже — самописная мини-карта, которая печатает момент resize.
 * (К полю table настоящего HashMap из JDK 17 нельзя достучаться без --add-opens.)
 */
public class _03_ResizeAndLoadFactor {

    public static void main(String[] args) {
        // 1) Математика: что произойдёт в стандартном HashMap
        System.out.println("=== Теория для стандартного HashMap ===");
        printThresholds(16);
        printThresholds(32);
        printThresholds(64);
        printThresholds(128);

        // 2) То же поведение на самописной мини-карте, чтобы увидеть момент resize
        System.out.println("\n=== Мини-карта: реальный resize ===");
        Mini map = new Mini();
        for (int i = 1; i <= 30; i++) {
            map.put(i, i);
            System.out.printf("put %2d -> size=%2d capacity=%d threshold=%d%n",
                    i, map.size, map.table.length, map.threshold);
        }

        // 3) Сравнение со стандартным HashMap по количеству ресайзов:
        //    если заранее указать достаточный initialCapacity — ресайза не будет вовсе.
        //
        //    Реальная capacity у HashMap скрыта в private поле table — посмотрим через рефлексию.
        //    JDK 17+ требует для этого JVM-флаг:
        //        --add-opens java.base/java.util=ALL-UNNAMED
        //    Без него рефлексия не пройдёт, и мы покажем подсказку.

        System.out.println("\n=== Стандартный HashMap с initialCapacity=64 — capacity не меняется ===");
        HashMap<Integer, Integer> noResize = new HashMap<>(64);
        printHashMapState("после конструктора", noResize);
        fillWithSnapshots(noResize, 30, new int[]{1, 12, 13, 24, 25, 30});

        System.out.println("\n=== Стандартный HashMap БЕЗ initialCapacity (=16) — capacity растёт ===");
        HashMap<Integer, Integer> defaultMap = new HashMap<>();
        printHashMapState("после конструктора", defaultMap);
        fillWithSnapshots(defaultMap, 30, new int[]{1, 12, 13, 24, 25, 30});
        // Ожидание: capacity 16 -> 32 -> 64 (два resize), threshold 12 -> 24 -> 48.
    }

    /** Кладёт totalPuts элементов и печатает состояние после позиций из milestones. */
    private static void fillWithSnapshots(HashMap<Integer, Integer> map, int totalPuts, int[] milestones) {
        int idx = 0;
        for (int i = 1; i <= totalPuts; i++) {
            map.put(i, i);
            if (idx < milestones.length && i == milestones[idx]) {
                printHashMapState("после " + i + "-го put", map);
                idx++;
            }
        }
    }

    /**
     * Печатает size, capacity и threshold через рефлексию.
     * HashMap ленивый: до первого put поле table == null, capacity ещё нет.
     * В threshold в этот момент лежит "tableSizeFor(initialCapacity)" — подсказка для первого resize.
     */
    private static void printHashMapState(String label, HashMap<?, ?> map) {
        State s = readState(map);
        if (s == null) {
            System.out.printf("%-22s size=%d (рефлексия закрыта — нужен JVM-флаг " +
                    "--add-opens java.base/java.util=ALL-UNNAMED)%n", label, map.size());
            return;
        }
        String cap = s.tableLen < 0 ? "lazy(null)" : Integer.toString(s.tableLen);
        System.out.printf("%-22s size=%-3d capacity=%-10s threshold=%d%n",
                label, map.size(), cap, s.threshold);
    }

    private static State readState(HashMap<?, ?> map) {
        try {
            Field tableField = HashMap.class.getDeclaredField("table");
            Field thresholdField = HashMap.class.getDeclaredField("threshold");
            tableField.setAccessible(true);
            thresholdField.setAccessible(true);
            Object table = tableField.get(map);
            int tableLen = table == null ? -1 : ((Object[]) table).length;
            return new State(tableLen, thresholdField.getInt(map));
        } catch (ReflectiveOperationException | RuntimeException e) {
            return null;
        }
    }

    private record State(int tableLen, int threshold) { }

    private static void printThresholds(int capacity) {
        int threshold = (int) (capacity * 0.75f);
        System.out.printf("capacity=%-4d -> threshold=%-4d  (после %d-го элемента -> resize до %d)%n",
                capacity, threshold, threshold + 1, capacity * 2);
    }

    /** Минимальная HashMap-подобная карта без деревьев. Видим, КОГДА происходит resize. */
    static final class Mini {
        Object[] table = new Object[16];
        int size;
        int threshold = 12;

        static final class Node {
            final int hash;
            final Object key;
            Object value;
            Node next;
            Node(int hash, Object key, Object value, Node next) {
                this.hash = hash; this.key = key; this.value = value; this.next = next;
            }
        }

        void put(Object key, Object value) {
            int h = key.hashCode() ^ (key.hashCode() >>> 16);
            int i = h & (table.length - 1);
            for (Node n = (Node) table[i]; n != null; n = n.next) {
                if (n.hash == h && n.key.equals(key)) { n.value = value; return; }
            }
            table[i] = new Node(h, key, value, (Node) table[i]);
            size++;
            if (size > threshold) resize();
        }

        void resize() {
            int newCap = table.length * 2;
            Object[] fresh = new Object[newCap];
            int mask = newCap - 1;
            for (Object head : table) {
                Node n = (Node) head;
                while (n != null) {
                    Node next = n.next;
                    int i = n.hash & mask;
                    n.next = (Node) fresh[i];
                    fresh[i] = n;
                    n = next;
                }
            }
            table = fresh;
            threshold = (int) (newCap * 0.75f);
            System.out.println("   >> RESIZE -> capacity=" + newCap + ", threshold=" + threshold);
        }
    }
}
