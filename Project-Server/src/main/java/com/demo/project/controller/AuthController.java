package com.demo.project.controller;

import com.demo.project.dto.OrganizerDto;
import com.demo.project.dto.StandardUserDto;
import com.demo.project.dto.UserAccountDto;
import com.demo.project.dto.UserLoginDto;
import com.demo.project.service.AuthServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Handles the HTTP requests related to authentication and registration, translates the JSON parameter
 * to object, and authenticates the request and transfer it to the service layer.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthServiceInterface authService;

    /**
     * Handles the api call for registration of a new user and transfer it to the service layer.
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody StandardUserDto standardUserDto) {

        StandardUserDto registered = authService.register(standardUserDto);
        if (registered == null) {
            return new ResponseEntity<>("An account is already registered with this email!", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("User registered success!", HttpStatus.OK);
        }
    }

    /**
     * Handles the api call for registration of a new organizer and transfer it to the service layer
     * if the user which try to make this request is an admin
     * else it returns a bad request
     */
    @PostMapping("/registerOrganizer")
    public ResponseEntity<String> registerOrganizer(@RequestBody OrganizerDto organizerDto) {
        OrganizerDto registered = authService.registerOrganizer(organizerDto);
        if (registered == null) {
            return new ResponseEntity<>("An account is already registered with this email!", HttpStatus.BAD_REQUEST);

        } else {
            return new ResponseEntity<>("Organizer registered success!", HttpStatus.OK);
        }
    }


    /**
     * Handles the api call for authentication of a user and transfer it to the service layer.
     */
    @PostMapping("/login")
    public ResponseEntity<UserAccountDto> login(@RequestBody UserLoginDto userLoginDto) {
        return new ResponseEntity<>(authService.login(userLoginDto), HttpStatus.OK);
    }

}
