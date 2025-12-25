package com.example.testlinux.blind.seal;

import lombok.Getter;

/**
 * Tracks typing statistics during the game.
 * Follows Single Responsibility Principle - manages only statistical data.
 */
public class TypingStatistics {
    private int totalSymbols;
    private int wrongSymbols;
    private int completedWords;
    private int correctWords;
    private int generatedWords;
    @Getter
    private int elapsedSeconds;

    public TypingStatistics() {
        reset();
    }

    public void incrementTotalSymbols() {
        totalSymbols++;
    }

    public void incrementWrongSymbols() {
        wrongSymbols++;
    }

    public void incrementCompletedWords() {
        completedWords++;
    }

    public void incrementCorrectWords() {
        correctWords++;
    }

    public void incrementGeneratedWords() {
        generatedWords++;
    }

    public void incrementElapsedSeconds() {
        elapsedSeconds++;
    }


    /**
     * Calculates typing accuracy as a percentage.
     *
     * @return accuracy percentage (0-100)
     */
    public double getAccuracy() {
        if (totalSymbols == 0) {
            return 0.0;
        }
        return 100.0 * (1.0 - ((double) wrongSymbols / totalSymbols));
    }

    /**
     * Returns formatted statistics string for display.
     *
     * @return formatted statistics
     */
    public String getFormattedStats() {
        return String.format("Всего символов: %d | Ошибок: %d | Точность: %.1f%%",
                totalSymbols, wrongSymbols, getAccuracy());
    }

    /**
     * Returns formatted final results for game end.
     *
     * @return formatted final results
     */
    public String getFormattedFinalResults() {
        return String.format("<html><center>Сгенерировано рандомных слов: %d<br>Набрано слов: %d<br>Правильно набранных слов: %d<br>Набрано всего символов: %d<br>" +
                        "Набрано неверных символов: %d<br>Процент попадания: %.2f%%</center></html>",
                generatedWords, completedWords, correctWords, totalSymbols, wrongSymbols, getAccuracy());
    }

    public void reset() {
        totalSymbols = 0;
        wrongSymbols = 0;
        completedWords = 0;
        correctWords = 0;
        generatedWords = 0;
        elapsedSeconds = 0;
    }
}
