package com.example.testlinux.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "events_bids_fighters")
public class EventBidFighter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "idEvent")
    private Integer eventId;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fighter_id")
    private Fighter fighter;

    /*@Column(name="approved")
    private Integer approved = 0;

    @Column(name="approved_doc")
    private Integer approvedDoc = 0;

    @Column(name="approve_weight")
    private Boolean approveWeight = false;

    @Column(name="level_by_site")
    private Integer levelBySite;

    @Column(name="count_sends")
    private Integer countSends = 0;

    @Column(name="deposit_by_acquire")
    private String deposit;

    @Column(name="deposit_by_organizer")
    private String depositByOrganizer;

    @Column(name="by_trainer")
    private Integer byTrainer;

    @Column(name="comment")
    private String comment;

    @Column(name="ban")
    private Boolean ban = false;

    @Column(name="number_of_short_media")
    private Integer numberOfShortMedia = 0;


    @Transient
    private boolean bidIsNotValid = false;*/
}

