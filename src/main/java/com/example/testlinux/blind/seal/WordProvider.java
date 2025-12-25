package com.example.testlinux.blind.seal;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Provides words for the typing game with repetition logic.
 * Follows Single Responsibility Principle - manages only word selection logic.
 * If user makes an error, the word sequence will be: previous word → current word → random words.
 */
public class WordProvider {
    private static final String[] WORDS = {
            "нищий ", "тень ", "меч ", "честь ", "семь ", "нить ", "щит ", "кит ",
            "куст ", "сеть ", "месть ", "мешки ", "счет ", "цех ", "шум ", "шут ",
            "учет ", "зуб ", "куб ", "тушь ", "щи ", "тишь ", "мишень ", "цемент ",
            "мечеть ", "течь ", "смесь ", "немец ", "шесть ", "снег ", "цент ", "хит ",
            "них ", "миг ", "низ ", "тихий ", "бить ", "шить ", "гнет ", "стих ",
            "гимн ", "шест ", "тест ", "текст ",
            "шитье ", "нитью ", "щитки ", "шитый ", "чуткий ", "чисть ",
            "щепки ", "шестик ", "кистью ", "тишье ", "щенки ", "щипцы ",
            "шинкуй ", "чекань ", "щемит ", "чешки ", "щуки ", "щиты ",
            "шитью ", "чинить ", "кнут ", "шнур ", "чингис ",
            "шумит ", "чутье ", "щебни ", "чистки ", "шитьщик ",
            "щекот ", "чинки ", "щемящий ", "штих ", "чекушь ",
            "кишки ", "чушки ", "щучьи ", "шмель ", "чистенький ", "чувство "
    };

    private final Random random;
    private final Queue<String> repeatQueue;
    private String previousWord;
    private String currentWord;
    private boolean errorInCurrentWord;

    public WordProvider() {
        this.random = new Random();
        this.repeatQueue = new LinkedList<>();
        this.errorInCurrentWord = false;
    }

    /**
     * Marks that an error occurred in the current word.
     * This will trigger word repetition logic when getting the next word.
     */
    public void markError() {
        errorInCurrentWord = true;
    }

    /**
     * Checks if an error occurred in the current word.
     *
     * @return true if error occurred, false otherwise
     */
    public boolean hasErrorInCurrentWord() {
        return errorInCurrentWord;
    }

    /**
     * Returns the next word based on error status.
     * If there was an error in the current word, adds previous and current words to repeat queue.
     * Next words will be: previous word → current word → random words.
     *
     * @return the next word to type
     */
    public String getNextWord() {
        // If error occurred, add words to repeat queue
        if (errorInCurrentWord && currentWord != null) {
            if (previousWord != null) {
                repeatQueue.add(previousWord);
            }
            repeatQueue.add(currentWord);
        }

        // Select next word: from queue or random
        String nextWord;
        if (!repeatQueue.isEmpty()) {
            nextWord = repeatQueue.poll();
        } else {
            nextWord = WORDS[random.nextInt(WORDS.length)];
        }

        // Update word history and reset error flag
        previousWord = currentWord;
        currentWord = nextWord;
        errorInCurrentWord = false;

        return nextWord;
    }
}
