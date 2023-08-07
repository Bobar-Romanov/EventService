package com.service.EventMicroservice.service;


import com.service.EventMicroservice.dto.EventCreationDTO;
import com.service.EventMicroservice.dto.EventDTO;
import com.service.EventMicroservice.model.Event;
import jakarta.validation.Valid;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;



public interface EventService {

    EventDTO addEvent(@Valid Long userId, @Valid EventCreationDTO dto);
    boolean isEventHasTimeConflict(Event event);
    EventDTO getEventById(Long eventId);

    List<EventDTO> getFilteredEvents(String location, LocalDateTime startDate, LocalDateTime endDate, int page, int size);

    boolean isUserExists(Long userId) throws IOException;

}
