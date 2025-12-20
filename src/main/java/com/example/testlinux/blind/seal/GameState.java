package com.example.testlinux.blind.seal;

import lombok.Getter;
import lombok.Setter;

/**
 * Represents the current state of the typing game.
 * Follows Single Responsibility Principle - manages only game state data.
 */
public class GameState {
    @Getter
    private String currentWord;
    private StringBuilder typedWord;
    @Setter
    @Getter
    private boolean active;

    public GameState() {
        this.typedWord = new StringBuilder();
        this.active = false;
    }

    public void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
        this.typedWord = new StringBuilder();
    }

    public String getTypedWord() {
        return typedWord.toString();
    }

    public void appendTypedChar(char c) {
        typedWord.append(c);
    }

    public boolean isWordCompleted() {
        return currentWord != null && typedWord.length() == currentWord.length();
    }

    public boolean isCharCorrect(char inputChar) {
        int position = typedWord.length();
        return position < currentWord.length() && inputChar == currentWord.charAt(position);
    }
}
