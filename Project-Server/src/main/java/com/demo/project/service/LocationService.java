package com.demo.project.service;

import com.demo.project.dto.LocationDto;
import com.demo.project.entity.LocationEntity;
import com.demo.project.repository.EventRepository;
import com.demo.project.repository.LocationRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This class handles all requests related to events'location.
 */
@Component
@AllArgsConstructor
@NoArgsConstructor
public class LocationService implements LocationSeviceInterface {
    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private EventRepository eventRepository;

    /**
     * Create a location for an event and adds it into database
     *
     * @param locationDto an object that contains all the details of an location
     * @return LocationDto - If the location was successfully added in the database it returns created location, otherwise
     * the registration will not succeed and it returns null.
     */
    @Transactional
    public LocationDto createLocation(LocationDto locationDto) {
        if (locationRepository.findByNameAndCountyAndCity(
                locationDto.getName(), locationDto.getCounty(), locationDto.getCity()) != null) {
            return null;
        }
        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setAddress(locationDto.getAddress());
        locationEntity.setNoSeats(locationDto.getNoSeats());
        locationEntity.setName(locationDto.getName());
        locationEntity.setCity(locationDto.getCity());
        locationEntity.setCounty(locationDto.getCounty());

        locationRepository.saveAndFlush(locationEntity);
        return new LocationDto(locationEntity);
    }

    /**
     * Deletes a location from the database, if it exists.
     *
     * @param id the location ID that is being deleted.
     * @return boolean - true if the event was deleted successfully, otherwise false.
     */
    @Transactional
    public boolean deleteLocation(Long id) {
        if (locationRepository.findById(id).isEmpty()) {
            return false;
        }

        if (eventRepository.existsByLocation_Id(id)) {
            return false;
        } else {
            locationRepository.deleteById(id);
        }
        return true;
    }


    /**
     * Finds all locations from a given county and city in database and returns them.
     *
     * @param county the county
     * @param city the city
     * @return all location available in a specific city
     */
    public List<LocationDto> getLocationsByCountyAndCity(String county, String city) {
        List<LocationEntity> locationEntities = locationRepository.findAllByCountyAndCityOrderByName(county,city);
        return locationEntities.stream().map(LocationDto::new).toList();
    }

    /**
     * Finds location by id in database and returns them.
     *
     * @param id the id
     * @return the location with given id
     */
    public LocationDto getLocationById(Long id) {
        LocationEntity locationEntity = locationRepository.findById(id).get();
        return new LocationDto(locationEntity);
    }

    /**
     * Finds all location in database and returns them.
     *
     * @return all available locations
     */
    public List<LocationDto> getAllLocations() {
        List<LocationEntity> locationEntities = locationRepository.findAll();
        return locationEntities.stream().map(LocationDto::new).toList();
    }
}
