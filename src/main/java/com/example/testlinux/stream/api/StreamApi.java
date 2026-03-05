package com.example.testlinux.stream.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class StreamApi {
    public static void main(String[] args) {
        List<Integer> sample = new ArrayList<>();
        sample.stream().forEach(System.out::println);

        List<Stage1> stage1s = List.of(new Stage1("Nikitin"), new Stage1("Ton"));

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
}
