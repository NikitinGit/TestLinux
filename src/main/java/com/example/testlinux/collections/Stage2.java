package com.example.testlinux.collections;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Data
@NoArgsConstructor
public class Stage2 {
    private String name;
    private String name2;
    public List<Stage3> stages = new ArrayList<>();

    public Stage2(String name) {
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
