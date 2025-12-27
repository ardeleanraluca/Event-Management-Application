package com.demo.project;

import com.demo.project.dto.LocationDto;
import com.demo.project.entity.LocationEntity;
import com.demo.project.repository.EventRepository;
import com.demo.project.repository.LocationRepository;
import com.demo.project.service.LocationService;
import com.demo.project.service.LocationSeviceInterface;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class LocationServiceTests {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private EventRepository eventRepository;


    @Test
    void testCreateLocation() {
        LocationSeviceInterface locationSeviceInterface = new LocationService(locationRepository, eventRepository);
        LocationEntity locationEntity = LocationEntity.builder()
                .name("Sala1")
                .address("adresa")
                .noSeats(300L)
                .city("Zalau")
                .county("Salaj")
                .build();


        LocationDto locationDto = LocationDto.builder()
                .name("Sala1")
                .address("adresa")
                .noSeats(300L)
                .city("Zalau")
                .county("Salaj")
                .build();

        when(locationRepository.findByNameAndCountyAndCity(locationDto.getName(), locationDto.getCounty(), locationDto.getCity())).thenReturn(null);

        LocationDto result = locationSeviceInterface.createLocation(locationDto);

        assertEquals(locationDto, result);

        verify(locationRepository).saveAndFlush(locationEntity);
    }


    @Test
    void testDeleteLocation() {
        LocationSeviceInterface locationSeviceInterface = new LocationService(locationRepository, eventRepository);
        LocationEntity locationEntity = new LocationEntity(1L, 300L, "Sala1", "Salaj", "Zalau", "adresa");

        when(locationRepository.findById(locationEntity.getId())).thenReturn(Optional.of(locationEntity));
        when(eventRepository.existsByLocation_Id(locationEntity.getId())).thenReturn(false);

        boolean result = locationSeviceInterface.deleteLocation(locationEntity.getId());
        assertTrue(result);

        verify(locationRepository).deleteById(locationEntity.getId());
    }


}
