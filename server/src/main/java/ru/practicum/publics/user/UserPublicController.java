package ru.practicum.publics.user;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.admin.user.dto.UserShortDto;
import ru.practicum.publics.user.service.UserPublicServiceImpl;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserPublicController {

    private final UserPublicServiceImpl userService;

    @GetMapping(path = "/rating")
    public List<UserShortDto> getUserRating(@RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        return userService.getUserRating(from, size);
    }

    @GetMapping(path = "/rating/author")
    public List<UserShortDto> getAuthorRating(@RequestParam(name = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                            @RequestParam(name = "size", defaultValue = "10") @Positive Integer size) {
        return userService.getAuthorRating(from, size);
    }
}
