package com.example.testlinux.blind.seal;

import java.util.Random;

/**
 * Provides random words for the typing game.
 * Follows Single Responsibility Principle - manages only word selection logic.
 */
public class WordProvider {
    private static final String[] WORDS = {
            "нищий ", "тень ", "меч ", "честь ", "семь ", "нить ", "щит ", "кит ",
            "куст ", "сеть ", "месть ", "мешки ", "счет ", "цех ", "шум ", "шут ",
            "учет ", "зуб ", "куб ", "тушь ", "щи ", "тишь ", "мишень ", "цемент "
    };

    private final Random random;

    public WordProvider() {
        this.random = new Random();
    }

    /**
     * Returns a random word from the word list.
     *
     * @return a random word
     */
    public String getRandomWord() {
        return WORDS[random.nextInt(WORDS.length)];
    }

    /**
     * Returns the total number of words available.
     *
     * @return word count
     */
    public int getWordCount() {
        return WORDS.length;
    }
}
