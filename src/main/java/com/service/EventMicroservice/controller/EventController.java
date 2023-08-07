package com.service.EventMicroservice.controller;

import com.service.EventMicroservice.dto.EventCreationDTO;
import com.service.EventMicroservice.dto.EventDTO;
import com.service.EventMicroservice.service.EventService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/event")
public class EventController {

    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventDTO createEvent(@RequestHeader("userId") Long userId, @RequestBody @Valid EventCreationDTO dto) {
        return eventService.addEvent(userId, dto);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventDTO getEventById(@PathVariable Long eventId) {
        return eventService.getEventById(eventId);
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<EventDTO> getAllEvents(@RequestParam(required = false) String location, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime startDate, @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDateTime endDate, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return eventService.getFilteredEvents(location, startDate, endDate, page, size);
    }
}
