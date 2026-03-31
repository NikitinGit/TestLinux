package com.example.testlinux.java.core.lambda;

import lombok.Data;

@Data
public class Function1 {

    private String name;

    private Function2 f2;

    public Function1(String name) {
        this.name = name;
    }

    public Function2 getF2() {
        return new Function2("Function1::getF2()");
    }
}
