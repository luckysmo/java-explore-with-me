package ru.practicum.priv.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import ru.practicum.priv.event.EventState;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = true)
public class EventFullDto extends EventShortDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime publishedOn;
    private boolean requestModeration;
    private Integer participantLimit;
    private Location location;
    private EventState state;
}
