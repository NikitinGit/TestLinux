package com.example.testlinux.stream.api;

import com.example.testlinux.java.core.lambda.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamApi {
    public static void main(String[] args) {
        streamTest();
        //helloStream();
    }

    private static void helloStream() {
        List<Integer> sample = new ArrayList<>();
        sample.stream().forEach(System.out::println);

        List<Stage1> stage1s = List.of(new Stage1("Nikitin"), new Stage1("Ton"));

        stage1s.stream().peek(u -> System.out.println("name: " + u.name)).toList();
/*        List<String> names = stage1s.stream()
                .flatMap(Stage1::getStagesStream)
                .map(Stage2::getName)
                .toList();
        *//*List<String> names = stage1s.stream().flatMap(stage1 -> stage1.stages.stream()).map(Stage2::getName).toList();*//*
        names.forEach(System.out::println);*/

        System.out.println("s.names: ------------------------------------------------------------------------------- ");
        stage1s.stream().map(s -> s.names).toList().forEach(
                s -> {
                    for (var a : s) {
                        System.out.println(a);
                    }
                }
        );

        System.out.println("stage3:--------------------------------------------------------------------------------- ");
        stage1s.stream().map(s -> new Stage3(s.name)).toList().forEach(s -> System.out.println(s.getName()));

        System.out.println("stage1s.stream().flatMap(Stage1::getStagesStreamStringNames).toList()");
        stage1s.stream().flatMap(s -> s.names.stream())
                .toList()
                .forEach(s -> System.out.println(s + " string test"));

        System.out.println("Stream.of(25, 5, 2):--------------------------------------------------------------------------------- ");
        Stream.of(25, 5, 2)
                .flatMap(obj -> new Stage1("Stream.of " + obj).names.stream())
                .toList()
                .forEach(System.out::println);


/*        System.out.println("Получение стрима из двумерного массива - вариант 2: плоский Stream<Stage2>");
        stage1s.get(0).getArray2dStream()
                .flatMap(Arrays::stream)
                .forEach(x -> System.out.println("name; " + x.getName()));
        // Stream<Object>: первый элемент уже Stage1, остальные — Integer
        // map: каждый Object -> Stage1 (если уже Stage1 — оставляем, иначе создаём по toString)
        // flatMap: разворачивает каждый Stage1 -> Stream<Stage2>
        Stream.of(new Stage1("Stream.of 1"), 5, 2)
                .map(obj -> obj instanceof Stage1 s ? s : new Stage1("Stream.of " + obj))
                .flatMap(Stage1::getStagesStream)
                .map(Stage2::getName)
                .toList().forEach(System.out::println);

        Stream.of(new Stage1("Stream.of 1"), new Stage1("Stream.of 2"), new Stage1("Stream.of 3"))
                .flatMap(Stage1::getStagesStream)
                .flatMap(Stage2::getStagesStream)
                .map(Stage3::getName)
                .toList().forEach(System.out::println);

        System.out.println("Вариант с Arrays.stream() - преобразуем List в массив------------------------------------------------------------------------");
        names = Arrays.stream(stage1s.toArray(new Stage1[0]))
                .flatMap(Stage1::getStagesStream)
                .flatMap(Stage2::getStagesStream)
                .map(Stage3::getName)
                .toList();
        names.forEach(System.out::println);

        System.out.println("------------------------------------------------------------------------");
        names = stage1s.stream()
                .flatMap(Stage1::getStagesStream)
                .flatMap(Stage2::getStagesStream)
                .map(Stage3::getName)
                .toList();
        names.forEach(System.out::println);

        System.out.println("------------------------------------------------------------------------");
        stage1s.stream()
                .flatMap(Stage1::getStagesStream)
                .forEach(stage2 -> {
                    System.out.println("Stage2: " + stage2.getName());
                    stage2.getStagesStream()
                            .map(Stage3::getName)
                            .forEach(name -> System.out.println("  Stage3: " + name));
                });

        System.out.println("------------------------------------------------------------------------");
        names = stage1s.stream()
                .flatMap(Stage1::getStagesStream)
                .flatMap(stage2 -> {
                    Stream<String> stage2Name = Stream.of(stage2.getName());
                    Stream<String> stage3Names = stage2.getStagesStream().map(Stage3::getName);
                    return Stream.concat(stage2Name, stage3Names);
                })
                .toList();
        names.forEach(System.out::println);

        System.out.println("--- все имена всех уровней ---");
        printAllNames(stage1s);*/
    }

    // выводит name каждого Stage1, его Stage2 и их Stage3
    // типы разные — рекурсия невозможна, поэтому concat склеивает все уровни вручную
    static void printAllNames(List<Stage1> stage1s) {
        stage1s.stream()
                .flatMap(s1 -> Stream.concat(
                        Stream.of(s1.name),                            // имя Stage1
                        s1.getStagesStream().flatMap(s2 -> Stream.concat(
                                Stream.of(s2.getName()),               // имя Stage2
                                s2.getStagesStream().map(Stage3::getName) // имена Stage3
                        ))
                ))
                .forEach(System.out::println);
    }

    static void streamTest() {
//        st1_distinct();
//        List<Integer> numsInt = List.of(44444444, 1, 2, 3, 4, 5);
//        intToLong(numsInt);
//        List<Float> numsFloat = List.of(2.5F, 400000000.0F, 3f, 4f, 5f, 8f);
//        floatToDouble(numsFloat);
//        st2_filter();
//        st3_sumOfSquaresOfEven();
//        st4_firstUnique();
//        st5_firstWithMinCount();
//        st6_groupByLength();
//        st7_top3();
//        anagrams();
//        anagramsV2();
//        anagramsV3();
//        List<Integer> task8nums = List.of(1, 2, 2, 3, 3, 3, 4);
//        task8(task8nums);
//        task8v2(task8nums);
//        task8v3(task8nums);
//        task8v4(task8nums);
//        task8v5(task8nums);
//        st8_mostFrequentElement();
//        st9_mostFrequentElement();
//        st9_flattenTask();
        st10_longestString();
    }

    private static void anagrams() {
        //7. Найти анаграммы
        List<String> anagrams = List.of("eat", "tea", "tan", "ate", "nat", "bat");

        char[] a = anagrams.get(0).toCharArray();
        Arrays.sort(a);
        char[] b = anagrams.get(2).toCharArray();
        Arrays.sort(b);
        b = null;
        System.out.println("Arrays.equals(a, b); " + Arrays.equals(a, b));

        Map<Integer, List<String>> anagramGroup = new HashMap<>();
        Set<String> checkedWord = new HashSet<>();
        for (int i = 0; i < anagrams.size(); i++) {
            String word1 = anagrams.get(i);

            List<String> words = new ArrayList<>(List.of(word1));
            if (!checkedWord.contains(word1)) {
                anagramGroup.put(i, words);
            }

            for(int j = i + 1; j < anagrams.size(); j++) {
                String word2 = anagrams.get(j);
                if (IsAnagram(word1, word2) && !checkedWord.contains(word2)) {
                    words.add(word2);
                    checkedWord.add(word2);
                }
            }
        }

        anagramGroup.entrySet().forEach(System.out::println);
    }

    private static void anagramsV2() {
        List<String> words = List.of("eat", "tea", "tan", "ate", "nat", "bat");
        Map<String, List<String>> groups = new LinkedHashMap<>();

        for (String word : words) {
            char[] chars = word.toCharArray();
            Arrays.sort(chars);
            String key = new String(chars); // "eat" -> "aet"
            groups.computeIfAbsent(key, k -> new ArrayList<>()).add(word);
        }

        groups.values().forEach(System.out::println);
    }

    private static void anagramsV3() {
        List<String> words = List.of("eat", "tea", "tan", "ate", "nat", "bat");
        words.stream()
                .collect(Collectors.groupingBy(word -> {
                    char[] chars = word.toCharArray();
                    Arrays.sort(chars);
                    return new String(chars); // ключ — отсортированные символы
                }))
                .values()
                .forEach(System.out::println);
    }

    private static boolean IsAnagram(String a, String b) {
        char[] x = a.toCharArray();
        Arrays.sort(x);
        char[] y = b.toCharArray();
        Arrays.sort(y);

        return Arrays.equals(x, y);
    }

    private static void task8(List<Integer> nums) {
        //8. Самый частый элемент
        Map<Integer, Long> groups = nums.stream()
                .collect(Collectors.groupingBy(k -> k, Collectors.counting()));

        Optional<Integer> mostFrequent = groups.entrySet().stream()
                .max(Comparator.comparingLong(Map.Entry::getValue))
                .map(Map.Entry::getKey);

        mostFrequent.ifPresent(integer -> System.out.println("Stream comparingLong task8 mostFrequent.get(); " + integer));
    }

    private static void task8v2(List<Integer> nums) {
        nums.stream()
                .collect(Collectors.groupingBy(k -> k, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(e -> System.out.println("task8v2 element=" + e.getKey() + " count=" + e.getValue()));
    }

    private static void task8v3(List<Integer> nums) {
        nums.stream()
                .distinct()
                .max(Comparator.comparingInt(n -> Collections.frequency(nums, n)))
                .ifPresent(e -> System.out.println("frequency element=" + e
                        + " count=" + Collections.frequency(nums, e)));
    }

    private static void task8v4(List<Integer> nums) {
        nums.stream()
                .collect(Collectors.toMap(k -> k, k -> 1L, Long::sum))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(e -> System.out.println("toMap element=" + e.getKey() + " count=" + e.getValue()));
    }

    private static void task8v5(List<Integer> nums) {
        nums.stream()
                .collect(Collectors.groupingBy(k -> k)) // Map<Integer, List<Integer>> {1=[1], 2=[2,2], 3=[3,3,3], 4=[4]}
                .entrySet().stream()
                .max(Map.Entry.<Integer, List<Integer>>comparingByValue(Comparator.comparingInt(List::size)).reversed()) // сравниваем по размеру списка
                .ifPresent(e -> System.out.println("task8v5 element=" + e.getKey() + " count=" + e.getValue().size()));
    }

    /// /////////////////////////////////////////
    // 1. Убрать дубликаты из списка
    static void st1_distinct() {
        //nums.stream().findFirst().ifPresent(System.out::println);
        List<Integer> nums = List.of(444444444, 3, 1, 2, 3, 5, 4, 2);
        nums.stream().distinct().toList()
                .forEach(s -> System.out.println("st1_distinct: " + s));
    }

    public static void intToLong(List<Integer> nums) {
        //nums.stream().sorted(Comparator.reverseOrder()).toList().forEach(System.out::println);
        long test = nums.stream().map((i -> i % 2 == 0 ? (long)i * i : 0))
                .distinct().reduce(Long::sum).orElse(0L);//689916708
        System.out.println("intToLong() test; " + test);
    }

    public static void floatToDouble(List<Float> nums) {
        double test = nums.stream()
                .map(i -> i % 2 == 0 ? (double)(i * i) : 0D) // явный cast float->double
                .distinct()
                .reduce(Double::sum)
                .orElse(0D);
        System.out.println("floatToDouble test; " + test);
    }

    // 2. Оставить только строки длиннее 4 символов
    static void st2_filter() {
        List<String> words = List.of("apple", "banana", "kiwi", "pear", "grape", "apple", "banana");
        words.stream().filter(s -> s.length() > 4).toList()
                .forEach(s -> System.out.println("st2_filter: " + s));

        Map<String, List<String>> map = words.stream().collect(Collectors.groupingBy(
                w -> w
        ));

        map.values().stream().sorted().forEach(System.out::println);

        //words.stream().filter(s -> map.get(s).size() == 1).findFirst().ifPresent(System.out::println);
    }

    // 3. Сумма квадратов чётных чисел
    // peek — для побочных эффектов (логирование), не для преобразования
    // map → преобразование, mapToInt → даёт IntStream с .sum()
    static void st3_sumOfSquaresOfEven() {
        Double d = 2.85;
        Double d2 = 3.25;
        float c = (float) (d + d2);
        System.out.println("c; " + c);
        List<Double> nums = List.of(1.85, 2.0, 3.0, 4.0, 5.2);
        int result = nums.stream()
                .mapToInt(Double::intValue)
                .sum();
        System.out.println("st3_sumOfSquaresOfEven: " + result); // 4+16=20

/*        //Optional<Integer> sum = nums.stream().reduce(Integer::sum);
        List<Integer> nums2 = List.of(1.85, 2, 3, 4, 5);
        // через map + reduce
        int sum2 = nums2.stream().map(f -> f % 2 == 0 ? f * f : 0).reduce(0, Integer::sum);
        System.out.println("map+reduce: " + sum2);

        // через обычный map + collect
        int sum3 = nums.stream().map(f -> f % 2 == 0 ? f * f : 0).reduce(0, (a, b) -> a + b);
        System.out.println("map+lambda: " + sum3);*/
    }

    // 4. Найти первую не повторяющуюся строку
    static void st4_firstUnique() {
        List<String> list = List.of("a", "b", "c", "a", "b", "d");
        // for-вариант: один проход, быстрее
        Set<String> set = new HashSet<>();
        Map<String, String> map = new LinkedHashMap<>();
        for (String s : list) {
            if (!set.contains(s)) {
                map.put(s, s);
                set.add(s);
            } else map.remove(s);
        }
        System.out.println("st4_firstUnique for: " + map.entrySet().iterator().next().getValue());

        // stream-вариант: два прохода, читаемее
        Map<String, Long> counts = list.stream()
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        String first = list.stream().filter(s -> counts.get(s) == 1).findFirst().orElse(null);
        System.out.println("st4_firstUnique stream: " + first);
    }

    // 5. Найти строку с минимальным количеством повторений
    static void st5_firstWithMinCount() {
        List<String> list = List.of("a", "b", "c", "a", "b", "d", "a", "b", "c", "a", "b", "c", "d");
        Map<String, Long> counts = list.stream()
                .collect(Collectors.groupingBy(s -> s, Collectors.counting()));
        long minCount = counts.values().stream().mapToLong(Long::longValue).min().orElse(0);
        String first = list.stream().filter(s -> counts.get(s) == minCount).findFirst().orElse(null);
        System.out.println("st5_firstWithMinCount minCount=" + minCount + " first=" + first);

        Map<String, Integer> countsInt = list.stream()
                .collect(Collectors.toMap(
                        k -> k,
                        v -> 1,//increment
                        Integer::sum
                ));
        int minCountInt = countsInt.values().stream().mapToInt(Integer::intValue).min().orElse(-1);
        String first2 = list.stream().filter(s -> counts.get(s) == minCount).findFirst().orElse(null);
        System.out.println("st5_firstWithMinCount 2  minCountInt=" + minCountInt + " first2=" + first2);

        // collectingAndThen — применяет финишную функцию к результату коллектора
        // если нужэен только int через Collectors
        /*Map<String, Integer> counts = list.stream()
                .collect(Collectors.groupingBy(
                        s -> s,
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));*/
    }

    // 6. Группировка по длине строки
    // длина 17 → бакет 17 % 16 = 1 → в HashMap выведется ПЕРВОЙ, хотя число 17 > 2, 3, 5
    static void st6_groupByLength() {
        List<String> words = List.of("hi", "hello", "bye", "abcdefghijklmnopq");

        // HashMap — порядок непредсказуем
        Map<Integer, List<String>> grouped = words.stream()
                .collect(Collectors.groupingBy(String::length));
        grouped.forEach((len, group) ->
                System.out.println("st6 HashMap len=" + len + " -> " + group));

        // LinkedHashMap — порядок вставки
        Map<Integer, List<String>> groupedLinked = words.stream()
                .collect(Collectors.groupingBy(String::length, LinkedHashMap::new, Collectors.toList()));
        groupedLinked.forEach((len, group) ->
                System.out.println("st6 LinkedHashMap len=" + len + " -> " + group));

        // реверс LinkedHashMap
        Map<Integer, List<String>> groupedLinkedReversed = groupedLinked.entrySet().stream()
                .sorted(Map.Entry.<Integer, List<String>>comparingByKey().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a, LinkedHashMap::new));
        groupedLinkedReversed.forEach((len, group) ->
                System.out.println("st6 LinkedHashMap reversed len=" + len + " -> " + group));

        // TreeMap — сортировка по убыванию ключа
        Map<Integer, List<String>> groupedTree = words.stream()
                .collect(Collectors.groupingBy(String::length,
                        () -> new TreeMap<>(Comparator.reverseOrder()), Collectors.toList()));
        groupedTree.forEach((len, group) ->
                System.out.println("st6 TreeMap reversed len=" + len + " -> " + group));
    }

    // 7. Топ-3 наибольших числа
    static void st7_top3() {
        List<Integer> nums = List.of(10, 5, 20, 3, 7, 30);
        List<Integer> top3 = nums.stream()
                .sorted(Comparator.reverseOrder())
                .limit(3)
                .toList();
        System.out.println("st7_top3: " + top3); // [30, 20, 10]
    }

    //8. Самый частый элемент
    static void st8_mostFrequentElement() {
        List<Integer> nums = List.of(1, 2, 2, 2, 3, 3, 4);
        var map = nums.stream().collect(
                Collectors.toMap(
                        k -> k,
                        v -> 1,
                        Integer::sum
                )
        );
        Integer intFrequent = map.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
        System.out.println("intFrequent; " + intFrequent);
    }

    // 9. Перевернуть строки и убрать дубликаты
    static void st9_mostFrequentElement() {
        List<String> words = List.of("abc", "cba", "bca", "abc");
        words.stream()
                .distinct()                                          // убрать дубликаты
                .map(v -> new StringBuilder(v).reverse().toString()) // перевернуть
                .forEach(System.out::println);
    }

    // 10. Flatten + фильтрация Получить список всех чисел > 4
    static void st9_flattenTask() {
        List<List<Integer>> list = List.of(
                List.of(1, 2, 3),
                List.of(4, 5),
                List.of(6, 7, 8)
        );

        List<Integer> listOf4 = list.stream().flatMap(Collection::stream)
                .filter(i -> i > 4)
                .toList();
        listOf4.forEach(System.out::println);
    }

    //11. Найти longest string Найти самую длинную строку
    static void st10_longestString() {
        List<String> words = List.of("a", "abcd", "abс", "ab");
//        words.stream().collect(
//                Collectors.toMap(
//                        String::length,
//                        v -> v,
//                        (f, l) -> f
//                )
//        ).entrySet().stream().max(Map.Entry.comparingByKey())
//                .ifPresent(s -> System.out.println("s.getValue(); " + s.getValue()));
        words.stream()
                .max(Comparator.comparingInt(String::length))
                .ifPresent(System.out::println);
    }

    static class StaticMethod {

        static int test1(int x, int y) {
            return x + 2 * y;
        }

        int test2(int x, int y) {
            return x * 2 + y * 2;
        }
    }
}
