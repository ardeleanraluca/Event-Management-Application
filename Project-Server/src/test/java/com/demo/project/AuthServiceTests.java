package com.demo.project;

import com.demo.project.dto.OrganizerDto;
import com.demo.project.dto.StandardUserDto;
import com.demo.project.entity.OrganizerEntity;
import com.demo.project.entity.RoleEntity;
import com.demo.project.entity.StandardUserEntity;
import com.demo.project.entity.UserAccountEntity;
import com.demo.project.repository.OrganizerRepository;
import com.demo.project.repository.RoleRepository;
import com.demo.project.repository.StandardUserRepository;
import com.demo.project.repository.UserAccountRepository;
import com.demo.project.service.AuthService;
import com.demo.project.service.AuthServiceInterface;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class AuthServiceTests {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private StandardUserRepository standardUserRepository;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private OrganizerRepository organizerRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;


    @Test
    public void testRegisterUser(){
        AuthServiceInterface authServiceInterface = new AuthService(authenticationManager,userAccountRepository,standardUserRepository,roleRepository,passwordEncoder,organizerRepository,applicationEventPublisher);
        RoleEntity role = new RoleEntity(1, "USER");
        UserAccountEntity userAccountEntity = UserAccountEntity.builder()
                .firstName("Raluca")
                .lastName("Ardelean")
                .email("email")
                .password("password")
                .role(role)
                .build();

        StandardUserEntity standardUserEntity = StandardUserEntity.builder()
                .userAccountEntity(userAccountEntity)
                .city("Oradea")
                .county("Bihor")
                .boughtTickets(new ArrayList<>())
                .build();

        StandardUserDto standardUserDto = StandardUserDto.builder()
                .firstName("Raluca")
                .lastName("Ardelean")
                .email("email")
                .password("password")
                .role("USER")
                .city("Oradea")
                .county("Bihor")
                .build();

        when(passwordEncoder.encode(standardUserDto.getPassword())).thenReturn(standardUserDto.getPassword());
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(role));

        StandardUserDto result = authServiceInterface.register(standardUserDto);
        assertEquals(standardUserDto,result);
        verify(userAccountRepository).saveAndFlush(userAccountEntity);
        verify(standardUserRepository).saveAndFlush(standardUserEntity);

    }


    @Test
    public void testRegisterOrganizer(){
        AuthServiceInterface authServiceInterface = new AuthService(authenticationManager,userAccountRepository,standardUserRepository,roleRepository,passwordEncoder,organizerRepository,applicationEventPublisher);
        RoleEntity role = new RoleEntity(1, "ORGANIZER");
        UserAccountEntity userAccountEntity = UserAccountEntity.builder()
                .firstName("Raluca")
                .lastName("Ardelean")
                .email("email")
                .password("password")
                .role(role)
                .build();

        OrganizerEntity organizerEntity = OrganizerEntity.builder()
                .userAccountEntity(userAccountEntity)
                .events(new ArrayList<>())
                .build();

        OrganizerDto organizerDto = OrganizerDto.builder()
                .firstName("Raluca")
                .lastName("Ardelean")
                .email("email")
                .password("password")
                .role("ORGANIZER")
                .events(new ArrayList<>())
                .build();

        when(passwordEncoder.encode(organizerDto.getPassword())).thenReturn(organizerDto.getPassword());
        when(roleRepository.findByName("ORGANIZER")).thenReturn(Optional.of(role));

        OrganizerDto result = authServiceInterface.registerOrganizer(organizerDto);
        assertEquals(organizerDto,result);
        verify(userAccountRepository).saveAndFlush(userAccountEntity);
        verify(organizerRepository).saveAndFlush(organizerEntity);

    }
}
