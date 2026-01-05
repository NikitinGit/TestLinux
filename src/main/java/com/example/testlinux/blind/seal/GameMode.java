package com.example.testlinux.blind.seal;

import lombok.Getter;

/**
 * Game mode enumeration.
 * WORDS - typing words
 * LETTERS - typing individual letters
 * MULTIPLE_WORDS - typing words with preview of next words
 */
@Getter
public enum GameMode {
    WORDS("Слова"),
    LETTERS("Буквы"),
    MULTIPLE_WORDS("Несколько слов");

    private final String displayName;

    GameMode(String displayName) {
        this.displayName = displayName;
    }

}
