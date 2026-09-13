package com.example.testlinux.java.core.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

/**
 * Гонка на счётчике: 4 способа инкремента из многих потоков.
 * Показывает разом ДВА эффекта: корректность (потери) и скорость.
 * ВНИМАНИЕ: это ГРУБЫЙ бенч (System.nanoTime), не JMH — цифры лишь ориентир (порядок величин).
 */
public class CounterRace {

    static final int THREADS = 8;
    static final int INCREMENTS = 1_000_000;
    static final int EXPECTED = THREADS * INCREMENTS;

    // 1) обычный int — нет ни атомарности, ни видимости
    static int plain = 0;
    // 2) volatile int — ВИДИМОСТЬ есть, АТОМАРНОСТИ нет (count++ = read-modify-write)
    static volatile int vol = 0;
    // 3) synchronized — корректно, но с БЛОКИРОВКОЙ
    static int sync = 0;
    static final Object lock = new Object();
    // 4) AtomicInteger — корректно, LOCK-FREE (CAS)
    static final AtomicInteger atomic = new AtomicInteger(0);
    // 5) LongAdder — корректно, счётчик разбит на несколько ячеек (меньше конкуренции на CAS)
    static final LongAdder adder = new LongAdder();
    // 6) per-thread local + combine — БЕЗ Atomic/lock/volatile: нет общей записи → нет гонки
    static long localTotal = 0;

    public static void main(String[] args) throws InterruptedException {
        // прогрев JIT (первый замер иначе сильно завышен)
        for (int w = 0; w < 3; w++) {
            atomic.set(0);
            runThreads(atomic::incrementAndGet);
        }

        plain = 0;
        long t1 = runThreads(() -> plain++);
        int v1 = plain;

        vol = 0;
        long t2 = runThreads(() -> vol++);
        int v2 = vol;

        atomic.set(0);
        long t3 = runThreads(atomic::incrementAndGet);
        int v3 = atomic.get();

        sync = 0;
        long t4 = runThreads(() -> { synchronized (lock) { sync++; } });
        int v4 = sync;

        adder.reset();
        long t5 = runThreads(adder::increment);
        int v5 = (int) adder.sum();

        long t6 = runPerThreadLocal();
        int v6 = (int) localTotal;

        System.out.println("=== Гонка счётчика: " + THREADS + " потоков x " + INCREMENTS
                + " = ожидаем " + String.format("%,d", EXPECTED) + " ===");
        report("1) int++ (без sync)          ", v1, t1);
        report("2) volatile int++ (видимость)", v2, t2);
        report("3) AtomicInteger (CAS)       ", v3, t3);
        report("4) synchronized (блокировка) ", v4, t4);
        report("5) LongAdder (ячейки)        ", v5, t5);
        report("6) local + combine (без sync)", v6, t6);
    }

    // запускает THREADS потоков, каждый зовёт op INCREMENTS раз; возвращает время в мс
    static long runThreads(Runnable op) throws InterruptedException {
        List<Thread> pool = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < THREADS; i++) {
            Thread th = new Thread(() -> {
                for (int j = 0; j < INCREMENTS; j++) op.run();
            });
            pool.add(th);
            th.start();
        }
        for (Thread th : pool) th.join();
        return (System.nanoTime() - start) / 1_000_000;
    }

    // 6) Каждый поток копит в ЛОКАЛЬНУЮ переменную и пишет в СВОЮ ячейку массива (разные idx →
    // нет общей записи → нет гонки). Сумму считаем в main ПОСЛЕ join() (join даёт happens-before).
    // Ни Atomic, ни lock, ни volatile не нужны.
    static long runPerThreadLocal() throws InterruptedException {
        long[] partials = new long[THREADS];               // по ячейке на поток
        List<Thread> pool = new ArrayList<>();
        long start = System.nanoTime();
        for (int i = 0; i < THREADS; i++) {
            final int idx = i;
            Thread th = new Thread(() -> {
                long local = 0;                            // приватная переменная потока
                for (int j = 0; j < INCREMENTS; j++) local++;
                partials[idx] = local;                     // одна запись в СВОЮ ячейку
            });
            pool.add(th);
            th.start();
        }
        for (Thread th : pool) th.join();                  // join → всё, что сделали потоки, видно тут
        long sum = 0;
        for (long p : partials) sum += p;                  // объединяем в main (однопоточно)
        localTotal = sum;
        return (System.nanoTime() - start) / 1_000_000;
    }

    static void report(String name, int value, long ms) {
        boolean ok = value == EXPECTED;
        String status = ok
                ? "OK (без потерь)"
                : "ПОТЕРЯНО " + String.format("%,d", (EXPECTED - value));
        System.out.printf("%s значение=%,d  %-22s  время=%d ms%n", name, value, status, ms);
    }
}
