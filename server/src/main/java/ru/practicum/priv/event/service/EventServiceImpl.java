package ru.practicum.priv.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.admin.category.repository.CategoryRepository;
import ru.practicum.admin.user.User;
import ru.practicum.admin.user.repository.UserRepository;
import ru.practicum.exceptions.EventBadRequestException;
import ru.practicum.exceptions.EventForbiddenException;
import ru.practicum.exceptions.LikeNotFoundException;
import ru.practicum.exceptions.RequestForbiddenException;
import ru.practicum.priv.event.Event;
import ru.practicum.priv.event.EventState;
import ru.practicum.priv.event.dto.EventFullDto;
import ru.practicum.priv.event.dto.EventMapper;
import ru.practicum.priv.event.dto.NewEventDto;
import ru.practicum.priv.event.dto.UpdateEventDto;
import ru.practicum.priv.event.dto.service.EventDtoService;
import ru.practicum.priv.event.repository.EventRepository;
import ru.practicum.priv.likes.Like;
import ru.practicum.priv.likes.LikeRepository;
import ru.practicum.priv.request.Request;
import ru.practicum.priv.request.RequestStatus;
import ru.practicum.priv.request.dto.RequestDto;
import ru.practicum.priv.request.repository.RequestRepository;
import ru.practicum.priv.request.service.RequestService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

import static ru.practicum.priv.event.dto.EventMapper.eventToEventFullDto;
import static ru.practicum.priv.event.dto.EventMapper.newEventDtoToEvent;
import static ru.practicum.priv.request.dto.RequestMapper.requestToDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final RequestService requestService;
    private final EventDtoService eventDtoService;

    private final LikeRepository likeRepository;

    @Override
    public EventFullDto saveEvent(NewEventDto eventDto, Long userId) {
        log.debug("Запрос saveEvent с title - {} и userId {}", eventDto.getTitle(), userId);

        checkTimeOfEvent(eventDto);

        Event event = newEventDtoToEvent(eventDto);
        event.setCreatedOn(LocalDateTime.now());
        event.setCategory(categoryRepository.checkAndReturnCategoryIfExist(eventDto.getCategory()));
        event.setInitiator(userRepository.checkAndReturnUserIfExist(userId));
        event.setState(EventState.PENDING);
        event.setLikeCount(0L);
        event.setDislikeCount(0L);

        EventFullDto eventFullDto = eventToEventFullDto(
                eventRepository.save(event));

        return eventDtoService.fillAdditionalInfo(eventFullDto);
    }

    @Override
    public EventFullDto updateEvent(UpdateEventDto eventDto, Long userId) {
        log.debug("Запрос updateEvent с title - {} и userId {}", eventDto.getTitle(), userId);

        checkTimeOfEvent(eventDto);

        String error = "Нельзя редактировать чужие события. Владелец %s";
        Event event = getVerifiedEvent(userId, eventDto.getEventId(), error);

        if (event.getState() == EventState.PUBLISHED) {
            throw new EventForbiddenException("Событие уже опубликовано");
        }

        eventDto.setFieldsToEvent(event);
        if (eventDto.getCategory() != null) {
            event.setCategory(
                    categoryRepository.checkAndReturnCategoryIfExist(eventDto.getCategory())
            );
        }

        return eventDtoService.fillAdditionalInfo(
                eventToEventFullDto(
                        eventRepository.save(event)
                )
        );
    }

    @Override
    public EventFullDto cancelEvent(Long userId, Long eventId) {
        log.debug("Запрос getEvent с userId - {} и eventId {}", userId, eventId);

        String error = "Нельзя отменять чужие события. Владелец %s";
        Event event = getVerifiedEvent(userId, eventId, error);

        if (event.getState() != EventState.PENDING) {
            throw new EventForbiddenException("Событие должно быть в состоянии PENDING");
        }

        event.setState(EventState.CANCELED);

        return eventDtoService.fillAdditionalInfo(
                eventToEventFullDto(
                        eventRepository.save(event)
                )
        );
    }

    @Override
    public List<EventFullDto> getEvents(Long userId, int from, int size) {
        log.debug("Запрос getEvents с userId - {}", userId);

        User user = userRepository.checkAndReturnUserIfExist(userId);
        Pageable pageRequest = PageRequest.of(from, size);

        return eventDtoService.fillAdditionalInfo(
                eventToEventFullDto(eventRepository.findEventsByInitiator(user, pageRequest)));
    }

    @Override
    public EventFullDto getEvent(Long userId, Long eventId) {
        log.debug("Запрос getEvent с userId - {} и eventId {}", userId, eventId);

        String error = "Нельзя смотреть чужие события. Владелец %s";

        return eventDtoService.fillAdditionalInfo(
                eventToEventFullDto(getVerifiedEvent(userId, eventId, error)));
    }

    @Override
    public List<RequestDto> getEventRequests(Long userId, Long eventId) {
        log.debug("Запрос getEventRequests с userId - {} и eventId {}", userId, eventId);

        String error = "Нельзя смотреть чужие события. Владелец %s";

        return requestToDto(requestRepository.findRequestsByEvent(getVerifiedEvent(userId, eventId, error)));
    }

    @Override
    public RequestDto confirmEventRequest(Long userId, Long eventId, Long reqId) {
        log.debug("Запрос confirmEventRequest с userId - {} и eventId {} и reqId {}", userId, eventId, reqId);

        Request request = getVerifiedRequest(
                reqId,
                eventId,
                RequestStatus.CONFIRMED,
                "Запрос уже подтвержден"
        );

        String error = "Нельзя подтверждать заявки по чужому событию. Владелец события %s";
        Event event = getVerifiedEvent(userId, eventId, error);

        int requestCount = requestService.countRequestByEventOrThrowException(event);
        requestCount++;

        if (requestService.isExceededLimitOfRequests(requestCount, event.getParticipantLimit())) {
            rejectOpenRequest(event);
        }

        request.setStatus(RequestStatus.CONFIRMED);

        return requestToDto(requestRepository.save(request));
    }

    @Override
    public RequestDto rejectEventRequest(Long userId, Long eventId, Long reqId) {
        log.debug("Запрос rejectEventRequest с userId - {} и eventId {} и reqId {}", userId, eventId, reqId);

        String error = "Нельзя отменять заявки по чужому событию. Владелец события %s";
        getVerifiedEvent(userId, eventId, error);

        Request request = getVerifiedRequest(
                reqId,
                eventId,
                RequestStatus.REJECTED,
                "Запрос уже отменен"
        );

        request.setStatus(RequestStatus.REJECTED);

        return requestToDto(requestRepository.save(request));
    }

    @Override
    public EventFullDto addLikeOrDislike(Long userId, Long eventId, Boolean isLike) {
        log.debug(String.format("Запрос POST: /users/{userId}/events/{eventId}/like; " +
                "userId = %d, eventId = %d, isLike = %s", userId, eventId, isLike));

        userRepository.checkAndReturnUserIfExist(userId);
        Event event = eventRepository.checkAndReturnEventIfExist(eventId);
            Like like = likeRepository.findLikeByUserIdAndEventId(userId, eventId);

        if (like == null) {
            return EventMapper.eventToEventFullDto(saveLike(userId, eventId, isLike, event));
        } else {
            return EventMapper.eventToEventFullDto(updateLike(like, event, isLike));
        }
    }

    private Event updateLike(Like like, Event event, Boolean isLike) {
        if(like.getIsLike() == isLike) {
            return event;
        }
        if (isLike) {
            like.setIsLike(true);
            likeRepository.save(like);
            if (event.getDislikeCount() != 0L) {
                event.setLikeCount(event.getLikeCount() + 1);
                event.setDislikeCount(event.getDislikeCount() - 1);
            } else {
                event.setLikeCount(event.getLikeCount() + 1);
            }
        } else {
            like.setIsLike(false);
            likeRepository.save(like);
            if (event.getLikeCount() != 0L) {
                event.setDislikeCount(event.getDislikeCount() + 1);
                event.setLikeCount(event.getLikeCount() - 1);
            } else {
                event.setDislikeCount(event.getDislikeCount() + 1);
            }
        }
        return eventRepository.save(event);
    }

    private Event saveLike(Long userId, Long eventId, Boolean isLike, Event event) {
        likeRepository.save(Like.builder()
                .eventId(eventId)
                .userId(userId)
                .isLike(isLike)
                .build());
        if (isLike) {
            event.setLikeCount(event.getLikeCount() + 1);
        } else {
            event.setDislikeCount(event.getDislikeCount() + 1);
        }
        return eventRepository.save(event);
    }

    @Override
    public void deleteLikeOrDislike(Long userId, Long eventId, Boolean isLike) {
        log.debug(String.format("Запрос DELETE: /users/{userId}/events/{eventId}/like; " +
                "userId = %d, eventId = %d, isLike = %s", userId, eventId, isLike));

        userRepository.checkAndReturnUserIfExist(userId);
        Event event = eventRepository.checkAndReturnEventIfExist(eventId);
        Like like = likeRepository.findLikeByUserIdAndEventId(userId, eventId);
        if (like == null) {
            throw new LikeNotFoundException(String.format(
                    "like/dislike пользователя с id = %d на событие с id = %d не обнаружен", userId, eventId));
        }
        likeRepository.deleteById(like.getId());
        if (like.getIsLike()) {
            event.setLikeCount(event.getLikeCount() - 1);
        } else {
            event.setDislikeCount(event.getDislikeCount() - 1);
        }
        eventRepository.save(event);
    }

    private void checkTimeOfEvent(UpdateEventDto eventDto) {
        if (eventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            String eventDate = eventDto.getEventDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            throw new EventBadRequestException(
                    String.format("Событие начинается %s. Это очень рано. Никто не придет", eventDate));
        }
    }

    private Event getVerifiedEvent(Long userId, Long eventId, String error) {
        userRepository.checkAndReturnUserIfExist(userId);
        Event event = eventRepository.checkAndReturnEventIfExist(eventId);

        if (!Objects.equals(event.getInitiator().getId(), userId)) {
            throw new EventForbiddenException(
                    String.format(error, event.getInitiator().getId()));
        }
        return event;
    }

    private Request getVerifiedRequest(Long reqId, Long eventId, RequestStatus status, String error) {
        Request request = requestRepository.checkAndReturnRequestIfExist(reqId);

        if (request.getStatus() == status) {
            throw new RequestForbiddenException(error);
        }

        if (!Objects.equals(request.getEvent().getId(), eventId)) {
            throw new RequestForbiddenException(
                    String.format("В заявке указано другое событие - %s", eventId)
            );
        }

        return request;
    }

    private void rejectOpenRequest(Event event) {
        List<Request> requests = requestRepository.findRequestsByEventAndStatus(event, RequestStatus.PENDING);

        requests.forEach(x -> x.setStatus(RequestStatus.REJECTED));

        requestRepository.saveAll(requests);
    }
}
