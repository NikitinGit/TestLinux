package com.example.testlinux.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class StreamApi {
    public static void main(String[] args) {
        List<Integer> sample = new ArrayList<>();
        sample.stream().forEach(System.out::println);

        List<Stage1> stage1s = List.of(new Stage1("Nikitin"), new Stage1("Ton"));

        List<String> names = stage1s.stream()
                .flatMap(Stage1::getStagesStream)
                .map(Stage2::getName)
                .toList();
        /*List<String> names = stage1s.stream().flatMap(stage1 -> stage1.stages.stream()).map(Stage2::getName).toList();*/
        names.forEach(System.out::println);

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
        // Получение стрима из двумерного массива - вариант 2: плоский Stream<Integer>
        stage1s.get(0).getArray2dStream()
                .flatMap(Arrays::stream)
                .forEach(System.out::println);

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
    }
}
