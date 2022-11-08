package ru.practicum.admin.user;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.admin.user.dto.UserDto;
import ru.practicum.admin.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getUsers(@RequestParam List<Long> ids,
                                  @RequestParam(defaultValue = "0", required = false) int from,
                                  @RequestParam(defaultValue = "10", required = false) int size) {
        return userService.getUsers(ids, from, size);
    }

    @PostMapping
    public UserDto saveUser(@Valid @RequestBody UserDto userDto) {
        return userService.saveUser(userDto);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
    }
}
