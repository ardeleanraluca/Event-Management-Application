package com.demo.project;

import com.demo.project.dto.EventDto;
import com.demo.project.entity.*;
import com.demo.project.repository.EventRepository;
import com.demo.project.repository.LocationRepository;
import com.demo.project.repository.OrganizerRepository;
import com.demo.project.repository.TicketRepository;
import com.demo.project.service.EventService;
import com.demo.project.service.EventServiceInterface;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class EventServiceTests {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private OrganizerRepository organizerRepository;

    @Mock
    private TicketRepository ticketRepository;

    @Test
    void testCreateEvent() {
        EventServiceInterface eventServiceInterface = new EventService(eventRepository, locationRepository, organizerRepository, ticketRepository);
        RoleEntity role = new RoleEntity(1, "ORGANIZER");
        UserAccountEntity userAccountEntity = new UserAccountEntity(1L, "Raluca", "Ardelean", "email", "password", role);
        OrganizerEntity organizerEntity = new OrganizerEntity(1L, userAccountEntity, new ArrayList<>());

        LocationEntity locationEntity = LocationEntity.builder()
                .id(1L)
                .name("Sala1")
                .address("adresa")
                .noSeats(300L)
                .city("Zalau")
                .county("Salaj")
                .build();

        EventEntity eventEntity = EventEntity.builder()
                .name("Event")
                .description("description")
                .category("Movie")
                .location(locationEntity)
                .tickets(new ArrayList<>())
                .date("5 iunie 2023")
                .hour("20:00")
                .organizer(organizerEntity)
                .build();

        EventDto eventDto = EventDto.builder()
                .name("Event")
                .description("description")
                .category("Movie")
                .locationId(locationEntity.getId())
                .tickets(new ArrayList<>())
                .date("5 iunie 2023")
                .hour("20:00")
                .organizerId(organizerEntity.getId())
                .build();


        when(organizerRepository.findById(eventDto.getOrganizerId())).thenReturn(Optional.of(organizerEntity));

        when(locationRepository.findById(eventDto.getLocationId())).thenReturn(Optional.ofNullable(locationEntity));

        EventDto result = eventServiceInterface.createEvent(eventDto);

        assertEquals(eventDto, result);

        verify(eventRepository).saveAndFlush(eventEntity);
    }

    @Test
    void testUpdateEvent() {
        EventServiceInterface eventServiceInterface = new EventService(eventRepository, locationRepository, organizerRepository, ticketRepository);
        RoleEntity role = new RoleEntity(1, "ORGANIZER");
        UserAccountEntity userAccountEntity = new UserAccountEntity(1L, "Raluca", "Ardelean", "email", "password", role);
        OrganizerEntity organizerEntity = new OrganizerEntity(1L, userAccountEntity, new ArrayList<>());

        LocationEntity locationEntity = LocationEntity.builder()
                .id(1L)
                .name("Sala1")
                .address("adresa")
                .noSeats(300L)
                .city("Zalau")
                .county("Salaj")
                .build();

        EventEntity eventEntity = EventEntity.builder()
                .id(3L)
                .name("Event")
                .description("description")
                .category("Movie")
                .location(locationEntity)
                .tickets(new ArrayList<>())
                .date("5 iunie 2023")
                .hour("20:00")
                .organizer(organizerEntity)
                .build();

        EventDto eventDto = EventDto.builder()
                .id(3L)
                .name("Event")
                .description("description")
                .category("Movie")
                .locationId(locationEntity.getId())
                .tickets(new ArrayList<>())
                .date("5 iunie 2023")
                .hour("20:00")
                .organizerId(organizerEntity.getId())
                .build();


        when(eventRepository.findById(eventEntity.getId())).thenReturn(Optional.of(eventEntity));

        when(organizerRepository.findById(eventDto.getOrganizerId())).thenReturn(Optional.of(organizerEntity));

        when(locationRepository.findById(eventDto.getLocationId())).thenReturn(Optional.of(locationEntity));

        EventDto result = eventServiceInterface.updateEvent(eventDto, eventEntity.getId());

        assertEquals(eventDto, result);

        verify(eventRepository).saveAndFlush(eventEntity);
        verify(ticketRepository).deleteAllByEvent(eventEntity);
    }

    @Test
    void testDeleteEvent() {
        EventServiceInterface eventServiceInterface = new EventService(eventRepository, locationRepository, organizerRepository, ticketRepository);

        RoleEntity role = new RoleEntity(1, "ORGANIZER");
        UserAccountEntity userAccountEntity = new UserAccountEntity(1L, "Raluca", "Ardelean", "email", "password", role);
        OrganizerEntity organizerEntity = new OrganizerEntity(1L, userAccountEntity, new ArrayList<>());

        LocationEntity locationEntity = LocationEntity.builder()
                .id(1L)
                .name("Sala1")
                .address("adresa")
                .noSeats(300L)
                .city("Zalau")
                .county("Salaj")
                .build();

        EventEntity eventEntity = EventEntity.builder()
                .id(3L)
                .name("Event")
                .description("description")
                .category("Movie")
                .location(locationEntity)
                .tickets(new ArrayList<>())
                .date("5 iunie 2023")
                .hour("20:00")
                .organizer(organizerEntity)
                .build();

        when(eventRepository.findById(eventEntity.getId())).thenReturn(Optional.of(eventEntity));

        boolean result = eventServiceInterface.deleteEvent(eventEntity.getId());
        assertTrue(result);

        verify(eventRepository).deleteById(eventEntity.getId());
    }

    @Test
    void testGetEventsByCity() {
        EventServiceInterface eventServiceInterface = new EventService(eventRepository, locationRepository, organizerRepository, ticketRepository);

        RoleEntity role = new RoleEntity(1, "ORGANIZER");
        UserAccountEntity userAccountEntity = new UserAccountEntity(1L, "Raluca", "Ardelean", "email", "password", role);
        OrganizerEntity organizerEntity = new OrganizerEntity(1L, userAccountEntity, new ArrayList<>());

        LocationEntity locationEntity = LocationEntity.builder()
                .id(1L)
                .name("Sala1")
                .address("adresa")
                .noSeats(300L)
                .city("Zalau")
                .county("Salaj")
                .build();

        EventEntity eventEntity = EventEntity.builder()
                .id(3L)
                .name("Event")
                .description("description")
                .category("Movie")
                .location(locationEntity)
                .tickets(new ArrayList<>())
                .date("5 iunie 2023")
                .hour("20:00")
                .organizer(organizerEntity)
                .build();

        EventDto eventDto = EventDto.builder()
                .id(3L)
                .name("Event")
                .description("description")
                .category("Movie")
                .locationId(locationEntity.getId())
                .tickets(new ArrayList<>())
                .date("5 iunie 2023")
                .hour("20:00")
                .organizerId(organizerEntity.getId())
                .build();


        when(eventRepository.findAllByLocation_City("Zalau")).thenReturn(List.of(eventEntity));

        List<EventDto> result = eventServiceInterface.getEventsByCity("Zalau");
        assertEquals(List.of(eventDto), result);

        verify(eventRepository).findAllByLocation_City("Zalau");
    }

}
