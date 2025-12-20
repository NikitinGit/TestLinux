package com.example.testlinux.blind.seal;

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
    private final JButton startButton;

    public UIComponents() {
        this.instructionLabel = createInstructionLabel();
        this.timerLabel = createTimerLabel();
        this.wordLabel = createWordLabel();
        this.statsLabel = createStatsLabel();
        this.startButton = createStartButton();
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
        label.setPreferredSize(new Dimension(400, 150));
        return label;
    }

    private JLabel createStatsLabel() {
        JLabel label = new JLabel("Нажмите кнопку СТАРТ для начала игры", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
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
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(statsLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
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
     */
    public void updateWordDisplay(String currentWord, String typedWord) {
        String display = "Введите слово: " + currentWord + "<br>Набрано: " + typedWord;
        wordLabel.setText("<html><center>" + display + "</center></html>");
        wordLabel.setForeground(Color.BLACK);
    }

    /**
     * Shows a new word to type.
     *
     * @param word the new word
     */
    public void showNewWord(String word) {
        wordLabel.setText("Введите слово: " + word);
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
     * Shows game end state.
     *
     * @param finalResults formatted final results string
     */
    public void showGameEndState(String finalResults) {
        wordLabel.setText("Игра завершена!");
        wordLabel.setFont(new Font("Arial", Font.BOLD, 48));
        statsLabel.setText(finalResults);
        startButton.setVisible(true);
        startButton.setText("ПЕРЕЗАПУСК");
    }

    public JButton getStartButton() {
        return startButton;
    }
}
