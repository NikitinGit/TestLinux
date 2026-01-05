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
    private boolean gameJustEnded;

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

        // Configure mode selection buttons
        uiComponents.getWordsButton().addActionListener(e -> selectMode(GameMode.WORDS));
        uiComponents.getLettersButton().addActionListener(e -> selectMode(GameMode.LETTERS));
        uiComponents.getMultipleWordsButton().addActionListener(e -> selectMode(GameMode.MULTIPLE_WORDS));

        // Show mode selection on startup
        uiComponents.showModeSelection();
    }

    /**
     * Handles mode selection by user.
     *
     * @param mode selected game mode
     */
    private void selectMode(GameMode mode) {
        wordProvider.setGameMode(mode);

        // For LETTERS mode, ask user to input the initial letter
        if (mode == GameMode.LETTERS) {
            String initialLetter = showCustomInputDialog(
                "Введите префикс буквы:",
                "Выбор префикса буквы",
                new Color(240, 248, 255), // Alice Blue background
                new Color(0, 5, 5),        // Message text color
                400,                       // Width
                200,                       // Height
                15,                        // Input field width (columns)
                24,                        // Input text font size
                new Color(0, 0, 0)         // Input text color (black)
            );



            if (initialLetter != null && !initialLetter.isEmpty()) {
                wordProvider.setInitialSymbol(initialLetter.toLowerCase());
            } else {
                // If user cancelled or didn't enter anything, go back to mode selection
                uiComponents.showModeSelection();
                return;
            }
        }

        uiComponents.hideModeSelection();
        uiComponents.updateStats("Нажмите кнопку СТАРТ для начала игры");
        setTitle("Слепая печать - " + mode.getDisplayName());
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

        // Handle space to start game when not active (except right after game ends)
        if (!gameState.isActive() && inputChar == KeyEvent.VK_SPACE && !gameJustEnded) {
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
        gameJustEnded = false;

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
        gameJustEnded = true;
    }

    /**
     * Shows final statistics after user clicks the button.
     */
    private void showStatistics() {
        String finalResults = statistics.getFormattedFinalResults();
        uiComponents.showFinalStatistics(finalResults);
        statisticsShown = true;
        gameJustEnded = false;
    }

    // ========== GameEventListener Implementation ==========

    @Override
    public void onCorrectChar() {
        // Update word display with current progress
        if (wordProvider.getGameMode() == GameMode.MULTIPLE_WORDS) {
            String[] nextWords = wordProvider.getPreviewWords(2);
            uiComponents.showMultipleWords(gameState.getCurrentWord(), nextWords, gameState.getTypedWord());
        } else {
            uiComponents.updateWordDisplay(gameState.getCurrentWord(), gameState.getTypedWord(), wordProvider.getGameMode());
        }

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
        if (wordProvider.getGameMode() == GameMode.MULTIPLE_WORDS) {
            String[] nextWords = wordProvider.getPreviewWords(2);
            uiComponents.showMultipleWords(word, nextWords, "");
        } else {
            uiComponents.showNewWord(word, wordProvider.getGameMode());
        }
    }

    // ========== Helper Methods ==========

    /**
     * Shows a custom input dialog with specified colors and size.
     *
     * @param message           the message to display
     * @param title             the dialog title
     * @param bgColor           background color
     * @param textColor         text color
     * @param width             dialog width
     * @param height            dialog height
     * @param inputFieldColumns width of input field in columns
     * @param inputFontSize     font size of input text
     * @param inputTextColor    color of input text
     * @return the user input or null if cancelled
     */
    private String showCustomInputDialog(String message, String title, Color bgColor, Color textColor, int width, int height, int inputFieldColumns, int inputFontSize, Color inputTextColor) {
        JDialog dialog = new JDialog(this, title, true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(bgColor);
        dialog.setPreferredSize(new Dimension(width, height));

        // Create message label
        JLabel messageLabel = new JLabel(message);
        messageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        messageLabel.setForeground(textColor);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        // Create input field
        JTextField inputField = new JTextField(inputFieldColumns);
        inputField.setFont(new Font("Arial", Font.PLAIN, inputFontSize));
        inputField.setForeground(inputTextColor);
        inputField.setHorizontalAlignment(JTextField.CENTER);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(bgColor);
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Отмена");

        okButton.setFont(new Font("Arial", Font.BOLD, 14));
        cancelButton.setFont(new Font("Arial", Font.BOLD, 14));

        final String[] result = {null};

        okButton.addActionListener(e -> {
            result[0] = inputField.getText();
            dialog.dispose();
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        inputField.addActionListener(e -> {
            result[0] = inputField.getText();
            dialog.dispose();
        });

        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);

        // Create input panel
        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(bgColor);
        inputPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 10, 20));
        inputPanel.add(inputField);

        // Add components to dialog
        dialog.add(messageLabel, BorderLayout.NORTH);
        dialog.add(inputPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.pack();
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);

        return result[0];
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
