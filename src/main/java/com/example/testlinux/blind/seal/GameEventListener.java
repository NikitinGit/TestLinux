package com.example.testlinux.blind.seal;

/**
 * Interface for handling game events.
 * Follows Interface Segregation Principle and Dependency Inversion Principle.
 * Implements Observer pattern for loose coupling between game logic and UI.
 */
public interface GameEventListener {
    /**
     * Called when a correct character is typed.
     */
    void onCorrectChar();

    /**
     * Called when an incorrect character is typed.
     */
    void onIncorrectChar();

    /**
     * Called when a word is completed.
     */
    void onWordCompleted();

    /**
     * Called when a second elapses.
     */
    void onSecondElapsed();

    /**
     * Called when the game ends.
     */
    void onGameEnd();

    /**
     * Called when a new word is generated.
     *
     * @param word the new word
     */
    void onNewWord(String word);
}
