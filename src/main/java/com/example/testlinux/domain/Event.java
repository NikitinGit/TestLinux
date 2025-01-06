package com.example.testlinux.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="event_id")
    private Integer eventId;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "version", referencedColumnName = "id")
    private UserNew user;

    //    @Version
/*    @Column(name="version")
    private Integer version = 0;*/

    @Column(name="organizer_login")
    private Integer organizerLogin;

    @Column(name="sport_id")
    private Integer sportId;

    @Column(name="name")
    private String nameEvent;

    @Column(name="about", columnDefinition="text")
    private String aboutEvent;


    @Column(name="address")
    private String address;

    @Column(name="performance_place")
    private String performancePlace;

    @Column(name="performance_time")
    private LocalTime performanceTime;

    @Column(name="date")
    private LocalDate eventDate;

    @Column(name="rings_count")
    private Integer ringsCount;

    @Column(name="number_created_fighters")
    private Integer numberCreatedFighters = 0;

    @Column(name="pay_by_site")
    private Boolean payBySite;

    @Column(name="sortition_auto")
    private Boolean sortitionAuto;

    @Column(name="notification")
    private String notification = "No";

    @Column(name="organizer_phone")
    private String organizerPhone;

    @Column(name="organizer_mail")
    private String organizerMail;
/*
    @CreatedBy
    @Column(name="created_by", updatable = false)
    private String createdBy;

    @LastModifiedBy
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
