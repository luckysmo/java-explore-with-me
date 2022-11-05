package ru.practicum;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "endpoint_hit")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class EventStat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "app", length = 250)
    private String app;
    @Column(name = "uri", length = 250)
    private String uri;
    @Column(name = "ip", length = 50)
    private String ip;
    @Column(name = "date_hit")
    private LocalDateTime dateHit;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        EventStat eventStat = (EventStat) o;
        return id != null && Objects.equals(id, eventStat.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
