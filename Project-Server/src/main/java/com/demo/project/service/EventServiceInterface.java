package com.demo.project.service;

import com.demo.project.dto.EventDto;

import java.util.List;

public interface EventServiceInterface {
    public EventDto createEvent(EventDto eventDto);
    public EventDto updateEvent(EventDto eventDto, Long id);
    public boolean deleteEvent(Long id);
    public List<EventDto> getEventsByCity(String city);
    public List<String> getAllCategories();
    public List<EventDto> getAllEvents();
    public EventDto getEventById(Long id);
    public List<EventDto> getEventsByOrganizer(Long id);
}
