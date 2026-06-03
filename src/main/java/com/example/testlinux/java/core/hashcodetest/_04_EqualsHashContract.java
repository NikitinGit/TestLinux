package com.example.testlinux.java.core.hashcodetest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

/**
 * Контракт equals/hashCode и что ломается, если его нарушить.
 *
 * Контракт:
 *   1) Если a.equals(b) == true, то a.hashCode() == b.hashCode().
 *   2) Если a.hashCode() == b.hashCode(), про equals НИЧЕГО не известно (могут быть НЕ равны).
 *   3) Пока объект в коллекции — его hash не должен меняться.
 *
 * Что ломается, если нарушить:
 *
 *  - hashCode НЕ переопределён, equals переопределён:
 *      два "равных по бизнесу" объекта попадут в РАЗНЫЕ бакеты,
 *      Set/Map не увидят их как один.
 *
 *  - hashCode зависит от изменяемого поля:
 *      положили в Set, потом изменили поле -> объект "потерялся" в карте.
 *
 *  - hashCode всегда одинаковый (например return 1):
 *      работает корректно, но всё валится в один бакет -> O(n) поиск (после порога деградирует
 *      в дерево, и всё равно медленнее, чем нормально размазанный hash).
 */
public class _04_EqualsHashContract {

    // ---- Случай 1: equals без hashCode ----
    static final class BrokenNoHash {
        final String id;
        BrokenNoHash(String id) { this.id = id; }
        @Override public boolean equals(Object o) {
            return o instanceof BrokenNoHash b && Objects.equals(id, b.id);
        }
        // hashCode унаследован от Object -> почти всегда разный
    }

    // ---- Случай 2: hashCode завязан на изменяемое поле ----
    static final class MutableKey {
        String name;
        MutableKey(String name) { this.name = name; }
        @Override public int hashCode() { return Objects.hashCode(name); }
        @Override public boolean equals(Object o) {
            return o instanceof MutableKey k && Objects.equals(name, k.name);
        }
        @Override public String toString() { return "MutableKey(" + name + ")"; }
    }

    // ---- Случай 3: одинаковый хеш для всех (легально, но плохо) ----
    static final class ConstantHash {
        final String id;
        ConstantHash(String id) { this.id = id; }
        @Override public int hashCode() { return 1; }
        @Override public boolean equals(Object o) {
            return o instanceof ConstantHash c && Objects.equals(id, c.id);
        }
    }

    public static void main(String[] args) {
        // 1) Без hashCode — Set "не узнаёт" дубликат
        HashSet<BrokenNoHash> brokenSet = new HashSet<>();
        brokenSet.add(new BrokenNoHash("x"));
        boolean addedAgain = brokenSet.add(new BrokenNoHash("x"));
        System.out.println("BrokenNoHash: добавился второй раз? " + addedAgain   // true (ОШИБКА)
                + ", size=" + brokenSet.size());                                 // 2

        // 2) Изменили поле после вставки — ключ "теряется"
        HashMap<MutableKey, String> map = new HashMap<>();
        MutableKey k = new MutableKey("first");
        map.put(k, "value");
        System.out.println("\nдо мутации:  get -> " + map.get(k));
        k.name = "changed";                                  // ломаем инвариант
        System.out.println("после мутации: get -> " + map.get(k));   // null!
        System.out.println("containsKey(k) -> " + map.containsKey(k));     // false!
        System.out.println("map.size() -> " + map.size());                 // 1, но достать нельзя

        // 3) Постоянный hash — всё в один бакет
        HashMap<ConstantHash, Integer> constMap = new HashMap<>();
        for (int i = 0; i < 5; i++) constMap.put(new ConstantHash("k" + i), i);
        System.out.println("\nConstantHash map работает, но всё в одном бакете. size=" + constMap.size());
    }
}
