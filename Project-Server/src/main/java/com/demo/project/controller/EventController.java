package com.demo.project.controller;

import com.demo.project.dto.EventDto;
import com.demo.project.dto.ImageDto;
import com.demo.project.service.EventServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * Handles the HTTP requests related to events, translates the JSON parameter
 * to object, and authenticates the request and transfer it to the service layer.
 */

@RestController
@RequestMapping("/events")
public class EventController {
    @Autowired
    private EventServiceInterface eventService;

    /**
     * Handles the api call for creating an event and transfer it to the service layer.
     *
     * @return the response entity - CREATED if the event was created successfully
     */
    @PostMapping(value = "/createEvent", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EventDto> createEvent(@RequestPart("event") EventDto eventDto, @RequestPart("image") MultipartFile file) throws IOException {
        ImageDto imageDto = new ImageDto(file.getOriginalFilename(), file.getContentType(), file.getBytes());
        eventDto.setImage(imageDto);

        System.out.println(eventDto);

        EventDto createdEvent = eventService.createEvent(eventDto);
        return new ResponseEntity<>(createdEvent, HttpStatus.CREATED);
    }

    /**
     * Handles the api call to update an event and transfer it to the service layer.
     *
     * @return the response entity - OK if the event was updated successfully, otherwise BAD_REQUEST.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventDto> updateEvent(@RequestBody EventDto eventDto, @PathVariable Long id) {
        EventDto updatedEvent = eventService.updateEvent(eventDto, id);
        if (updatedEvent == null) {
            return new ResponseEntity<>(updatedEvent, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
        }

    }

    /**
     * Handles the api call to update an event including the photo of that event and transfer it to the service layer.
     *
     * @return the response entity - OK if the event was updated successfully, otherwise BAD_REQUEST.
     */
    @PutMapping(value="/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EventDto> updateEventImage(@RequestPart("event") EventDto eventDto, @RequestPart("image") MultipartFile file,@PathVariable Long id) throws IOException {
        ImageDto imageDto = new ImageDto(file.getOriginalFilename(), file.getContentType(), file.getBytes());
        eventDto.setImage(imageDto);

        EventDto updatedEvent = eventService.updateEvent(eventDto, id);
        if (updatedEvent == null) {
            return new ResponseEntity<>(updatedEvent, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(updatedEvent, HttpStatus.OK);
        }

    }


    /**
     * Handles the api call for deleting an event and transfer it to the service layer.
     *
     * @return the response entity - OK if the event was deleted successfully, otherwise BAD_REQUEST.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEvent(@PathVariable Long id) {
        boolean deleted = eventService.deleteEvent(id);
        if (deleted) {
            return new ResponseEntity<>("Event deleted successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(("Event deletion failed!"), HttpStatus.BAD_REQUEST);

        }
    }

    /**
     * Handles the api call for returning all events from a specified city and transfer it to the service layer.
     *
     * @return the response entity - OK if the user was updated successfully, otherwise BAD_REQUEST.
     */
    @GetMapping("/filter")
    public ResponseEntity<List<EventDto>> getEventsByCity(@RequestParam("city") String city) {
        List<EventDto> events = eventService.getEventsByCity(city);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }


    /**
     * Handles the api call for returning all existing categories from all existing events and transfer it to the service layer.
     *
     * @return all existing categories
     */
    @GetMapping("/allCategories")
    public ResponseEntity<List<String>> getCategories() {
        List<String> categories = eventService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    /**
     * Handles the api call for returning all existing events and transfer it to the service layer.
     *
     * @return all existing events
     */
    @GetMapping("/allEvents")
    public ResponseEntity<List<EventDto>> getEvents() {
        List<EventDto> events = eventService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    /**
     * Handles the api call for returning an event by id and transfer it to the service layer.
     *
     * @return the event with given id
     */
    @GetMapping("")
    public ResponseEntity<EventDto> getEventById(@RequestParam("eventId") Long id) {
        EventDto event = eventService.getEventById(id);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    /**
     * Handles the api call for returning all events of an organizer and transfer it to the service layer.
     *
     * @return all events organized by a given organizer
     */
    @GetMapping("/my-organized-events")
    public ResponseEntity<List<EventDto>> getEventsByOrganizer(@RequestParam("organizerId") Long id) {
        List<EventDto> events = eventService.getEventsByOrganizer(id);
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

}
