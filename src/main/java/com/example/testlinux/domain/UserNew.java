package com.example.testlinux.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user")
public class UserNew {
    @Id
    //@GeneratedValue(strategy= GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Setter
    @Getter
    @Column(name="name")
    private String name;

    @Setter
    @Column(name="email")
    private String email;

    public UserNew() {}

    public UserNew(String name, String email) {
       this.email = email;
       this.name = name;
    }

}