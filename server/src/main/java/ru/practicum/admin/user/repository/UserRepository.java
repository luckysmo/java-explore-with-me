package ru.practicum.admin.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.admin.user.User;
import ru.practicum.exceptions.UserNotFoundException;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    default User checkAndReturnUserIfExist(Long id) {
        return findById(id).orElseThrow(
                () -> new UserNotFoundException(String.format("Пользователь с id=%s не найден", id)));
    }

    @Query("select u from User u where u.id in (?1)")
    List<User> findUsersByIds(List<Long> ids, Pageable pageable);

    @Query("select u from User as u" +
            " left join Event as e on u.id = e.initiator.id" +
            " group by u order by sum(e.likeCount) desc ")
    List<User> getUserRating(Pageable pageable);

    @Query("select u" +
            " from User u inner join Event e on e.initiator.id=u.id" +
            " order by e.likeCount desc ")
    List<User> getAuthorRating(Pageable pageable);
}
