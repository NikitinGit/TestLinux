package com.example.testlinux.blind.seal;

import java.awt.event.KeyEvent;

/**
 * Processes user keyboard input and updates game state.
 * Follows Single Responsibility Principle - manages only input processing logic.
 */
public class InputProcessor {
    private final GameState gameState;
    private final TypingStatistics statistics;
    private final WordProvider wordProvider;
    private final GameEventListener eventListener;

    /**
     * Creates an InputProcessor with dependencies.
     * Follows Dependency Inversion Principle - depends on abstractions.
     *
     * @param gameState     game state manager
     * @param statistics    statistics tracker
     * @param wordProvider  word provider
     * @param eventListener event listener for game events
     */
    public InputProcessor(GameState gameState, TypingStatistics statistics,
                          WordProvider wordProvider, GameEventListener eventListener) {
        this.gameState = gameState;
        this.statistics = statistics;
        this.wordProvider = wordProvider;
        this.eventListener = eventListener;
    }

    /**
     * Processes a character input from the user.
     *
     * @param inputChar the character typed by the user
     */
    public void processCharacter(char inputChar) {
        if (!gameState.isActive()) {
            return;
        }

        // Ignore backspace
        if (inputChar == KeyEvent.VK_BACK_SPACE) {
            return;
        }

        statistics.incrementTotalSymbols();

        if (gameState.isCharCorrect(inputChar)) {
            handleCorrectChar(inputChar);
        } else {
            handleIncorrectChar();
        }
    }

    private void handleCorrectChar(char inputChar) {
        gameState.appendTypedChar(inputChar);
        eventListener.onCorrectChar();

        if (gameState.isWordCompleted()) {
            statistics.incrementCompletedWords();
            eventListener.onWordCompleted();
            generateNewWord();
        }
    }

    private void handleIncorrectChar() {
        statistics.incrementWrongSymbols();
        wordProvider.markError();
        eventListener.onIncorrectChar();
    }

    /**
     * Generates a new word for the game.
     * Delegates word selection logic to WordProvider.
     */
    public void generateNewWord() {
        String newWord = wordProvider.getNextWord();

        gameState.setCurrentWord(newWord);
        eventListener.onNewWord(newWord);
    }
}
