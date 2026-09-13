package com.example.testlinux.java.core.patterns.singlton;

/**
 * 2) Holder-идиома (Initialization-on-demand holder) — ленивый, потокобезопасный,
 * БЕЗ volatile и БЕЗ synchronized.
 * <p>
 * Опирается на гарантии JVM: вложенный класс {@code Holder} инициализируется ЛЕНИВО —
 * только при первом обращении к {@code Holder.INSTANCE} (т.е. в первом вызове getInstance()).
 * А инициализация класса потокобезопасна by design (JVM гарантирует, что статические
 * инициализаторы класса выполнятся ровно один раз, под внутренней блокировкой загрузчика).
 * <p>
 * Плюсы: просто, лениво, без блокировок на горячем пути. Обычно рекомендуемый вариант
 * для ленивого синглтона.
 */
public class SingletonHolder {

    private SingletonHolder() {
        System.out.println("SingletonHolder: конструктор (создан)");
    }

    // Вложенный класс НЕ загружается, пока к нему не обратятся → ленивая инициализация.
    private static class Holder {
        private static final SingletonHolder INSTANCE = new SingletonHolder();
    }

    public static SingletonHolder getInstance() {
        return Holder.INSTANCE;   // здесь впервые загрузится Holder → создастся INSTANCE (потокобезопасно)
    }
}
