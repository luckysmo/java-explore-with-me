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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.net.URLDecoder.decode;
import static java.nio.charset.StandardCharsets.UTF_8;

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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime startDate = LocalDateTime.now();
        LocalDateTime endDate = LocalDateTime.now();
        if (start != null && end != null) {
            startDate = LocalDateTime.parse(decode(start, UTF_8), formatter);
            endDate = LocalDateTime.parse(decode(end, UTF_8), formatter);
        }
        return eventStatService.getEventStats(startDate, endDate, uris, unique);
    }
}
