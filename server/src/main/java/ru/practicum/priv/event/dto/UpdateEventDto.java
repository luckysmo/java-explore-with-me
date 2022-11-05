package ru.practicum.priv.event.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import ru.practicum.json.CustomDateDeserializer;
import ru.practicum.priv.event.Event;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
public class UpdateEventDto {
    private Long eventId;
    @Size(min = 20, max = 2000, message = "annotation must be between 20 and 2000 characters")
    private String annotation;
    @Size(min = 20, max = 7000, message = "annotation must be between 20 and 7000 characters")
    private String description;
    @Size(min = 3, max = 120, message = "annotation must be between 3 and 120 characters")
    private String title;
    @NotNull
    @Future
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private LocalDateTime eventDate;
    private Long category;
    private Boolean paid;
    private Integer participantLimit;

    public void setFieldsToEvent(Event event) {
        if (annotation != null) event.setAnnotation(annotation);
        if (description != null) event.setDescription(description);
        if (title != null) event.setTitle(title);
        if (eventDate != null) event.setEventDate(eventDate);
        if (paid != null) event.setPaid(paid);
        if (participantLimit != null) event.setParticipantLimit(participantLimit);
    }
}
