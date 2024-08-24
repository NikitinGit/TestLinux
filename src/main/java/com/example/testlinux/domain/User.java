package com.example.testlinux.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "user")
public class User {
    @Id
    //@GeneratedValue(strategy= GenerationType.AUTO)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;

    @Column(name="name")
    private String name;
    @Column(name="email")
    private String email;

    public User() {}

    public User(String name, String email) {
       this.email = email;
       this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}