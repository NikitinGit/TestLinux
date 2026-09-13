package com.example.testlinux.java.core.syntax;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class SampleFor {

    public static void main(String[] testString) {
        Map<Integer, LocalDateTime> nextPredictedStartTimeByRing = new HashMap<>();
        nextPredictedStartTimeByRing.put(null, LocalDateTime.now());
        LocalDateTime test = nextPredictedStartTimeByRing.get(null);

        System.out.println("test; " + test);
        List<Integer> testList = List.of(5, 2, 4, 6, 8);

        // === ВСЕ ФОРМЫ for ===
        // Полная форма: for (init; condition; update)
        //   init      — выполняется 1 раз в начале
        //   condition — проверяется ПЕРЕД каждой итерацией (если ПУСТО → считается true)
        //   update    — выполняется ПОСЛЕ каждой итерации

        // 1) Классический for по индексу
        System.out.println("--- 1) классический for по индексу ---");
        for (int i = 0; i < testList.size(); i++) {
            System.out.println("i=" + i + " -> " + testList.get(i)); // get(index) — с индексом!
        }

        // 2) for-each — для перебора коллекции (самый частый и чистый)
        System.out.println("--- 2) for-each ---");
        for (Integer x : testList) {
            System.out.println("x=" + x);
        }

        // 3) Обратный обход (от конца к началу)
        System.out.println("--- 3) обратный for ---");
        for (int i = testList.size() - 1; i >= 0; i--) {
            System.out.println("i=" + i + " -> " + testList.get(i));
        }

        // 4) Несколько переменных в init и update (через запятую)
        System.out.println("--- 4) две переменные (i растёт, j убывает) ---");
        for (int i = 0, j = testList.size() - 1; i < j; i++, j--) {
            System.out.println("i=" + i + ", j=" + j);
        }

        // 5) Пустой init (переменная объявлена СНАРУЖИ цикла)
        System.out.println("--- 5) пустой init ---");
        int k = 0;
        for (; k < 3; k++) {
            System.out.println("k=" + k);
        }

        // 6) Пустой update (инкремент делаем ВНУТРИ тела)
        System.out.println("--- 6) пустой update ---");
        for (int i = 0; i < 3;) {
            System.out.println("i=" + i);
            i++; // update вручную
        }

        // 7) Пустое condition = ВСЕГДА true = бесконечный. Это и есть ваш for(init;;)
        //    ОБЯЗАТЕЛЕН break внутри, иначе цикл висит вечно
        System.out.println("--- 7) for(init;;) + break ---");
        for (int i = 0; ; ) {                    // condition пусто → бесконечный
            if (i >= testList.size()) break;     // выход ВНУТРИ
            System.out.println("i=" + i + " -> " + testList.get(i));
            i++;
        }

        // 8) Полностью пустой for(;;) — эквивалент while(true)
        System.out.println("--- 8) for(;;) ---");
        int n = 0;
        for (; ; ) {
            if (n >= 3) break;
            System.out.println("n=" + n);
            n++;
        }

        // 9) break (прервать цикл) и continue (пропустить итерацию)
        System.out.println("--- 9) break / continue ---");
        for (int i = 0; i < testList.size(); i++) {
            if (testList.get(i) == 2) continue;  // пропустить элемент 2
            if (testList.get(i) == 6) break;     // прервать на элементе 6
            System.out.println("i=" + i + " -> " + testList.get(i));
        }

        // 10) Вложенные циклы + метка (label) — break/continue сразу для ВНЕШНЕГО
        System.out.println("--- 10) вложенные + метка (break outer) ---");
        outer:
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (i + j == 3) break outer;     // выйти сразу из ВНЕШНЕГО цикла
                System.out.println("i=" + i + ", j=" + j);
            }
        }

        // 11) отдельным методом — та самая идиома for (int c = ctl.get();;)
        casRetryLoop();
    }

    //static volatile int test = 0;
    // 11) CAS retry-loop — идиома for (int c = ctl.get();;) из java.util.concurrent (ThreadPoolExecutor).
    // Бесконечный for + break, когда удалась атомарная замена. Lock-free инкремент из многих потоков.
    static void casRetryLoop() {
        AtomicInteger ctl = new AtomicInteger(0);      // ctl — AtomicInteger; ctl.get() читает БЕЗ индекса
        int threads = 10;
        int incrementsPerThread = 100_000;

        Runnable task = () -> {
            for (int t = 0; t < incrementsPerThread; t++) {
                // ВОТ ОНА: читаем текущее значение в c, цикл бесконечный, выход по успешному CAS
                for (int c = ctl.get(); ; ) {
                    if (ctl.compareAndSet(c, c + 1)) // атомарно: "если всё ещё c → стало c+1"
                        break;                        // CAS удался → выходим
                    c = ctl.get();                    // не удался (другой поток успел) → перечитали, повтор
                }
                //int c = ctl.get();ctl.compareAndSet(c, c + 1);// - будет потерянное обновление
                //test++;// lost update
            }
        };

        List<Thread> pool = new ArrayList<>();
        for (int i = 0; i < threads; i++) {
            Thread th = new Thread(task);
            pool.add(th);
            th.start();
        }
        for (Thread th : pool) {
            try {
                th.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        int expected = threads * incrementsPerThread;
       /* System.out.println("--- 11) CAS retry-loop: ожидали=" + expected + ", получили=" + ctl.get()
                + " -> " + (ctl.get() == expected ? "OK (без потерь, lock-free)" : "ПОТЕРЯ!"));*/
    }
}
