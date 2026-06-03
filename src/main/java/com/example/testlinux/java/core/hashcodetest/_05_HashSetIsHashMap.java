package com.example.testlinux.java.core.hashcodetest;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Objects;

/**
 * HashSet — это обёртка над HashMap.
 * Внутри HashSet есть private HashMap<E, Object> map, и единственный value-маркер PRESENT = new Object().
 *
 * Поэтому:
 *  - add(e)        == map.put(e, PRESENT) == null
 *  - contains(e)   == map.containsKey(e)
 *  - remove(e)     == map.remove(e) == PRESENT
 *
 * Значит ВСЁ, что мы обсуждали про HashMap, целиком относится и к HashSet:
 *  - индекс бакета считается из hashCode()
 *  - равенство решает equals()
 *  - коллизии разрулиются цепочкой/деревом
 *  - resize при превышении threshold
 *
 * LinkedHashSet / LinkedHashMap дополнительно поддерживают порядок вставки
 * через двусвязный список поверх тех же бакетов.
 */
public class _05_HashSetIsHashMap {

    static final class Word {
        final String text;
        Word(String text) { this.text = text; }
        @Override public int hashCode() { return Objects.hashCode(text); }
        @Override public boolean equals(Object o) {
            return o instanceof Word w && Objects.equals(text, w.text);
        }
        @Override public String toString() { return text; }
    }

    public static void main(String[] args) {
        HashSet<Word> set = new HashSet<>();
        System.out.println("add hello   = " + set.add(new Word("hello")));   // true
        System.out.println("add world   = " + set.add(new Word("world")));   // true
        System.out.println("add hello#2 = " + set.add(new Word("hello")));   // false — дубликат по equals
        System.out.println("contains(hello) = " + set.contains(new Word("hello")));
        System.out.println("size = " + set.size());                          // 2

        // Порядок в HashSet НЕ предсказуем — он зависит от бакетов
        System.out.println("HashSet:        " + set);

        // LinkedHashSet хранит порядок вставки
        LinkedHashSet<Word> linked = new LinkedHashSet<>();
        linked.add(new Word("c"));
        linked.add(new Word("a"));
        linked.add(new Word("b"));
        System.out.println("LinkedHashSet:  " + linked);   // [c, a, b]

        // LinkedHashMap — тот же приём, плюс есть режим access-order для LRU-кэша
        LinkedHashMap<String, Integer> lru = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(java.util.Map.Entry<String, Integer> eldest) {
                return size() > 3;   // храним не больше 3 — самый старый "вытесняется"
            }
        };
        lru.put("a", 1);
        lru.put("b", 2);
        lru.put("c", 3);
        lru.get("a");                 // обращение к "a" -> сдвиг в конец access-order
        lru.put("d", 4);              // переполнили -> "b" вытесняется (самый старый по доступу)
        System.out.println("LRU:            " + lru);
    }
}
