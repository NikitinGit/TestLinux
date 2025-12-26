package com.example.testlinux.blind.seal;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

/**
 * Manages and updates all UI components of the game.
 * Follows Single Responsibility Principle - manages only UI presentation logic.
 */
public class UIComponents {
    private final JLabel instructionLabel;
    private final JLabel timerLabel;
    private final JLabel wordLabel;
    private final JLabel statsLabel;
    @Getter
    private final JButton startButton;
    @Getter
    private final JButton showStatsButton;
    @Getter
    private final JButton wordsButton;
    @Getter
    private final JButton lettersButton;
    private final JPanel modeSelectionPanel;

    public UIComponents() {
        this.instructionLabel = createInstructionLabel();
        this.timerLabel = createTimerLabel();
        this.wordLabel = createWordLabel();
        this.statsLabel = createStatsLabel();
        this.startButton = createStartButton();
        this.showStatsButton = createShowStatsButton();
        this.wordsButton = createWordsButton();
        this.lettersButton = createLettersButton();
        this.modeSelectionPanel = createModeSelectionPanel();
    }

    private JLabel createInstructionLabel() {
        JLabel label = new JLabel("Нажмите ESC для выхода");
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JLabel createTimerLabel() {
        JLabel label = new JLabel("Время: 0 сек", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setForeground(new Color(0, 102, 204));
        return label;
    }

    private JLabel createWordLabel() {
        JLabel label = new JLabel("", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 72));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setPreferredSize(new Dimension(400, 350));
        return label;
    }

    private JLabel createStatsLabel() {
        JLabel label = new JLabel("Нажмите кнопку СТАРТ для начала игры", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 28));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JButton createStartButton() {
        JButton button = new JButton("СТАРТ");
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(200, 60));
        button.setMaximumSize(new Dimension(200, 60));
        button.setFocusable(false);
        return button;
    }

    private JButton createShowStatsButton() {
        JButton button = new JButton("ПОКАЗАТЬ СТАТИСТИКУ");
        button.setFont(new Font("Arial", Font.BOLD, 20));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(300, 60));
        button.setMaximumSize(new Dimension(300, 60));
        button.setFocusable(false);
        button.setVisible(false);
        return button;
    }

    private JButton createWordsButton() {
        JButton button = new JButton("СЛОВА");
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setPreferredSize(new Dimension(200, 60));
        button.setFocusable(false);
        return button;
    }

    private JButton createLettersButton() {
        JButton button = new JButton("БУКВЫ");
        button.setFont(new Font("Arial", Font.BOLD, 24));
        button.setPreferredSize(new Dimension(200, 60));
        button.setFocusable(false);
        return button;
    }

    private JPanel createModeSelectionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
        panel.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.setBackground(Color.WHITE);
        panel.add(wordsButton);
        panel.add(Box.createRigidArea(new Dimension(20, 0)));
        panel.add(lettersButton);
        return panel;
    }

    /**
     * Creates and returns the main panel with all components.
     *
     * @return configured main panel
     */
    public JPanel createMainPanel() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(instructionLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(timerLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(wordLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 80)));
        mainPanel.add(statsLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(showStatsButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(modeSelectionPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(startButton);
        mainPanel.add(Box.createVerticalGlue());

        return mainPanel;
    }

    /**
     * Updates the timer display.
     *
     * @param seconds elapsed seconds
     */
    public void updateTimer(int seconds) {
        timerLabel.setText("Время: " + seconds + " сек");
    }

    /**
     * Updates the word display showing current word and typed characters.
     *
     * @param currentWord the word to type
     * @param typedWord   characters typed so far
     * @param mode        game mode (WORDS or LETTERS)
     */
    public void updateWordDisplay(String currentWord, String typedWord, GameMode mode) {
        String label = mode == GameMode.LETTERS ? "Введите букву:" : "Введите слово:";
        String display = "<html><table>" +
                "<tr><td style='width:150px; text-align:right;'>" + label + "&nbsp;</td>" +
                "<td style='width:250px; text-align:left;'>" + currentWord + "</td></tr>" +
                "<tr><td style='width:150px; text-align:right;'>Набрано:&nbsp;</td>" +
                "<td style='width:250px; text-align:left;'>" + typedWord + "</td></tr>" +
                "</table></html>";
        wordLabel.setText(display);
        wordLabel.setForeground(Color.BLACK);
    }

    /**
     * Shows a new word to type.
     *
     * @param word the new word
     * @param mode game mode (WORDS or LETTERS)
     */
    public void showNewWord(String word, GameMode mode) {
        String label = mode == GameMode.LETTERS ? "Введите букву:" : "Введите слово:";
        String display = "<html><table>" +
                "<tr><td style='width:150px; text-align:right;'>" + label + "&nbsp;</td>" +
                "<td style='width:250px; text-align:left;'>" + word + "</td></tr>" +
                "<tr><td style='width:150px; text-align:right;'>Набрано:&nbsp;</td>" +
                "<td style='width:250px; text-align:left;'></td></tr>" +
                "</table></html>";
        wordLabel.setText(display);
        wordLabel.setForeground(Color.BLACK);
    }

    /**
     * Updates statistics display.
     *
     * @param formattedStats formatted statistics string
     */
    public void updateStats(String formattedStats) {
        statsLabel.setText(formattedStats);
    }

    /**
     * Highlights the word label in red for error indication.
     */
    public void highlightError() {
        wordLabel.setForeground(Color.RED);
    }

    /**
     * Resets word label color to black.
     */
    public void resetWordColor() {
        wordLabel.setForeground(Color.BLACK);
    }

    /**
     * Shows game start state.
     */
    public void showGameStartState() {
        timerLabel.setText("Время: 0 сек");
        wordLabel.setFont(new Font("Arial", Font.BOLD, 72));
        startButton.setVisible(false);
    }

    /**
     * Shows game end state with "Show Statistics" button.
     */
    public void showGameEndState() {
        wordLabel.setText("Игра завершена!");
        wordLabel.setFont(new Font("Arial", Font.BOLD, 48));
        statsLabel.setText("");
        showStatsButton.setVisible(true);
        startButton.setVisible(false);
    }

    /**
     * Shows final statistics and restart button.
     *
     * @param finalResults formatted final results string
     */
    public void showFinalStatistics(String finalResults) {
        statsLabel.setText(finalResults);
        showStatsButton.setVisible(false);
        startButton.setVisible(false);
        modeSelectionPanel.setVisible(true);
    }

    /**
     * Shows mode selection buttons and hides start button.
     */
    public void showModeSelection() {
        modeSelectionPanel.setVisible(true);
        startButton.setVisible(false);
        statsLabel.setText("Выберите режим игры");
    }

    /**
     * Hides mode selection buttons and shows start button.
     */
    public void hideModeSelection() {
        modeSelectionPanel.setVisible(false);
        startButton.setVisible(true);
    }

}
