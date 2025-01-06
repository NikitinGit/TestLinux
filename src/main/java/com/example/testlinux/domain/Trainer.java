package com.example.testlinux.domain;

import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Entity
@Table(name = "trainers")
@Setter
@Getter
public class Trainer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private Integer id;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "login", referencedColumnName = "id")
    private UserNew user;

    @Column(name="fullName")
    private String fullname;

/*    @OneToMany(mappedBy = "trainer", fetch = FetchType.LAZY)
    private List<Fighter> pupilList = new ArrayList<>();*/

/*    @Column(name="slug")
    private String slug;
    @Column(name="fullName")
    private String fullName;

    @CreatedBy
    @Column(name="created_by", updatable = false)
    private String createdBy;*/

    /*@LastModifiedBy
    @Column(name="modified_by")
    private String modifiedBy;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="modified_at")
    private LocalDateTime modifiedAt;*/
}

