package com.demo.project.service;

import com.demo.project.dto.LocationDto;

import java.util.List;

public interface LocationSeviceInterface {
    public LocationDto createLocation(LocationDto locationDto);
    public boolean deleteLocation(Long id);
    public List<LocationDto> getLocationsByCountyAndCity(String county, String city);
    public LocationDto getLocationById(Long Id);
    public List<LocationDto> getAllLocations();
}
