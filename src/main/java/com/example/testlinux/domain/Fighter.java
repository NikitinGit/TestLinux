package com.example.testlinux.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Setter
@Getter
@Slf4j
@Entity
@Table(name = "fighters")
public class Fighter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Integer id;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "login", referencedColumnName = "id")
    private User user;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @Column(name="slug")
    private String slug;

    @Column(name="club")
    private String club;

    @Column(name="weight")
    private Double weight;

    @Column(name="level_by_fighter")
    private Integer levelByFighter;

    @Column(name="level_by_site")
    private Integer levelBySite = 0;

    @Column(name="gender")
    private String gender;


    /*@Column(name="timeBusy")
    private Integer timeBusyMonth = 0; */

    @Column(name="phone_by_fighter")
    private String phoneByFighter;

    @Column(name="phone_by_trainer")
    private String phoneByTrainer;

    @Column(name="note")
    private String note;

    @Column(name="date_registration")
    private LocalDate dateBgn = LocalDate.now();

    @Column(name="document_path")
    private String docPath;

    @Column(name="count_battles_on_site")
    private Integer countBattlesOnSite = 0;

    @Column(name="count_wins_on_site")
    private Integer countWinsOnSite = 0;

    @Column(name="fromSortition")
    private String fromSortition;

    /*@EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idTrainer")
    private Trainer trainer;*/

    /*@Column(name="nameTrainer")
    private String nameTrainer;*/

    @Column(name="sport_community")
    private String sportCommunity;
}

