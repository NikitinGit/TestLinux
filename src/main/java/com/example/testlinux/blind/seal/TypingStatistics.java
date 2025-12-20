package com.example.testlinux.blind.seal;

/**
 * Tracks typing statistics during the game.
 * Follows Single Responsibility Principle - manages only statistical data.
 */
public class TypingStatistics {
    private int totalSymbols;
    private int wrongSymbols;
    private int completedWords;
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

    public void incrementElapsedSeconds() {
        elapsedSeconds++;
    }

    public int getTotalSymbols() {
        return totalSymbols;
    }

    public int getWrongSymbols() {
        return wrongSymbols;
    }

    public int getCompletedWords() {
        return completedWords;
    }

    public int getElapsedSeconds() {
        return elapsedSeconds;
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
        return String.format("<html><center>Набрано слов: %d<br>Набрано всего символов: %d<br>" +
                        "Набрано неверных символов: %d<br>Процент попадания: %.2f%%</center></html>",
                completedWords, totalSymbols, wrongSymbols, getAccuracy());
    }

    public void reset() {
        totalSymbols = 0;
        wrongSymbols = 0;
        completedWords = 0;
        elapsedSeconds = 0;
    }
}
