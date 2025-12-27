package com.demo.project.service;

import com.demo.project.dto.EventDto;
import com.demo.project.dto.TicketDto;
import com.demo.project.entity.*;
import com.demo.project.repository.EventRepository;
import com.demo.project.repository.LocationRepository;
import com.demo.project.repository.OrganizerRepository;
import com.demo.project.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class handles all requests related to events.
 */
@Component
@AllArgsConstructor
@NoArgsConstructor
public class EventService implements EventServiceInterface {
    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private OrganizerRepository organizerRepository;

    @Autowired
    private TicketRepository ticketRepository;

    /**
     * Create an event and adds it into database
     *
     * @param eventDto an object that contains all the details of an event
     * @return EventDto - If the event was successfully added in the database it returns created event, otherwise
     * the registration will not succeed and it returns null.
     */
    @Transactional
    public EventDto createEvent(EventDto eventDto) {
        System.out.println(eventDto);

        EventEntity eventEntity = new EventEntity();
        eventEntity.setName(eventDto.getName());
        eventEntity.setDescription(eventDto.getDescription());
        eventEntity.setCategory(eventDto.getCategory());
        eventEntity.setDate(eventDto.getDate());
        eventEntity.setHour(eventDto.getHour());


        ImageEntity newImage = ImageEntity.builder()
                .name(eventDto.getImage().getName())
                .type(eventDto.getImage().getType())
                .picByte(eventDto.getImage().getPicByte()).build();
        eventEntity.setImage(newImage);

        OrganizerEntity organizerEntity = organizerRepository.findById(eventDto.getOrganizerId()).get();
        eventEntity.setOrganizer(organizerEntity);

        LocationEntity locationEntity = locationRepository.findById(eventDto.getLocation().getId()).get();
        eventEntity.setLocation(locationEntity);
        eventEntity.setAvailableTickets(locationEntity.getNoSeats());
        eventEntity.setSoldTickets(0L);
        List<TicketDto> ticketDtos = eventDto.getTickets();
        System.out.println(ticketDtos.size());
        List<TicketEntity> ticketEntities = new ArrayList<>();

        for (TicketDto ticketDto : ticketDtos) {
            TicketEntity ticketEntity = new TicketEntity();
            ticketEntity.setDiscount(ticketDto.getDiscount());
            ticketEntity.setPrice(ticketDto.getPrice());
            ticketEntity.setEvent(eventEntity);
            ticketEntities.add(ticketEntity);
//            ticketRepository.saveAndFlush(ticketEntity); -- se salveaza cand se salveaza evenimentul
        }
        eventEntity.setTickets(ticketEntities);
        eventRepository.saveAndFlush(eventEntity);
        return new EventDto(eventEntity);

    }

    /**
     * Update an event's data in database, if it exists.
     *
     * @param eventDto an object that contains all the details of an event that will be updated.
     * @param id       The event ID that is being updated.
     * @return EventDto - the updated event if the event was updated successfully, otherwise null.
     */
    @Transactional
    public EventDto updateEvent(EventDto eventDto, Long id) {
        if (eventRepository.findById(id).isEmpty()) {
            return null;
        }
        EventEntity eventEntity = eventRepository.findById(id).get();
        eventEntity.setName(eventDto.getName());
        eventEntity.setDescription(eventDto.getDescription());
        eventEntity.setCategory(eventDto.getCategory());
        eventEntity.setDate(eventDto.getDate());
        eventEntity.setHour(eventDto.getHour());

        ImageEntity newImage = ImageEntity.builder()
                .name(eventDto.getImage().getName())
                .type(eventDto.getImage().getType())
                .picByte(eventDto.getImage().getPicByte()).build();
        eventEntity.setImage(newImage);

        LocationEntity locationEntity = locationRepository.findById(eventDto.getLocation().getId()).get();
        eventEntity.setLocation(locationEntity);
        eventEntity.setAvailableTickets(locationEntity.getNoSeats());

        List<TicketDto> ticketDtos = eventDto.getTickets();
        List<TicketEntity> ticketEntities = new ArrayList<>();
        ticketRepository.deleteAllByEvent(eventEntity);

        for (TicketDto ticketDto : ticketDtos) {
            TicketEntity ticketEntity = new TicketEntity();
            ticketEntity.setDiscount(ticketDto.getDiscount());
            ticketEntity.setPrice(ticketDto.getPrice());
            ticketEntity.setEvent(eventEntity);
            ticketEntities.add(ticketEntity);
        }
        eventEntity.setTickets(ticketEntities);
        eventRepository.saveAndFlush(eventEntity);
        return new EventDto(eventEntity);

    }

    /**
     * Deletes an event from the database, if it exists.
     *
     * @param id the event ID that is being deleted.
     * @return boolean - true if the event was deleted successfully, otherwise false.
     */
    public boolean deleteEvent(Long id) {
        if (eventRepository.findById(id).isEmpty()) {
            return false;
        }

        eventRepository.deleteById(id);
        return true;
    }

    /**
     * Finds all events from a city in database and returns them.
     *
     * @param city the city
     * @return the events in that city
     */
    public List<EventDto> getEventsByCity(String city) {
        List<EventEntity> eventEntities = eventRepository.findAllByLocation_City(city);
        return eventEntities.stream().map(EventDto::new).toList();
    }

    /**
     * Finds the event with given id in database and returns them.
     *
     * @param id the id
     * @return the event with given id
     */
    public EventDto getEventById(Long id) {
        EventEntity eventEntity = eventRepository.findById(id).get();
        return new EventDto(eventEntity);
    }

    /**
     * Finds all existing events in database and returns them.
     *
     * @return all existing events
     */
    public List<EventDto> getAllEvents() {
        List<EventEntity> eventEntities = eventRepository.findAll();
        List<EventDto> events = new ArrayList<>(eventEntities.stream().map(EventDto::new).toList());
        Collections.reverse(events);
        return events;
    }

    /**
     * Finds all event categories in database and returns them.
     *
     * @return all existing categories
     */
    public List<String> getAllCategories() {
        return eventRepository.findAll().stream().map(EventEntity::getCategory).distinct().sorted().toList();
    }

    /**
     * Finds all events organized by a given organizer in database and returns them.
     *
     * @param id organizer's id
     * @return all events of an organizer
     */
    public List<EventDto> getEventsByOrganizer(Long id) {
        List<EventEntity> eventEntities = eventRepository.findAllByOrganizer_Id(id);
        return eventEntities.stream().map(EventDto::new).toList();
    }


}
