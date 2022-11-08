package ru.practicum.priv.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.admin.user.User;
import ru.practicum.priv.event.Event;

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
import java.time.LocalDateTime;

@Entity
@Table(name = "participation_request")
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime created;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "event")
    private Event event;
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "requester")
    private User requester;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 100, nullable = false)
    private RequestStatus status;
}
