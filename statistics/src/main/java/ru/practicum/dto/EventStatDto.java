package ru.practicum.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import ru.practicum.json.CustomDateDeserializer;

import java.time.LocalDateTime;

@Data
public class EventStatDto {
    private String app;
    private String uri;
    private String ip;
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private LocalDateTime dateHit;
}
