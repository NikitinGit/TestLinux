package com.example.testlinux.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Integer id;

    @OneToOne(mappedBy = "user")
    private Fighter fighter;

    @OneToOne(mappedBy = "user")
    private Trainer trainer;

    @Column(name="pass")
    private String pass;

    @Column(name="telefon")
    private String telefon;

    @Column(name="login")
    private String login;

    @Column(name="acstatus")
    private String acstatus;

    @Column(name="hash")
    private String hash;

    public User() {}

    public User(String telefon, String pass) {
       this.telefon = telefon;
       this.pass = pass;
    }

    public String getHashForPasswordChange(String iv) {
        String passwordHash = getPass();
        int pos = passwordHash == null ? -1 : passwordHash.lastIndexOf('$');
        String passwordSalt = pos >= 0 && passwordHash.length() >= pos + 21
                ? passwordHash.substring(pos, pos + 21)
                : hash;
        return org.apache.commons.codec.digest.DigestUtils.sha1Hex(getId() + " - " + iv + " - " + passwordSalt);
    }
}