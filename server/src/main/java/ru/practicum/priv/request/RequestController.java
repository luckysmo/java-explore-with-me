package ru.practicum.priv.request;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.priv.request.dto.RequestDto;
import ru.practicum.priv.request.service.RequestService;

import java.util.List;

@RestController
@RequestMapping(path = "/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @PostMapping
    public RequestDto saveRequest(@PathVariable Long userId,
                                  @RequestParam Long eventId) {
        return requestService.saveRequest(userId, eventId);
    }

    @GetMapping
    public List<RequestDto> getRequests(@PathVariable Long userId) {
        return requestService.getRequests(userId);
    }

    @PatchMapping("/{requestId}/cancel")
    public RequestDto cancelRequest(@PathVariable Long userId,
                                    @PathVariable Long requestId) {
        return requestService.cancelRequest(userId, requestId);
    }
}
