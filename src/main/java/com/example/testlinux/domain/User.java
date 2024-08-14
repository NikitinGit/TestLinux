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
    private String bigname;
    @Column(name="email")
    private String bigemail;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return bigname;
    }

    public void setName(String name) {
        this.bigname = name;
    }

    public String getEmail() {
        return bigemail;
    }

    public void setEmail(String email) {
        this.bigemail = email;
    }
}