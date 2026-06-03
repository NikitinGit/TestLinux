package com.example.testlinux.java.core.hashcodetest;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Как правильно задать initialCapacity, когда заранее знаешь N.
 *
 * Подводный камень:
 *   new HashMap<>(N) задаёт capacity ~ N (округлённую до степени двойки),
 *   а threshold = capacity * 0.75. Значит при вставке N элементов один resize
 *   ВСЁ РАВНО сработает (на 0.75*N-м put), и он самый дорогой — рехеш почти всех.
 *
 *   Чтобы избежать resize, capacity нужна > N / 0.75:
 *      new HashMap<>((int)(N / 0.75f) + 1)
 *
 *   В Java 19+ для этого есть готовый метод:
 *      HashMap.newHashMap(N)
 *
 * Этот файл показывает все 4 варианта на N=1_000_000:
 *   1) new HashMap<>()                        — дефолт, ~17 resize-ов
 *   2) new HashMap<>(N)                       — 1 большой resize (наивный pre-size)
 *   3) new HashMap<>((int)(N / 0.75f) + 1)    — 0 resize, "ручная" формула
 *   4) HashMap.newHashMap(N)                  — 0 resize, Java 19+, самый читаемый
 *
 * Запускать с JVM-флагом:
 *   --add-opens java.base/java.util=ALL-UNNAMED
 * (без него capacity покажется как "??").
 */
public class _10_PreSizingWithNewHashMap {

    private static final int N = 1_000_000;

    public static void main(String[] args) throws Exception {

        // прогрев JIT
        for (int i = 0; i < 3; i++) {
            fill(new HashMap<>(), N);
            fill(new HashMap<>(N), N);
            fill(new HashMap<>((int)(N / 0.75f) + 1), N);
            fill(HashMap.newHashMap(N), N);
        }

        System.out.println("=== Бенчмарк put(N=1_000_000) для 4 вариантов ===\n");

        Result r1 = measure("new HashMap<>()                    ", () -> new HashMap<>());
        Result r2 = measure("new HashMap<>(N)                   ", () -> new HashMap<>(N));
        Result r3 = measure("new HashMap<>((int)(N / 0.75f) + 1)", () -> new HashMap<>((int)(N / 0.75f) + 1));
        Result r4 = measure("HashMap.newHashMap(N)              ", () -> HashMap.newHashMap(N));

        System.out.println();
        System.out.printf("выигрыш newHashMap(N) vs default:        %.2fx%n", (double) r1.elapsed / r4.elapsed);
        System.out.printf("выигрыш newHashMap(N) vs new HashMap(N): %.2fx%n", (double) r2.elapsed / r4.elapsed);

        // Покажем, что #3 и #4 дают одинаковую capacity
        System.out.println("\n=== Конечная capacity у каждого варианта ===");
        printFinalCapacity("new HashMap<>()                    ", fill(new HashMap<>(), N));
        printFinalCapacity("new HashMap<>(N)                   ", fill(new HashMap<>(N), N));
        printFinalCapacity("new HashMap<>((int)(N / 0.75f) + 1)", fill(new HashMap<>((int)(N / 0.75f) + 1), N));
        printFinalCapacity("HashMap.newHashMap(N)              ", fill(HashMap.newHashMap(N), N));

        // Покажем, что getReturns для разных N: формула N -> capacity
        System.out.println("\n=== Что выдаёт HashMap.newHashMap(N) для разных N ===");
        for (int n : new int[]{10, 100, 1000, 10_000, 100_000, 1_000_000}) {
            HashMap<Integer, Integer> m = HashMap.newHashMap(n);
            for (int i = 0; i < n; i++) m.put(i, i);
            Integer cap = readTableLength(m);
            int expectedNoResize = (int)(n / 0.75f) + 1;
            System.out.printf("N=%-8d -> HashMap.newHashMap(%-8d) -> capacity=%s (ожидали ≥ %d)%n",
                    n, n, cap == null ? "??" : cap.toString(), expectedNoResize);
        }
    }

    private static HashMap<Integer, Integer> fill(HashMap<Integer, Integer> map, int n) {
        for (int i = 0; i < n; i++) map.put(i, i);
        return map;
    }

    private static Result measure(String label, java.util.function.Supplier<HashMap<Integer, Integer>> factory) {
        long t = System.nanoTime();
        HashMap<Integer, Integer> map = factory.get();
        for (int i = 0; i < N; i++) map.put(i, i);
        long elapsed = System.nanoTime() - t;
        Integer cap = readTableLength(map);
        System.out.printf("%s : %,12d нс = %3d мс, capacity=%s%n",
                label, elapsed, elapsed / 1_000_000, cap == null ? "??" : cap.toString());
        return new Result(elapsed);
    }

    private static void printFinalCapacity(String label, HashMap<?, ?> map) {
        Integer cap = readTableLength(map);
        System.out.printf("%s : capacity=%s%n", label, cap == null ? "?? (нужен --add-opens)" : cap.toString());
    }

    /** Через рефлексию читает HashMap.table.length, либо null если рефлексия закрыта. */
    private static Integer readTableLength(HashMap<?, ?> map) {
        try {
            Field f = HashMap.class.getDeclaredField("table");
            f.setAccessible(true);
            Object table = f.get(map);
            return table == null ? null : ((Object[]) table).length;
        } catch (ReflectiveOperationException | RuntimeException e) {
            return null;
        }
    }

    private record Result(long elapsed) { }
}