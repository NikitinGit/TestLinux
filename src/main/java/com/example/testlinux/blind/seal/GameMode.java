package com.example.testlinux.blind.seal;

import lombok.Getter;

/**
 * Game mode enumeration.
 * WORDS - typing words
 * LETTERS - typing individual letters
 */
@Getter
public enum GameMode {
    WORDS("Слова"),
    LETTERS("Буквы");

    private final String displayName;

    GameMode(String displayName) {
        this.displayName = displayName;
    }

}
