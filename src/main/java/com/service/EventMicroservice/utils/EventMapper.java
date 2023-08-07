package com.service.EventMicroservice.utils;

import com.service.EventMicroservice.dto.EventCreationDTO;
import com.service.EventMicroservice.dto.EventDTO;
import com.service.EventMicroservice.model.Event;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    private final ModelMapper modelMapper;

    public EventMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Event toEvent(EventCreationDTO eventCreationDTO) {
        return modelMapper.map(eventCreationDTO, Event.class);
    }

    public EventDTO toEventDTO(Event event) {
        return modelMapper.map(event, EventDTO.class);
    }

}
