package com.example.testlinux.stream.api;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class StreamLazyDemo {

    static int operationCount = 0;

    static boolean filterEven(int n) {
        operationCount++;
        System.out.println("  filter(" + n + ") — чётное? " + (n % 2 == 0));
        return n % 2 == 0;
    }

    static int multiply(int n) {
        operationCount++;
        System.out.println("  map(" + n + ") -> " + (n * 10));
        return n * 10;
    }

    public static void main(String[] args) {
        List<Integer> data = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // =====================================================
        // ДЕМО 1: Short-circuiting — findFirst() останавливает обработку
        // =====================================================
        System.out.println("=== ДЕМО 1: Stream.findFirst() — ищем первое чётное ===");
        operationCount = 0;

        var result = data.stream()
                .filter(StreamLazyDemo::filterEven)
                .map(StreamLazyDemo::multiply)
                .findFirst();

        System.out.println("Результат: " + result.orElse(-1));
        System.out.println("Операций: " + operationCount);
        // filter вызвался только для 1 и 2, map — только для 2. Итого 3 операции.

        System.out.println();

        // Тот же результат через for:
        System.out.println("=== FOR-аналог: ===");
        operationCount = 0;
        for (int n : data) {
            if (filterEven(n)) {
                int mapped = multiply(n);
                System.out.println("Результат: " + mapped);
                System.out.println("Операций: " + operationCount);
                break;
            }
        }
        // То же количество — for тоже может, но надо явно писать break.

        System.out.println();

        // =====================================================
        // ДЕМО 2: Поэлементная обработка vs. послойная
        // =====================================================
        System.out.println("=== ДЕМО 2: Порядок обработки — поэлементно через цепочку ===");
        operationCount = 0;

        data.stream()
                .filter(StreamLazyDemo::filterEven)
                .map(StreamLazyDemo::multiply)
                .forEach(v -> System.out.println("  -> consume(" + v + ")"));

        System.out.println("Операций: " + operationCount);
        // Обратите внимание в выводе: filter(1), filter(2), map(2), consume(20), filter(3), ...
        // Элементы идут ОДИН ЗА ДРУГИМ через всю цепочку, а НЕ "сначала все filter, потом все map".

        System.out.println();

        // =====================================================
        // ДЕМО 3: Нет промежуточных коллекций
        // =====================================================
        System.out.println("=== ДЕМО 3: for с промежуточными списками vs stream ===");

        // Наивный for — создаёт промежуточный список:
        List<Integer> filtered = new ArrayList<>();
        for (int n : data) {
            if (n % 2 == 0) filtered.add(n);
        }
        List<Integer> mapped = new ArrayList<>();
        for (int n : filtered) {
            mapped.add(n * 10);
        }
        System.out.println("for результат: " + mapped);
        System.out.println("Промежуточных списков создано: 2 (filtered + mapped)");

        // Stream — ноль промежуточных списков:
        List<Integer> streamResult = data.stream()
                .filter(n -> n % 2 == 0)
                .map(n -> n * 10)
                .toList();
        System.out.println("stream результат: " + streamResult);
        System.out.println("Промежуточных списков создано: 0");

        System.out.println();

        // =====================================================
        // ДЕМО 4: limit() на бесконечном потоке — без ленивости невозможно
        // =====================================================
        System.out.println("=== ДЕМО 4: Бесконечный поток + limit ===");
        operationCount = 0;

        List<Integer> firstFiveEven = Stream.iterate(1, n -> n + 1) // бесконечный поток 1, 2, 3, ...
                .filter(StreamLazyDemo::filterEven)
                .limit(5)
                .toList();

        System.out.println("Первые 5 чётных: " + firstFiveEven);
        System.out.println("Операций filter: " + operationCount);
        // Обработал только 10 элементов, а не бесконечность. Попробуй это в for — нужен явный счётчик и break.
    }
}