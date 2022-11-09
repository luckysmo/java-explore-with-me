package ru.practicum.priv.likes;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    Like findLikeByUserIdAndEventId(Long userId, Long eventId);
}
