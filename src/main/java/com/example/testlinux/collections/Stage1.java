package com.example.testlinux.collections;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Stage1 {
    public List<Stage2> stages = new ArrayList<>();

    public Stage1(String name) {
        for(int i = 0; i < 3; i ++) {
            stages.add(new Stage2(name + i));
        }
    }

    public Stream<Stage2> getStagesStream() {
        return stages.stream();
    }
}
