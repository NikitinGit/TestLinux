package com.example.testlinux.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
@Table(name = "battles")
public class Battle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="battle_id")
    private Long idBattle;

    @Column(name="section_number")
    private Integer sectionNumber;
}