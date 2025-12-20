package com.example.testlinux.blind.seal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BlindSealWordsGUI extends JFrame {

    static final String[] words = {"нищий", "тень", "меч", "честь", "семь", "нить", "щит", "кит", "куст", "сеть", "месть", "мешки", "счет", "цех", "шум", "шут", "учет", "зуб", "куб", "тушь", "щи", "тишь", "мишень", "цемент"};

    private JLabel instructionLabel;
    private JLabel timerLabel;
    private JLabel wordLabel;
    private JLabel statsLabel;
    private JButton startButton;

    private String currentWord;
    private StringBuilder typedWord;
    private int countAllWords = 0;
    private int countWrongWords = 0;
    private int elapsedSeconds = 0;
    private boolean gameActive = false;

    private Timer gameTimer;
    private Timer blinkTimer;
    private Timer secondsTimer;

    public BlindSealWordsGUI() {
        setTitle("Слепая печать - Слова");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLayout(new BorderLayout(10, 10));

        initComponents();
        setupKeyListener();

        setVisible(true);
    }

    private void initComponents() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        instructionLabel = new JLabel("Нажмите ESC для выхода");
        instructionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        instructionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        timerLabel = new JLabel("Время: 0 сек", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Arial", Font.BOLD, 20));
        timerLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        timerLabel.setForeground(new Color(0, 102, 204));

        wordLabel = new JLabel("", SwingConstants.CENTER);
        wordLabel.setFont(new Font("Arial", Font.BOLD, 72));
        wordLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        wordLabel.setPreferredSize(new Dimension(400, 150));

        statsLabel = new JLabel("Нажмите кнопку СТАРТ для начала игры", SwingConstants.CENTER);
        statsLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        statsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        startButton = new JButton("СТАРТ");
        startButton.setFont(new Font("Arial", Font.BOLD, 24));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setPreferredSize(new Dimension(200, 60));
        startButton.setMaximumSize(new Dimension(200, 60));
        startButton.setFocusable(false);
        startButton.addActionListener(e -> resetGame());

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

        add(mainPanel, BorderLayout.CENTER);
    }

    private void setupKeyListener() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                char inputChar = e.getKeyChar();

                if (!gameActive) {
                    if (inputChar == KeyEvent.VK_SPACE) {
                        System.out.println("inputChar == KeyEvent.VK_SPACE && countAllWords == 0");
                        resetGame();
                    }
                    return;
                }

                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    endGame();
                    return;
                }

                if (inputChar == KeyEvent.CHAR_UNDEFINED) return;

                processInput(inputChar);
            }
        });

        setFocusable(true);
        requestFocus();
    }

    private void startGame() {
        generateNewWord();

        gameTimer = new Timer(60000, e -> endGame());
        gameTimer.setRepeats(false);
        gameTimer.start();

        secondsTimer = new Timer(1000, e -> {
            elapsedSeconds++;
            updateTimerDisplay();
        });
        secondsTimer.start();
    }

    private void updateTimerDisplay() {
        timerLabel.setText("Время: " + elapsedSeconds + " сек");
    }

    private void generateNewWord() {
        currentWord = words[(int) (Math.random() * words.length)];
        typedWord = new StringBuilder();
        wordLabel.setText("Введите слово: " + currentWord);
        wordLabel.setForeground(Color.BLACK);
    }

    private void processInput(char inputChar) {
        if (inputChar == KeyEvent.VK_BACK_SPACE) {
            if (typedWord.length() > 0) {
                typedWord.deleteCharAt(typedWord.length() - 1);
                updateWordDisplay();
            }
            return;
        }

        typedWord.append(inputChar);
        updateWordDisplay();

        if (typedWord.length() == currentWord.length()) {
            countAllWords++;

            if (typedWord.toString().equals(currentWord)) {
                generateNewWord();
                updateStats();
            } else {
                countWrongWords++;
                blinkRed();
                updateStats();
                generateNewWord();
            }
        }
    }

    private void updateWordDisplay() {
        String display = "Введите слово: " + currentWord + "<br>Набрано: " + typedWord.toString();
        wordLabel.setText("<html><center>" + display + "</center></html>");
    }

    private void blinkRed() {
        wordLabel.setForeground(Color.RED);

        if (blinkTimer != null && blinkTimer.isRunning()) {
            blinkTimer.stop();
        }

        blinkTimer = new Timer(300, e -> {
            wordLabel.setForeground(Color.BLACK);
        });
        blinkTimer.setRepeats(false);
        blinkTimer.start();
    }

    private void updateStats() {
        double accuracy = 100 * (1 - ((double) countWrongWords / countAllWords));
        statsLabel.setText(String.format("Всего слов: %d | Ошибок: %d | Точность: %.1f%%",
                countAllWords, countWrongWords, accuracy));
    }

    private void resetGame() {
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }
        if (secondsTimer != null && secondsTimer.isRunning()) {
            secondsTimer.stop();
        }

        countAllWords = 0;
        countWrongWords = 0;
        elapsedSeconds = 0;
        gameActive = true;

        timerLabel.setText("Время: 0 сек");
        wordLabel.setFont(new Font("Arial", Font.BOLD, 72));
        startButton.setVisible(false);

        startGame();
        requestFocus();
    }

    private void endGame() {
        gameActive = false;
        if (gameTimer != null) {
            gameTimer.stop();
        }
        if (secondsTimer != null && secondsTimer.isRunning()) {
            secondsTimer.stop();
        }

        double accuracy = countAllWords > 0 ? 100 * (1 - ((double) countWrongWords / countAllWords)) : 0;

        wordLabel.setText("Игра завершена!");
        wordLabel.setFont(new Font("Arial", Font.BOLD, 48));
        statsLabel.setText(String.format("<html><center>Набрано всего слов: %d<br>Набрано неверных слов: %d<br>Процент попадания: %.2f%%</center></html>",
                countAllWords, countWrongWords, accuracy));

        startButton.setVisible(true);
        startButton.setText("ПЕРЕЗАПУСК");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BlindSealWordsGUI::new);
    }
}
