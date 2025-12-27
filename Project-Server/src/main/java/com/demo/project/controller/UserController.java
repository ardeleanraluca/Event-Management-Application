package com.demo.project.controller;

import com.demo.project.dto.OrganizerDto;
import com.demo.project.dto.StandardUserDto;
import com.demo.project.service.UserServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Handles the HTTP requests related to users, translates the JSON parameter
 * to object, and authenticates the request and transfer it to the service layer.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserServiceInterface userService;


    /**
     * Handles the api call for deleting of a user and transfer it to the service layer.
     *
     * @return the response entity - OK if the user was deleted successfully, otherwise BAD_REQUEST.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        boolean deleted = userService.delete(id);
        if (deleted) {
            return new ResponseEntity<>(("User deleted!"), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(("User deletion failed!"), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     *  Handles the api call for deleting of an organizer with his events and transfer it to the service layer.
     *
     * @return the response entity - OK if the organizer was deleted successfully, otherwise BAD_REQUEST.
     */
    @DeleteMapping("/deleteOrganizer/{id}")
    public ResponseEntity<String> deleteOrganizer(@PathVariable Long id) {
        boolean deleted = userService.deleteOrganizer(id);
        if (deleted) {
            return new ResponseEntity<>("Organizer and his events deleted successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(("Organizer deletion failed"), HttpStatus.BAD_REQUEST);
        }
    }


    /**
     * Handles the api call to update a user's data and transfer it to the service layer.
     *
     * @return the response entity - OK if the user was updated successfully, otherwise BAD_REQUEST.
     */
    @PutMapping("/{id}")
    public ResponseEntity<StandardUserDto> update(@RequestBody StandardUserDto newUser, @PathVariable Long id) {
        StandardUserDto updatedUser = userService.update(newUser, id);
        if (updatedUser == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
    }


    /**
     * Handles the api call to update an organizer's data and transfer it to the service layer.
     *
     *  @return the response entity - OK if the organizer was updated successfully, otherwise BAD_REQUEST.
     */
    @PutMapping("updateOrganizer/{id}")
    public ResponseEntity<OrganizerDto> updateOrganizer(@RequestBody OrganizerDto newOrganizer, @PathVariable Long id) {

        OrganizerDto updateOrganizer = userService.updateOrganizer(newOrganizer, id);
        if (updateOrganizer == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(updateOrganizer, HttpStatus.OK);
        }
    }


    /**
     * Handles the api call for returning all standard users and transfer it to the service layer.
     *
     * @return all standard users
     */
    @GetMapping("/all")
    public ResponseEntity<List<StandardUserDto>> getAllStandardUsers() {
        List<StandardUserDto> users = userService.getAllStandardUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * Handles the api call for returning all organizers and transfer it to the service layer.
     *
     * @return all organizers
     */
    @GetMapping("/all/organizers")
    public ResponseEntity<List<OrganizerDto>> getAllOrganizers() {
        List<OrganizerDto> organizerDtos = userService.getAllOrganizers();
        return new ResponseEntity<>(organizerDtos, HttpStatus.OK);
    }

    /**
     * Handles the api call for returning an organizer by id and transfer it to the service layer.
     *
     * @return the organizer with given id
     */
    @GetMapping("/organizerId")
    public ResponseEntity<OrganizerDto> getOrgById(Long id) {
        OrganizerDto org = this.userService.getOrgbyId(id);
        return new ResponseEntity<>(org, HttpStatus.OK);
    }
}
