package ru.practicum.publics.user.service;

import ru.practicum.admin.user.dto.UserShortDto;

import java.util.List;

public interface UserPublicService {

    List<UserShortDto> getUserRating(Integer from, Integer size);

    List<UserShortDto> getAuthorRating(Integer from, Integer size);
}
