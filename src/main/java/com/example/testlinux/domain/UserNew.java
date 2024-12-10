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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

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