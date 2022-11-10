package ru.practicum.publics.user.service;

import ru.practicum.admin.user.dto.UserDto;

import java.util.List;

public interface UserPublicService {

    List<UserDto> getUserRating(Integer from, Integer size);

}
