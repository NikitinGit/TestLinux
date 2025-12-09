package com.example.testlinux.blind.seal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class BlindSealGUI extends JFrame {

    static final char[] symbols = new char[]{'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я'};

    private JLabel instructionLabel;
    private JLabel symbolLabel;
    private JLabel statsLabel;
    private JButton startButton;

    private char currentSymbol;
    private int countAllSymbols = 0;
    private int countWrongSymbols = 0;
    private boolean gameActive = false;

    private Timer gameTimer;
    private Timer blinkTimer;

    public BlindSealGUI() {
        setTitle("Слепая печать");
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

        symbolLabel = new JLabel("", SwingConstants.CENTER);
        symbolLabel.setFont(new Font("Arial", Font.BOLD, 72));
        symbolLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        symbolLabel.setPreferredSize(new Dimension(200, 150));

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
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(symbolLabel);
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
                        System.out.println("inputChar == KeyEvent.VK_SPACE && countAllSymbols == 0");
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
        generateNewSymbol();

        gameTimer = new Timer(60000, e -> endGame());
        gameTimer.setRepeats(false);
        gameTimer.start();
    }

    private void generateNewSymbol() {
        currentSymbol = symbols[(int) (Math.random() * symbols.length)];
        symbolLabel.setText("Введите символ: " + currentSymbol);
        symbolLabel.setForeground(Color.BLACK);
    }

    private void processInput(char inputChar) {
        countAllSymbols++;

        if (inputChar == currentSymbol) {
            generateNewSymbol();
            updateStats();
        } else {
            countWrongSymbols++;
            blinkRed();
            updateStats();
        }
    }

    private void blinkRed() {
        symbolLabel.setForeground(Color.RED);

        if (blinkTimer != null && blinkTimer.isRunning()) {
            blinkTimer.stop();
        }

        blinkTimer = new Timer(300, e -> {
            symbolLabel.setForeground(Color.BLACK);
        });
        blinkTimer.setRepeats(false);
        blinkTimer.start();
    }

    private void updateStats() {
        double accuracy = 100 * (1 - ((double) countWrongSymbols / countAllSymbols));
        statsLabel.setText(String.format("Всего символов: %d | Ошибок: %d | Точность: %.1f%%",
                countAllSymbols, countWrongSymbols, accuracy));
    }

    private void resetGame() {
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
        }

        countAllSymbols = 0;
        countWrongSymbols = 0;
        gameActive = true;

        symbolLabel.setFont(new Font("Arial", Font.BOLD, 72));
        startButton.setVisible(false);

        startGame();
        requestFocus();
    }

    private void endGame() {
        gameActive = false;
        if (gameTimer != null) {
            gameTimer.stop();
        }

        double accuracy = countAllSymbols > 0 ? 100 * (1 - ((double) countWrongSymbols / countAllSymbols)) : 0;

        symbolLabel.setText("Игра завершена!");
        symbolLabel.setFont(new Font("Arial", Font.BOLD, 48));
        statsLabel.setText(String.format("<html><center>Набрано всего символов: %d<br>Набрано неверных символов: %d<br>Процент попадания: %.2f%%</center></html>",
                countAllSymbols, countWrongSymbols, accuracy));

        startButton.setVisible(true);
        startButton.setText("ПЕРЕЗАПУСК");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BlindSealGUI::new);
    }
}
