package com.example.testlinux.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@EqualsAndHashCode(of = "eventId")
@NoArgsConstructor
@DynamicUpdate
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="event_id")
    private Integer eventId;

    @Column(name="organizer_login")
    private Integer organizerLogin;

    @ToString.Exclude
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<EventBidFighter> eventBidFighters = new ArrayList<>();

    @Column(name="name")
    private String nameEvent;

    @Column(name="about", columnDefinition="text")
    private String aboutEvent;


    @Column(name="address")
    private String address;

    @Column(name="performance_place")
    private String performancePlace;

    @Column(name="date")
    private LocalDate eventDate;

    @Column(name="rings_count")
    private Integer ringsCount;

    @Column(name="sortition_auto")
    private Boolean sortitionAuto;

    @Column(name="notification")
    private String notification = "No";

    @Column(name="organizer_phone")
    private String organizerPhone;

    @Column(name="organizer_mail")
    private String organizerMail;

/*    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Event event = (Event) o;
        return getEventId() != null && Objects.equals(getEventId(), event.getEventId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }*/
}
