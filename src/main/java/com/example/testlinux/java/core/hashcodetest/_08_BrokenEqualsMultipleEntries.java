package com.example.testlinux.java.core.hashcodetest;

import java.util.HashMap;

/**
 * Когда сломан equals — HashMap отдаёт "чужое" значение.
 *
 * Здесь специально показано, что в бакете лежат НЕСКОЛЬКО разных entry
 * (а не одна, как было бы при тотально-сломанном equals == true).
 *
 * Условия, при которых появляется ложно-положительный get:
 *   1) Несколько ключей попадают в один бакет (одинаковая формула индекса).
 *   2) put не считает их дубликатами  -> ступень 1 (hash) отсекает на этапе вставки.
 *   3) get для НЕЗНАКОМОГО ключа находит "случайный" узел,
 *      потому что equals лжёт на каком-то конкретном поле, которое случайно сошлось.
 *
 * Сломанный equals здесь — классический реальный баг:
 *   "equals по неидентифицирующему полю" (тут — по hashValue вместо id).
 *   Это эквивалентно ошибке: IDE сгенерировал equals по полю createdAt,
 *   а должен был — по id.
 */
public class _08_BrokenEqualsMultipleEntries {

    /** Сломанный ключ: equals смотрит ТОЛЬКО на hashValue, игнорирует id. */
    static final class BrokenEqualsKey {
        final String id;
        final int hashValue;

        BrokenEqualsKey(String id, int hashValue) {
            this.id = id;
            this.hashValue = hashValue;
        }

        @Override
        public int hashCode() {
            return hashValue;
        }

        /** ⚠ СЛОМАНО: сравнивает hashValue вместо идентифицирующего поля id. */
        @Override
        public boolean equals(Object o) {
            return o instanceof BrokenEqualsKey k && hashValue == k.hashValue;
        }

        @Override
        public String toString() {
            return id + "(h=" + hashValue + ")";
        }
    }

    /** Эталонный ключ — equals по id. */
    static final class CorrectKey {
        final String id;
        final int hashValue;

        CorrectKey(String id, int hashValue) {
            this.id = id;
            this.hashValue = hashValue;
        }

        @Override
        public int hashCode() {
            return hashValue;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof CorrectKey k && id.equals(k.id);
        }

        @Override
        public String toString() {
            return id + "(h=" + hashValue + ")";
        }
    }

    public static void main(String[] args) {
        // -----------------------------------------------------------------
        // 1) Сломанный equals + несколько entry в одном бакете
        // -----------------------------------------------------------------
        // capacity=16, mask = 15. Все хеши 1, 17, 33 дают бакет 1 (1 & 15 == 17 & 15 == 33 & 15 == 1).
        HashMap<BrokenEqualsKey, String> broken = new HashMap<>(16);
        broken.put(new BrokenEqualsKey("alpha", 1),  "A");
        broken.put(new BrokenEqualsKey("beta",  17), "B");
        broken.put(new BrokenEqualsKey("carl",  33), "C");

        // put НЕ считает их дубликатами — на этапе вставки hash-проверка (n.hash == h) отсекает.
        // alpha.hash=1, beta.hash=17, carl.hash=33 — разные числа, equals даже не вызывается.
        System.out.println("=== Сломанный equals (по hashValue вместо id) ===");
        System.out.println("size = " + broken.size());        // 3 — все три entry лежат

        // Запрашиваем НИКОГДА не положенный ключ "gamma", но с тем же hashValue, что у beta
        String fromGamma = broken.get(new BrokenEqualsKey("gamma", 17));
        System.out.println("get(gamma, h=17) = " + fromGamma); // "B" — чужое значение!

        String fromDelta = broken.get(new BrokenEqualsKey("delta", 33));
        System.out.println("get(delta, h=33) = " + fromDelta); // "C" — чужое значение!

        // Зато если hash не совпал ни с одним узлом — вернётся null (ступень 1 отсекла всех)
        String fromZeta = broken.get(new BrokenEqualsKey("zeta", 99));
        System.out.println("get(zeta,  h=99) = " + fromZeta);  // null

        // -----------------------------------------------------------------
        // 2) Тот же сценарий, но с правильным equals -> всё работает корректно
        // -----------------------------------------------------------------
        HashMap<CorrectKey, String> correct = new HashMap<>(16);
        correct.put(new CorrectKey("alpha", 1),  "A");
        correct.put(new CorrectKey("beta",  17), "B");
        correct.put(new CorrectKey("carl",  33), "C");

        System.out.println("\n=== Правильный equals (по id) ===");
        System.out.println("size = " + correct.size());                          // 3

        // Запрашиваем по id "gamma" с тем же hash, что у beta — id не совпадает -> null
        System.out.println("get(gamma, h=17) = "
                + correct.get(new CorrectKey("gamma", 17)));                      // null
        System.out.println("get(delta, h=33) = "
                + correct.get(new CorrectKey("delta", 33)));                      // null
        // А запрос с правильным id находит свою entry
        System.out.println("get(beta,  h=17) = "
                + correct.get(new CorrectKey("beta",  17)));                      // B

        // -----------------------------------------------------------------
        // 3) Пошагово: что именно делает HashMap при сломанном get(gamma, 17)
        // -----------------------------------------------------------------
        System.out.println("\n=== Трассировка get(gamma, h=17) для сломанного варианта ===");
        BrokenEqualsKey gamma = new BrokenEqualsKey("gamma", 17);
        int h = gamma.hashCode();
        int spread = h ^ (h >>> 16);
        int bucket = spread & (16 - 1);
        System.out.println("hash=" + h + ", spread=" + spread + ", bucket=" + bucket);
        System.out.println("узел alpha: n.hash=1   == h=17 ? false -> skip");
        System.out.println("узел beta:  n.hash=17  == h=17 ? true  -> equals(gamma, beta) = (17 == 17) = true -> return \"B\"");
    }
}