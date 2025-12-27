package com.demo.project.controller;

import com.demo.project.dto.BoughtTicketDto;
import com.demo.project.dto.TicketDetailsDto;
import com.demo.project.service.BoughtTicketServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Handles the HTTP requests related to bought tickets, translates the JSON parameter
 * to object, and authenticates the request and transfer it to the service layer.
 */

@RestController
@RequestMapping("/boughtTicket")
public class BoughtTicketController {
    @Autowired
    private BoughtTicketServiceInterface boughtTicketServiceInterface;

    /**
     * Handles the api call for creating bought tickets and transfer it to the service layer.
     *
     * @return the response entity - OK if all bought tickets was created successfully
     */
    @PostMapping("/buy")
    public ResponseEntity<String> buyTickets(@RequestBody List<BoughtTicketDto> boughtTicketsDto) {
        int savedTickets = boughtTicketServiceInterface.buyTickets(boughtTicketsDto);
        if (savedTickets != boughtTicketsDto.size()) {
            return new ResponseEntity<>("Failed", HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>("Success", HttpStatus.OK);
        }
    }

    /**
     * Handles the api call for returning all purchased tickets of a user and transfer it to the service layer.
     *
     * @return all tickets bought by a user
     */

    @GetMapping("/myTickets")
    public ResponseEntity<List<TicketDetailsDto>> getTicketsByUserId( Long id) {
        List<TicketDetailsDto> tickets = boughtTicketServiceInterface.getTicketsByUserId(id);
        return new ResponseEntity<>(tickets, HttpStatus.OK);
    }

}
