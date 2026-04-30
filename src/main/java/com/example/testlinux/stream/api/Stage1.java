package com.example.testlinux.stream.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class Stage1 {
    public List<String> names = new ArrayList<>(List.of("name1;", "name2;"));
    public String name;
    public double d;

    private final Random random1 = new Random();
    private final Random random2 = new Random();

    private int randomInt1;
    private int randomInt2;
    private int randomInt3;

    private Stage2 stage2;

    public Stage2 getStage2() {
        return stage2;
    }

    public int getRandom1() {
        return randomInt1;
    }

    public int getRandom2() {
        return randomInt2;
    }

    public int getRandom3() {
        return randomInt3;
    }

    public List<Stage2> stages = new ArrayList<>();
    private Stage2[][] array2d = new Stage2[][] {
            {new Stage2("1bn"), new Stage2("12bn"), new Stage2("13bn")},
            {new Stage2("14bn"), new Stage2("51bn")}
    };

    public Stage1(String name) {
        randomInt1 = random1.nextInt(5);
        randomInt2 = random2.nextInt(8);
        randomInt3 = random2.nextInt(8);

        stage2 = new Stage2("random");

        this.name = name;
        for(int i = 0; i < 3; i ++) {
            stages.add(new Stage2(name + i));
        }

        names.replaceAll(nam -> nam + name);
    }

    public Stage1(double d) {
        this.d = d;
    }

    public Stream<Stage2> getStagesStream() {
        return stages.stream();
    }

    public Stream<String> getStagesStreamString() {
        return Stream.of(name);
    }

    public Stream<String> getStagesStreamStringNames() {
        return names.stream();
    }

    public Stream<Stage2[]> getArray2dStream() {
        return Arrays.stream(array2d);
    }
}
