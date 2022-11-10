package ru.practicum.publics.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.admin.user.dto.UserMapper;
import ru.practicum.admin.user.dto.UserShortDto;
import ru.practicum.admin.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserPublicServiceImpl implements UserPublicService {

    private final UserRepository userRepository;

    @Override
    public List<UserShortDto> getUserRating(Integer from, Integer size) {
        int page = from < size ? 0 : from / size;
        Pageable pageable = PageRequest.of(page, size);

        return userRepository.getUserRating(pageable).stream()
                .map(UserMapper::toUserShortDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserShortDto> getAuthorRating(Integer from, Integer size) {
        int page = from < size ? 0 : from / size;
        Pageable pageable = PageRequest.of(page, size);

        return userRepository.getAuthorRating(pageable).stream()
                .map(UserMapper::toUserShortDto)
                .collect(Collectors.toList());
    }
}
