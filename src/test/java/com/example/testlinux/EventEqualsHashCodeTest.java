package com.example.testlinux;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Демонстрирует, почему @EqualsAndHashCode(of = "id") некорректен
 * для JPA-сущности.
 *
 * Используется локальный класс FakeEvent с такой же аннотацией,
 * как в Event.java, чтобы тест был автономным.
 */
public class EventEqualsHashCodeTest {

    @Getter
    @Setter
    @NoArgsConstructor
    @EqualsAndHashCode(of = "id")
    static class FakeEvent {
        private Integer id;          // имитирует @Id, заполняется Hibernate при persist()
        private String name;
    }

    /**
     * БАГ №1: две новые (ещё не сохранённые) сущности считаются равными,
     * потому что у обеих id == null, а Objects.equals(null, null) == true.
     * В HashSet попадает только одна.
     */
    @Test
    void transientEntitiesAreIncorrectlyEqual() {
        FakeEvent a = new FakeEvent();
        a.setName("Турнир А");

        FakeEvent b = new FakeEvent();
        b.setName("Турнир B");

        // Это РАЗНЫЕ события, но equals говорит, что они одинаковые
        assertEquals(a, b, "Lombok считает их равными — оба id == null");
        assertEquals(a.hashCode(), b.hashCode());

        Set<FakeEvent> events = new HashSet<>();
        events.add(a);
        events.add(b);

        // Ожидали 2, получили 1 — потеря данных
        assertEquals(1, events.size(),
                "В HashSet попала только одна сущность из двух разных!");
    }

    /**
     * БАГ №2: hashCode меняется после присвоения id (имитация persist()).
     * Если сущность положили в HashSet до сохранения, после сохранения
     * её больше не найти — она "потеряна" в коллекции.
     */
    @Test
    void hashCodeChangesAfterPersist() {
        FakeEvent event = new FakeEvent();
        event.setName("Турнир");

        Set<FakeEvent> events = new HashSet<>();
        events.add(event);

        // Сущность в множестве — contains() её находит
        assertTrue(events.contains(event), "До persist — находится");

        // Имитируем persist(): Hibernate проставил id
        event.setId(42);

        // Та же ссылка, но contains() её больше НЕ находит,
        // потому что hashCode изменился, и она лежит не в том bucket
        assertFalse(events.contains(event),
                "После persist hashCode изменился — сущность 'потерялась' в HashSet!");
    }

    /**
     * Дополнительно: две разные сущности с одинаковым id считаются равными.
     * Это в целом ожидаемо для бизнес-equals, но показывает,
     * что equals полностью игнорирует все остальные поля.
     */
    @Test
    void sameIdMeansEqualRegardlessOfOtherFields() {
        FakeEvent a = new FakeEvent();
        a.setId(1);
        a.setName("Совсем одно");

        FakeEvent b = new FakeEvent();
        b.setId(1);
        b.setName("Совсем другое");

        assertEquals(a, b);
    }
}