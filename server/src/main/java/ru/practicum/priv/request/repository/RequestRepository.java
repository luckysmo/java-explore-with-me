package ru.practicum.priv.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.admin.user.User;
import ru.practicum.exceptions.RequestNotFoundException;
import ru.practicum.priv.event.Event;
import ru.practicum.priv.request.Request;
import ru.practicum.priv.request.RequestStatus;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    default Request checkAndReturnRequestIfExist(Long id) {
        return findById(id).orElseThrow(
                () -> new RequestNotFoundException(String.format("Реквест с id=%s не найден", id)));
    }

    Optional<Request> findByRequesterAndEvent(User user, Event event);

    List<Request> findRequestsByRequester(User user);

    List<Request> findRequestsByEvent(Event event);

    List<Request> findRequestsByEventAndStatus(Event event, RequestStatus requestStatus);

    int countRequestByEventAndStatus(Event event, RequestStatus requestStatus);
}
