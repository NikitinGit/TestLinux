package com.example.testlinux.blind.seal;

import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

/**
 * Provides words or letters for the typing game with repetition logic.
 * Follows Single Responsibility Principle - manages only word/letter selection logic.
 * If user makes an error, the word sequence will be: previous word → current word → random words.
 */
public class WordProvider {
    private static final String[] LETTERS = {
            "б", "г", "е", "ё", "з", "и", "й", "к", "м", "н", "с", "т",
            "у", "х", "ц", "ч", "ш", "щ", "ъ", "ь", "ю", "я"
    };

    private static final String[] WORDS = {
            "нищий", "тень", "меч", "честь", "семь", "нить", "щит", "кит",
            "куст", "сеть", "месть", "мешки", "счет", "цех", "шум", "шут",
            "учет", "зуб", "куб", "тушь", "щи", "тишь", "мишень", "цемент",
            "мечеть", "течь", "смесь", "немец", "шесть", "снег", "цент", "хит",
            "них", "миг", "низ", "тихий", "бить", "шить", "гнет", "стих",
            "гимн", "шест", "тест", "текст",
            "шитье", "нитью", "щитки", "шитый", "чуткий", "чисть",
            "щепки", "шестик", "кистью", "тишье", "щенки", "щипцы",
            "шинкуй", "чекань", "щемит", "чешки", "щуки", "щиты",
            "шитью", "чинить", "кнут", "шнур", "чингис",
            "шумит", "чутье", "щебни", "чистки", "шитьщик",
            "щекот", "чинки", "щемящий", "штих", "чекушь",
            "кишки", "чушки", "щучьи", "шмель", "чистенький", "чувство"
    };

    private final Random random;
    private final Queue<String> repeatQueue;
    private final Queue<String> upcomingWordsQueue; // Queue for MULTIPLE_WORDS mode
    private String previousWord;
    private String currentWord;
    private boolean errorInCurrentWord;
    private boolean lastWordWasRandom;
    /**
     * -- GETTER --
     *  Gets the current game mode.
     *
     * @return the current game mode
     */
    @Getter
    private GameMode gameMode;
    @Setter
    private String initialSymbol;

    public WordProvider() {
        this.random = new Random();
        this.repeatQueue = new LinkedList<>();
        this.upcomingWordsQueue = new LinkedList<>();
        this.errorInCurrentWord = false;
        this.gameMode = GameMode.WORDS; // Default mode
    }

    /**
     * Sets the game mode (WORDS or LETTERS).
     *
     * @param mode the game mode to set
     */
    public void setGameMode(GameMode mode) {
        this.initialSymbol = LETTERS[random.nextInt(LETTERS.length)];
        this.gameMode = mode;

        // Initialize upcoming words queue for MULTIPLE_WORDS mode
        if (mode == GameMode.MULTIPLE_WORDS) {
            upcomingWordsQueue.clear();
            String[] source = WORDS;
            // Pre-fill with 3 words
            for (int i = 0; i < 3; i++) {
                upcomingWordsQueue.add(source[random.nextInt(source.length)] + " ");
            }
        }
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
     * Checks if the last word was randomly selected from WORDS array.
     * @return true if word was random, false if from repeat queue
     */
    public boolean wasLastWordRandom() {
        return lastWordWasRandom;
    }

    /**
     * Returns the next word based on error status.
     * For MULTIPLE_WORDS mode: If error occurred, adds only current word to repeat queue.
     * For other modes: If error occurred, adds previous and current words to repeat queue.
     *
     * @return the next word to type
     */
    public String getNextWord() {
        // If error occurred, add words to repeat queue
        if (errorInCurrentWord && currentWord != null) {
            if (gameMode == GameMode.MULTIPLE_WORDS) {
                // For MULTIPLE_WORDS mode: only add current word
                repeatQueue.add(currentWord);
            } else {
                // For other modes: add previous and current words
                if (previousWord != null) {
                    repeatQueue.add(previousWord);
                }
                repeatQueue.add(currentWord);
            }
        }

        // Select next word: from queue or random
        String nextWord;
        if (!repeatQueue.isEmpty()) {
            nextWord = repeatQueue.poll();
            lastWordWasRandom = false;
        } else if (gameMode == GameMode.MULTIPLE_WORDS) {
            // For MULTIPLE_WORDS mode: take from upcoming queue and add new word to end
            nextWord = upcomingWordsQueue.poll();
            String[] source = WORDS;
            upcomingWordsQueue.add(source[random.nextInt(source.length)] + " ");
            lastWordWasRandom = true;
        } else {
            // For other modes: generate random word
            String[] source = (gameMode == GameMode.LETTERS) ? LETTERS : WORDS;
            nextWord = (gameMode == GameMode.LETTERS)
                    ? (initialSymbol + source[random.nextInt(source.length)])
                    : source[random.nextInt(source.length)];
            lastWordWasRandom = true;
            nextWord += " ";
        }

        // Update word history and reset error flag
        previousWord = currentWord;
        currentWord = nextWord;
        errorInCurrentWord = false;

        return nextWord;
    }

    /**
     * Returns the next N words for preview (without actually moving to them).
     * For MULTIPLE_WORDS mode: returns actual words from the upcoming queue.
     * For other modes: generates random preview words.
     *
     * @param count number of words to preview
     * @return array of preview words
     */
    public String[] getPreviewWords(int count) {
        String[] previewWords = new String[count];

        if (gameMode == GameMode.MULTIPLE_WORDS) {
            // Return actual words from the queue without removing them
            Object[] queueArray = upcomingWordsQueue.toArray();
            for (int i = 0; i < count && i < queueArray.length; i++) {
                previewWords[i] = (String) queueArray[i];
            }
        } else {
            // For other modes: generate random preview words (not used currently)
            String[] source = (gameMode == GameMode.LETTERS) ? LETTERS : WORDS;
            for (int i = 0; i < count; i++) {
                if (gameMode == GameMode.LETTERS) {
                    previewWords[i] = initialSymbol + source[random.nextInt(source.length)] + " ";
                } else {
                    previewWords[i] = source[random.nextInt(source.length)] + " ";
                }
            }
        }

        return previewWords;
    }
}
