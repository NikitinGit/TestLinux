package com.example.testlinux.blind.seal;

import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.NonBlockingReader;

import java.io.IOException;

public class BlindSeal {

    static final char[] symbols = new char[]{'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з', 'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ', 'ъ', 'ы', 'ь', 'э', 'ю', 'я'};

    public static void main(String[] args) throws IOException, InterruptedException {

        var timeBegin = System.currentTimeMillis();
        var countAllSymbols = 0;
        var countWrongSymbols = 0;

        try (Terminal terminal = TerminalBuilder.builder()
                .system(true)
                .build()) {

            terminal.enterRawMode();
            NonBlockingReader reader = terminal.reader();

            terminal.writer().println("5 December Нажмите Backspace для выхода\n");
            terminal.flush();

            char currentSymbol = 'b';
            boolean isRight = true;

            while (true) {
                if (isRight) {
                    currentSymbol = symbols[(int)(Math.random() * symbols.length)];
                }
                terminal.writer().print("Введите символ: " + currentSymbol + " ");
                terminal.flush();

                int read = reader.read();
                if (read == -1) break;

                char inputChar = (char) read;

/*                // Backspace (127 или 8) или Ctrl+C (3) или Escape (27)
                if (inputChar == 127 || inputChar == 8 || inputChar == 3 || inputChar == 27) {
                    terminal.writer().println("\n\nВыход из программы.");
                    break;
                }*/


                countAllSymbols++;

                if (inputChar == currentSymbol) {
                    terminal.writer().println(" → Правильно!");
                    isRight = true;
                } else {
                    countWrongSymbols++;
                    // Мигание красным
                    terminal.writer().print("\r\u001B[31mВведите символ: " + currentSymbol + " \u001B[0m");
                    terminal.flush();
                    Thread.sleep(300);
                    terminal.writer().print("\r");
                    isRight = false;
                }
                terminal.flush();

                if (System.currentTimeMillis() - timeBegin > 60000) {
                    terminal.writer().print("Набрано всего символов: " + countAllSymbols + "\n");
                    terminal.writer().print("Набрано не верных символов: " + countWrongSymbols + "\n");
                    terminal.writer().print("Процент попадания: " + 100 * (1 - ((double)(countWrongSymbols) / countAllSymbols)) + " %");
                    terminal.flush();
                    break;
                }
            }
        }
    }
}
