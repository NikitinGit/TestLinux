package com.example.testlinux.domain;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Setter
@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "judges")
public class Judge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Integer id;
/*    @OneToMany(mappedBy = "judge", fetch = FetchType.LAZY)
    private List<JudgeScore> judgeScoreList;

    @OneToMany(mappedBy = "judge", fetch = FetchType.LAZY)
    private List<EventBidJudge> bidsList;*/

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "login", referencedColumnName = "id")
    private UserNew user;

    @Column(name="fullName")
    private String fullName;

    @Column(name="category")
    private String category;

    @Column(name="slug")
    private String slug;

    @Column(name="comment")
    private String comment;

 /*   @CreatedBy
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
    private LocalDateTime modifiedAt;

    @Transient
    public EventBidJudge getEventBidJudge(Integer eventId){
        if (bidsList == null) {
            return null;
        }

        return bidsList.stream().filter(b -> b.getEvent().getEventId().equals(eventId)).findFirst().
                orElse(null);
    }*/
}
