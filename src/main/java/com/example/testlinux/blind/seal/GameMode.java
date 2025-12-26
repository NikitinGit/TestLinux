package com.example.testlinux.blind.seal;

/**
 * Game mode enumeration.
 * WORDS - typing words
 * LETTERS - typing individual letters
 */
public enum GameMode {
    WORDS("Слова"),
    LETTERS("Буквы");

    private final String displayName;

    GameMode(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
