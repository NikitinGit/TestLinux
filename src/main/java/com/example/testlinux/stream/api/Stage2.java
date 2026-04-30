package com.example.testlinux.stream.api;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class Stage2 {
    private String name;
    private String name2;
    public List<Stage3> stages = new ArrayList<>();

    private final Random random1 = new Random();
    private int randomInt1;

    public int getRandom1() {
        return randomInt1;
    }

    public Stage2(String name) {
        randomInt1 = random1.nextInt(8);

        this.name = name;
        this.name2 = name + " to 2";
        for(int i = 0; i < 3; i ++) {
            stages.add(new Stage3(name + i));
        }
    }

    public Stream<Stage3> getStagesStream() {
        return stages.stream();
    }
}
