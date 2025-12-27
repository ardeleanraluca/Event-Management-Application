package com.demo.project.service;

import com.demo.project.dto.OrganizerDto;
import com.demo.project.dto.StandardUserDto;

import java.util.List;

public interface UserServiceInterface {
    public boolean delete(Long id);

    public boolean deleteOrganizer(Long id);

    public StandardUserDto update(StandardUserDto newUser, Long id);

    public OrganizerDto updateOrganizer(OrganizerDto newOrganizer, Long id);

    public List<StandardUserDto> getAllStandardUsers();

    public OrganizerDto getOrgbyId(Long id);

    public List<OrganizerDto> getAllOrganizers();
}
