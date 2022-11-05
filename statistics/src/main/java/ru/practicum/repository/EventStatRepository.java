package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.EventStat;

public interface EventStatRepository extends JpaRepository<EventStat, Long> {
}
