package com.demo.project.controller;

import com.demo.project.dto.LocationDto;
import com.demo.project.service.LocationSeviceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Handles the HTTP requests related to events'location, translates the JSON parameter
 * to object, and authenticates the request and transfer it to the service layer.
 */

@RestController
@RequestMapping("/location")
public class LocationController {
    @Autowired
    private LocationSeviceInterface locationService;

    /**
     * Handles the api call for creating a location for an event and transfer it to the service layer.
     *
     * @return the response entity - OK if the location was created successfully, otherwise CREATED.
     */
    @PostMapping("/createLocation")
    public ResponseEntity<LocationDto> createLocation(@RequestBody LocationDto locationDto) {
        LocationDto createdLocation = locationService.createLocation(locationDto);
        if(createdLocation==null){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }else{
            return new ResponseEntity<>(createdLocation, HttpStatus.OK);
        }
    }

    /**
     * Handles the api call for deleting a location and transfer it to the service layer.
     *
     * @return the response entity - OK if the location was deleted successfully, otherwise BAD_REQUEST.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLocation(@PathVariable Long id) {
        boolean deleted =  locationService.deleteLocation(id);
        if (deleted) {
            return new ResponseEntity<>("Location deleted successfully!", HttpStatus.OK);
        } else {
            return new ResponseEntity<>(("Location deletion failed - Location do not exists or exists events that take place here! "), HttpStatus.BAD_REQUEST);

        }
    }

    /**
     * Handles the api call for returning all locations from a city by id and transfer it to the service layer.
     *
     * @return all available locations from a given county and city
     */
    @GetMapping("")
    public ResponseEntity<List<LocationDto>> getLocationsByCountyAndCity(String county, String city) {
        List<LocationDto> locations = locationService.getLocationsByCountyAndCity(county,city);
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    /**
     * Handles the api call for returning a location by id and transfer it to the service layer.
     *
     * @return the location with given id
     */
    @GetMapping("/getById")
    public ResponseEntity<LocationDto> getLocationById(Long id) {
        LocationDto location = locationService.getLocationById(id);
        return new ResponseEntity<>(location, HttpStatus.OK);
    }

    /**
     * Handles the api call for returning all existing locations and transfer it to the service layer.
     *
     * @return all available locations
     */
    @GetMapping("/allLocations")
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        List<LocationDto> locations = locationService.getAllLocations();
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }


}
