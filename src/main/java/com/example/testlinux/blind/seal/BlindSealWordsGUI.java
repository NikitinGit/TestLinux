package com.example.testlinux.blind.seal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Main GUI class for the Blind Typing Game.
 * Refactored to follow SOLID principles:
 * - Acts as a coordinator/facade for game components
 * - Delegates responsibilities to specialized classes
 * - Implements GameEventListener for loose coupling
 * - Follows Dependency Inversion Principle by depending on abstractions
 */
public class BlindSealWordsGUI extends JFrame implements GameEventListener {

    // Domain components (business logic layer)
    private final GameState gameState;
    private final TypingStatistics statistics;
    private final WordProvider wordProvider;
    private final GameTimer gameTimer;
    private final InputProcessor inputProcessor;

    // Presentation component (UI layer)
    private final UIComponents uiComponents;

    // State flags
    private boolean statisticsShown;
    private boolean timerStarted;

    /**
     * Constructs the main game window and initializes all components.
     * Follows Dependency Injection pattern.
     */
    public BlindSealWordsGUI() {
        // Initialize domain layer components
        this.gameState = new GameState();
        this.statistics = new TypingStatistics();
        this.wordProvider = new WordProvider();
        this.gameTimer = new GameTimer();

        // Initialize input processor with dependencies
        this.inputProcessor = new InputProcessor(gameState, statistics, wordProvider, this);

        // Initialize UI components
        this.uiComponents = new UIComponents();

        // Setup frame
        setupFrame();
        setupUI();
        setupKeyListener();

        setVisible(true);
    }

    /**
     * Configures the main JFrame properties.
     */
    private void setupFrame() {
        setTitle("Слепая печать - Слова");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout(10, 10));
    }

    /**
     * Sets up the UI components and layout.
     */
    private void setupUI() {
        JPanel mainPanel = uiComponents.createMainPanel();
        add(mainPanel, BorderLayout.CENTER);

        // Configure start button action
        uiComponents.getStartButton().addActionListener(e -> resetGame());

        // Configure show statistics button action
        uiComponents.getShowStatsButton().addActionListener(e -> showStatistics());
    }

    /**
     * Sets up keyboard input listener.
     */
    private void setupKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyPress(e);
            }
        });

        setFocusable(true);
        requestFocus();
    }

    /**
     * Handles keyboard input events.
     *
     * @param e the key event
     */
    private void handleKeyPress(KeyEvent e) {
        char inputChar = e.getKeyChar();

        // Handle space to start game when not active (only if statistics are shown)
        if (!gameState.isActive() && inputChar == KeyEvent.VK_SPACE && statisticsShown) {
            resetGame();
            return;
        }

        // Handle ESC to end game
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE && gameState.isActive()) {
            endGame();
            return;
        }

        // Start timers on first character input
        if (gameState.isActive() && !timerStarted && inputChar != KeyEvent.CHAR_UNDEFINED) {
            startTimers();
            timerStarted = true;
        }

        // Process character input
        if (inputChar != KeyEvent.CHAR_UNDEFINED) {
            inputProcessor.processCharacter(inputChar);
        }
    }

    /**
     * Starts the game timers.
     */
    private void startTimers() {
        // Start game timer (60 seconds)
        gameTimer.startGameTimer(this::endGame);

        // Start seconds counter
        gameTimer.startSecondsTimer(this::onSecondElapsed);
    }

    /**
     * Resets the game to initial state and prepares for a new session.
     */
    private void resetGame() {
        // Stop all timers
        gameTimer.stopAll();

        // Reset domain state
        statistics.reset();
        gameState.setActive(true);
        statisticsShown = false;
        timerStarted = false;

        // Update UI
        uiComponents.showGameStartState();

        // Generate first word (but don't start timers yet)
        inputProcessor.generateNewWord();

        requestFocus();
    }

    /**
     * Ends the current game session.
     */
    private void endGame() {
        gameState.setActive(false);
        gameTimer.stopAll();

        // Show game end state with "Show Statistics" button
        uiComponents.showGameEndState();
        statisticsShown = false;
    }

    /**
     * Shows final statistics after user clicks the button.
     */
    private void showStatistics() {
        String finalResults = statistics.getFormattedFinalResults();
        uiComponents.showFinalStatistics(finalResults);
        statisticsShown = true;
    }

    // ========== GameEventListener Implementation ==========

    @Override
    public void onCorrectChar() {
        // Update word display with current progress
        uiComponents.updateWordDisplay(gameState.getCurrentWord(), gameState.getTypedWord());

        // Update statistics display
        uiComponents.updateStats(statistics.getFormattedStats());
    }

    @Override
    public void onIncorrectChar() {
        // Show error indication
        uiComponents.highlightError();

        // Start blink timer to reset color
        gameTimer.startBlinkTimer(uiComponents::resetWordColor);

        // Update statistics display
        uiComponents.updateStats(statistics.getFormattedStats());
    }

    @Override
    public void onWordCompleted() {
        // Word completion is handled by generating new word
        // Statistics are already updated in InputProcessor
    }

    @Override
    public void onSecondElapsed() {
        statistics.incrementElapsedSeconds();
        uiComponents.updateTimer(statistics.getElapsedSeconds());
    }

    @Override
    public void onGameEnd() {
        endGame();
    }

    @Override
    public void onNewWord(String word) {
        uiComponents.showNewWord(word);
    }

    // ========== Application Entry Point ==========

    /**
     * Application entry point.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(BlindSealWordsGUI::new);
    }
}
