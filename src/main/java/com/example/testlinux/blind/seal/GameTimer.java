package com.example.testlinux.blind.seal;

import javax.swing.Timer;

/**
 * Manages all game timers (game duration, seconds counter, blink effect).
 * Follows Single Responsibility Principle - manages only timer logic.
 */
public class GameTimer {
    private static final int GAME_DURATION_MS = 180000; // 3 minutes
    private static final int SECOND_INTERVAL_MS = 1000;
    private static final int BLINK_DURATION_MS = 300;

    private Timer gameTimer;
    private Timer secondsTimer;
    private Timer blinkTimer;

    /**
     * Starts the game duration timer.
     *
     * @param onExpire callback when game time expires
     */
    public void startGameTimer(Runnable onExpire) {
        stopGameTimer();
        System.out.println("Starting game timer for " + (GAME_DURATION_MS / 1000) + " seconds");
        gameTimer = new Timer(GAME_DURATION_MS, e -> {
            System.out.println("Game timer expired!");
            onExpire.run();
        });
        gameTimer.setRepeats(false);
        gameTimer.start();
    }

    /**
     * Starts the seconds counter timer.
     *
     * @param onTick callback on each second
     */
    public void startSecondsTimer(Runnable onTick) {
        stopSecondsTimer();
        secondsTimer = new Timer(SECOND_INTERVAL_MS, e -> onTick.run());
        secondsTimer.start();
    }

    /**
     * Starts the blink timer for error indication.
     *
     * @param onBlink callback when blink duration expires
     */
    public void startBlinkTimer(Runnable onBlink) {
        stopBlinkTimer();
        blinkTimer = new Timer(BLINK_DURATION_MS, e -> onBlink.run());
        blinkTimer.setRepeats(false);
        blinkTimer.start();
    }

    /**
     * Stops the game duration timer.
     */
    public void stopGameTimer() {
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
    }

    /**
     * Stops the seconds counter timer.
     */
    public void stopSecondsTimer() {
        if (secondsTimer != null && secondsTimer.isRunning()) {
            secondsTimer.stop();
        }
    }

    /**
     * Stops the blink timer.
     */
    public void stopBlinkTimer() {
        if (blinkTimer != null && blinkTimer.isRunning()) {
            blinkTimer.stop();
        }
    }

    /**
     * Stops all active timers.
     */
    public void stopAll() {
        stopGameTimer();
        stopSecondsTimer();
        stopBlinkTimer();
    }
}
