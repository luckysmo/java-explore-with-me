package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.EventStatDto;
import ru.practicum.dto.EventStatDtoView;
import ru.practicum.service.EventStatService;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class EventStatController {
    private final EventStatService eventStatService;

    @PostMapping("/hit")
    public void save(@RequestBody EventStatDto eventStatDto) {
        eventStatService.save(eventStatDto);
    }

    @GetMapping("/stats")
    public List<EventStatDtoView> getStats(@RequestParam String start,
                                           @RequestParam String end,
                                           @RequestParam List<String> uris,
                                           @RequestParam Boolean unique) {
        LocalDateTime startDate = LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8));
        LocalDateTime endDate = LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8));
        return eventStatService.getEventStats(startDate, endDate, uris, unique);
    }
}
