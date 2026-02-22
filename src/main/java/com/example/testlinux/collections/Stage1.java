package com.example.testlinux.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class Stage1 {
    public List<Stage2> stages = new ArrayList<>();
    private Stage2[][] array2d = new Stage2[][] {
            {new Stage2("1bn"), new Stage2("12bn"), new Stage2("13bn")},
            {new Stage2("14bn"), new Stage2("51bn")}
    };

    public Stage1(String name) {
        for(int i = 0; i < 3; i ++) {
            stages.add(new Stage2(name + i));
        }
    }

    public Stream<Stage2> getStagesStream() {
        return stages.stream();
    }

    public Stream<Stage2[]> getArray2dStream() {
        return Arrays.stream(array2d);
    }
}
