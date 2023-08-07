package com.service.EventMicroservice.service.impl;

import com.service.EventMicroservice.dto.EventCreationDTO;
import com.service.EventMicroservice.dto.EventDTO;
import com.service.EventMicroservice.exception.EventTimeConflictException;
import com.service.EventMicroservice.exception.HTTPRequestException;
import com.service.EventMicroservice.model.Event;
import com.service.EventMicroservice.repository.EventRepository;
import com.service.EventMicroservice.service.EventService;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Validated
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final ModelMapper modelMapper;
    @Value("${user.service.url}")
    private String userServiceUrl;


    public EventServiceImpl(EventRepository eventRepository, ModelMapper modelMapper) {
        this.eventRepository = eventRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public EventDTO addEvent(Long userId, EventCreationDTO eventCreationDTO) {
        Event event = modelMapper.map(eventCreationDTO, Event.class);
        event.setUserId(userId);
        if (isEventHasTimeConflict(event)) {
            throw new EventTimeConflictException("Another event is already taking this place at this time: " + event.getDate());
        }
        if(!isUserExists(userId)){
            throw new EntityNotFoundException("User not found with id: " + userId);
        }
        eventRepository.save(event);
        log.info("Event saved: {}", event.getName());
        return modelMapper.map(event, EventDTO.class);
    }

    @Override
    public boolean isEventHasTimeConflict(Event event) {
        List<Event> anotherEvents = eventRepository.findAllByLocation(event.getLocation());
        for (Event currentEvent : anotherEvents) {
            if (isTimeConflicted(currentEvent, event)) {
                log.info("Event have time conflict: {}", event.getName());
                return true;
            }
        }
        return false;
    }

    @Override
    public EventDTO getEventById(Long eventId) {
        Optional<Event> optionalEvent = eventRepository.findById(eventId);
        if (optionalEvent.isEmpty()) {
            throw new EntityNotFoundException("Event not found with id: " + eventId);
        }
        log.info("Event got by id: {}", eventId);
        return modelMapper.map(optionalEvent.get(), EventDTO.class);
    }

    @Override
    public List<EventDTO> getFilteredEvents(String location, LocalDateTime startDate, LocalDateTime endDate, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("date").ascending());
        Page<Event> eventPage = eventRepository.findAllFiltered(location, startDate, endDate, pageRequest);
        log.info("Events get in the amount of", eventPage.getTotalElements());
        return eventPage.stream().map(event -> modelMapper.map(event, EventDTO.class)).collect(Collectors.toList());
    }

    @Override
    public boolean isUserExists(Long userId) {
        try {
            URL url = new URL(userServiceUrl + "/" + userId);
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            int responseCode = conn.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                return false;
            }
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String response = in.readLine();
                in.close();
                log.info("User exist with id: {}", userId);
                return response != null && !response.isEmpty();
            } else {
                throw new HTTPRequestException("HTTP request error: " + responseCode);
            }
        } catch (IOException e) {
            return false;
        }
    }

    private boolean isTimeConflicted(Event event, Event newEvent) {
        return (newEvent.getDate().isAfter(event.getDate()) && newEvent.getDate().isBefore(event.getDate().plus(event.getDuration()))) || (newEvent.getDate().isEqual(event.getDate())) || (newEvent.getDate().isBefore(event.getDate()) && newEvent.getDate().plus(newEvent.getDuration()).isAfter(event.getDate()));

    }

}
