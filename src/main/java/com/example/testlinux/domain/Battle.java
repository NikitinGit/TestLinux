package com.example.testlinux.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DDD-сущность Battle с паттерном Factory.
 *
 * Объект нельзя создать через new Battle() извне — только через
 * статический фабричный метод {@link #create(Integer)}, который
 * гарантирует, что сущность рождается в валидном состоянии (инварианты).
 */
@Getter
@Entity
@Table(name = "battles")
// JPA требует конструктор без аргументов, но снаружи он не нужен —
// protected прячет его от прикладного кода
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Battle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "battle_id")
    private Long idBattle;

    @Column(name = "section_number")
    private Integer sectionNumber;

    // приватный конструктор — вызывается только фабричным методом
    private Battle(Integer sectionNumber) {
        this.sectionNumber = sectionNumber;
    }

    /**
     * Фабричный метод (DDD Factory).
     * Единственный способ создать новый Battle — здесь проверяются инварианты.
     */
    public static Battle create(Integer sectionNumber) {
        if (sectionNumber == null) {
            throw new IllegalArgumentException("sectionNumber must not be null");
        }
        if (sectionNumber < 1) {
            throw new IllegalArgumentException("sectionNumber must be >= 1, was: " + sectionNumber);
        }
        return new Battle(sectionNumber);
    }

    /**
     * Доменное поведение вместо сеттера — мутация проходит через метод,
     * который сам следит за консистентностью состояния.
     */
    public void moveToNextSection() {
        this.sectionNumber++;
    }
}