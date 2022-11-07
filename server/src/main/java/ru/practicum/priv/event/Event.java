package ru.practicum.priv.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.admin.category.Category;
import ru.practicum.admin.user.User;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Column(name = "title", length = 120, nullable = false)
    private String title;
    @Column(name = "annotation", length = 2000)
    private String annotation;
    @NotBlank
    @Column(name = "description", length = 7000)
    private String description;
    @NotNull
    private LocalDateTime createdOn;
    @NotNull
    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;
    @Column(name = "published_on", nullable = false)
    private LocalDateTime publishedOn;
    private boolean paid;
    private boolean requestModeration;
    private Integer participantLimit;
    private Double longitude;
    private Double latitude;
    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 100, nullable = false)
    private EventState state;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "initiator")
    private User initiator;
}
