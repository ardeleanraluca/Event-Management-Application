package com.demo.project.service;

import com.demo.project.dto.OrganizerDto;
import com.demo.project.dto.StandardUserDto;
import com.demo.project.dto.UserAccountDto;
import com.demo.project.dto.UserLoginDto;

public interface AuthServiceInterface {
    public StandardUserDto register(StandardUserDto standardUserDto);
    public OrganizerDto registerOrganizer(OrganizerDto organizerDto);
    public UserAccountDto login(UserLoginDto userLoginDto);
}
