package com.example.testlinux.stream.api;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Stage3 {

    private String name;

    public Stage3(String name) {
        this.name = name + " - stage3 ";
    }
}
