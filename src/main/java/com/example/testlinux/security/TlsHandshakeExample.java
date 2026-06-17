package com.example.testlinux.security;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.SSLSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

/**
 * РЕАЛЬНЫЙ TLS handshake через JSSE (Java Secure Socket Extension).
 *
 * Что показывает (привязка к памятке Hash-функции.md, раздел HTTPS/TLS):
 *   1) согласованный ПРОТОКОЛ (TLSv1.2 / TLSv1.3);
 *   2) согласованный CIPHER SUITE (шифронабор) — какие алгоритмы выбрали стороны;
 *   3) ЦЕПОЧКУ СЕРТИФИКАТОВ сервера: subject, issuer, алгоритм подписи (SHA256withRSA...),
 *      тип открытого ключа (RSA / EC).
 *
 * Поток HTTPS целиком:
 *   - АСИММЕТРИЯ (ECDHE + RSA/ECDSA) работает ТОЛЬКО в рукопожатии — договориться о ключе
 *     и проверить сертификат;
 *   - дальше трафик шифруется СИММЕТРИКОЙ (AES-GCM / ChaCha20) из cipher suite.
 *
 * Полезный флаг для наблюдения за рукопожатием на уровне пакетов:
 *   -Djavax.net.debug=ssl:handshake
 */
public class TlsHandshakeExample {

    private static final String HOST = "example.com";
    private static final int PORT = 443;

    public static void main(String[] args) throws Exception {
        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();

        try (SSLSocket socket = (SSLSocket) factory.createSocket(HOST, PORT)) {

            // Подписываемся на событие "рукопожатие завершено" — сработает в шаге startHandshake()
            socket.addHandshakeCompletedListener(event ->
                    System.out.println(">> Событие: handshake завершён, cipher = "
                            + event.getCipherSuite()));

            // Что МЫ готовы предложить серверу (включено в нашей JVM)
            System.out.println("=== Что предлагает клиент (мы) ===");
            System.out.println("Поддерживаемые протоколы: " + String.join(", ", socket.getEnabledProtocols()));
            System.out.println("Кол-во предлагаемых cipher suites: " + socket.getEnabledCipherSuites().length);
            System.out.println();

            // Явно запускаем TLS handshake (иначе он стартует лениво при первом read/write)
            System.out.println(">> Запускаем TLS handshake с " + HOST + ":" + PORT + " ...");
            socket.startHandshake();

            // После рукопожатия можно прочитать ИТОГ из SSLSession
            SSLSession session = socket.getSession();
            printSessionInfo(session);
            printCertificates(session);

            // Доказываем, что канал рабочий: шлём HTTP-запрос поверх уже зашифрованного соединения
            sendHttpRequest(socket);
        }
    }

    /** Шаг 1–3 итог: какой протокол и шифронабор реально согласовали. */
    private static void printSessionInfo(SSLSession session) {
        System.out.println("\n=== ИТОГ рукопожатия (SSLSession) ===");
        System.out.println("Протокол    : " + session.getProtocol());        // напр. TLSv1.3
        System.out.println("Cipher suite: " + session.getCipherSuite());     // напр. TLS_AES_256_GCM_SHA384
        // Разбор имени cipher suite на части (для TLS 1.2 видны все, для 1.3 — только шифр+хеш)
        explainCipherSuite(session.getCipherSuite());
    }

    /** Раскладываем имя шифронабора на задачи: обмен ключей / подпись / шифр / хеш. */
    private static void explainCipherSuite(String suite) {
        System.out.println("  разбор имени:");
        if (suite.contains("ECDHE")) System.out.println("    ECDHE         -> обмен ключами (асимметрия, forward secrecy)");
        if (suite.contains("_RSA"))  System.out.println("    RSA           -> подпись/аутентификация сервера (асимметрия)");
        if (suite.contains("ECDSA")) System.out.println("    ECDSA         -> подпись/аутентификация сервера (асимметрия)");
        if (suite.contains("AES"))   System.out.println("    AES_*_GCM     -> шифрование трафика (СИММЕТРИЯ)");
        if (suite.contains("CHACHA20")) System.out.println("    CHACHA20      -> шифрование трафика (СИММЕТРИЯ)");
        if (suite.contains("SHA384")) System.out.println("    SHA384        -> хеш/целостность (вывод ключей, HMAC)");
        else if (suite.contains("SHA256")) System.out.println("    SHA256        -> хеш/целостность (вывод ключей, HMAC)");
    }

    /** Цепочка сертификатов сервера: subject, issuer, алгоритм подписи и тип ключа. */
    private static void printCertificates(SSLSession session) throws Exception {
        System.out.println("\n=== Сертификаты сервера (цепочка до корневого CA) ===");
        Certificate[] chain = session.getPeerCertificates();
        int i = 1;
        for (Certificate cert : chain) {
            if (cert instanceof X509Certificate x509) {
                System.out.println("  [" + i++ + "]");
                System.out.println("     subject      : " + x509.getSubjectX500Principal());
                System.out.println("     issuer       : " + x509.getIssuerX500Principal());
                System.out.println("     алгоритм подписи: " + x509.getSigAlgName());      // напр. SHA256withRSA
                System.out.println("     тип ключа    : " + x509.getPublicKey().getAlgorithm()
                        + " (" + keyBits(x509) + ")");                                       // RSA / EC
            }
        }
    }

    private static String keyBits(X509Certificate x509) {
        var key = x509.getPublicKey();
        if (key instanceof java.security.interfaces.RSAPublicKey rsa) {
            return rsa.getModulus().bitLength() + " бит";
        }
        if (key instanceof java.security.interfaces.ECPublicKey ec) {
            return "кривая " + ec.getParams().getCurve().getField().getFieldSize() + " бит";
        }
        return "?";
    }

    /** Поверх зашифрованного канала шлём обычный HTTP — данные уже идут под AES из cipher suite. */
    private static void sendHttpRequest(SSLSocket socket) throws Exception {
        System.out.println("\n=== Проверка канала: GET / поверх TLS ===");
        OutputStream out = socket.getOutputStream();
        PrintWriter writer = new PrintWriter(out, false, StandardCharsets.UTF_8);
        writer.print("GET / HTTP/1.1\r\n");
        writer.print("Host: " + HOST + "\r\n");
        writer.print("Connection: close\r\n");
        writer.print("\r\n");
        writer.flush();

        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {
            // печатаем только статусную строку — этого достаточно как доказательство
            System.out.println("Ответ сервера (первая строка): " + in.readLine());
        }
    }
}
