package com.example.testlinux.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class UserNew {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Integer id;

    @OneToOne(mappedBy = "user")
    private Fighter fighter;

    @OneToOne(mappedBy = "user")
    private Judge judge;

    @OneToOne(mappedBy = "user")
    private Trainer trainer;

    @Column(name="pass")
    private String pass;

    @Column(name="telefon")
    private String telefon;

    public UserNew() {}

    public UserNew(String telefon, String pass) {
       this.telefon = telefon;
       this.pass = pass;
    }
}