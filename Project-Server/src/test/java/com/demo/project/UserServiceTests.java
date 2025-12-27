package com.demo.project;

import com.demo.project.dto.OrganizerDto;
import com.demo.project.dto.StandardUserDto;
import com.demo.project.entity.OrganizerEntity;
import com.demo.project.entity.RoleEntity;
import com.demo.project.entity.StandardUserEntity;
import com.demo.project.entity.UserAccountEntity;
import com.demo.project.repository.OrganizerRepository;
import com.demo.project.repository.StandardUserRepository;
import com.demo.project.repository.UserAccountRepository;
import com.demo.project.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTests {
    @Mock
    private UserAccountRepository userAccountRepository;

    @Mock
    private StandardUserRepository standardUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private OrganizerRepository organizerRepository;

    @Test
    void testDeleteUser() {
        UserService userServiceInterface = new UserService(userAccountRepository, standardUserRepository, passwordEncoder, organizerRepository);
        RoleEntity role = new RoleEntity(1, "USER");
        UserAccountEntity userAccountEntity = new UserAccountEntity(1L, "Raluca", "Ardelean", "email", "password", role);
        when(passwordEncoder.encode(userAccountEntity.getPassword())).thenReturn(userAccountEntity.getPassword());
        StandardUserEntity standardUserEntity = new StandardUserEntity(1L, userAccountEntity, "Oradea", "Bihor", new ArrayList<>());

        when(standardUserRepository.findById(standardUserEntity.getId())).thenReturn(Optional.of(standardUserEntity));

        boolean result = userServiceInterface.delete(standardUserEntity.getId());
        assertTrue(result);

        verify(standardUserRepository).deleteById(standardUserEntity.getId());
    }

    @Test
    void testDeleteOrganizer() {
        UserService userServiceInterface = new UserService(userAccountRepository, standardUserRepository, passwordEncoder, organizerRepository);
        RoleEntity role = new RoleEntity(1, "ORGANIZER");
        UserAccountEntity userAccountEntity = new UserAccountEntity(1L, "Raluca", "Ardelean", "email", "password", role);
        when(passwordEncoder.encode(userAccountEntity.getPassword())).thenReturn(userAccountEntity.getPassword());
        OrganizerEntity organizerEntity = new OrganizerEntity(1L, userAccountEntity, new ArrayList<>());

        when(organizerRepository.findById(organizerEntity.getId())).thenReturn(Optional.of(organizerEntity));

        boolean result = userServiceInterface.deleteOrganizer(organizerEntity.getId());
        assertTrue(result);

        verify(organizerRepository).deleteById(organizerEntity.getId());
    }

    @Test
    void testUpdateUser() {
        UserService userServiceInterface = new UserService(userAccountRepository, standardUserRepository, passwordEncoder, organizerRepository);
        RoleEntity role = new RoleEntity(1, "USER");
        UserAccountEntity userAccountEntity = new UserAccountEntity(1L, "Raluca", "Ardelean", "email", "password", role);
        StandardUserEntity standardUserEntity = new StandardUserEntity(1L, userAccountEntity, "Oradea", "Bihor", new ArrayList<>());

        when(userAccountRepository.findById(standardUserEntity.getId())).thenReturn(Optional.of(userAccountEntity));
        when(standardUserRepository.findByUserAccountEntity(userAccountEntity)).thenReturn(standardUserEntity);

        StandardUserDto standardUserDto = StandardUserDto.builder()
                .firstName("Raluca")
                .lastName("Ardelean")
                .email("email")
                .password("password")
                .role("USER")
                .city("Oradea")
                .county("Bihor")
                .build();

        standardUserDto.setEmail(userAccountEntity.getEmail());
        when(passwordEncoder.encode(standardUserDto.getPassword())).thenReturn(standardUserDto.getPassword());

        StandardUserDto result = userServiceInterface.update(standardUserDto, userAccountEntity.getId());

        assertEquals(standardUserDto, result);

        verify(userAccountRepository).saveAndFlush(userAccountEntity);
        verify(standardUserRepository).saveAndFlush(standardUserEntity);
    }


    @Test
    void testUpdateOrganizer() {
        UserService userServiceInterface = new UserService(userAccountRepository, standardUserRepository, passwordEncoder, organizerRepository);
        RoleEntity role = new RoleEntity(1, "USER");
        UserAccountEntity userAccountEntity = new UserAccountEntity(1L, "Raluca", "Ardelean", "email", "password", role);
        OrganizerEntity organizerEntity = new OrganizerEntity(1L, userAccountEntity, new ArrayList<>());

        when(userAccountRepository.findById(organizerEntity.getId())).thenReturn(Optional.of(userAccountEntity));
        when(organizerRepository.findByUserAccountEntity(userAccountEntity)).thenReturn(organizerEntity);

        OrganizerDto organizerDto = OrganizerDto.builder()
                .firstName("Raluca")
                .lastName("Ardelean")
                .email("emailll")
                .password("password")
                .role("ORGANIZER")
                .events(new ArrayList<>())
                .build();
        organizerDto.setEmail(userAccountEntity.getEmail());

        when(passwordEncoder.encode(organizerDto.getPassword())).thenReturn(organizerDto.getPassword());

        OrganizerDto result = userServiceInterface.updateOrganizer(organizerDto, userAccountEntity.getId());

        assertEquals(organizerDto, result);

        verify(userAccountRepository).saveAndFlush(userAccountEntity);
        verify(organizerRepository).saveAndFlush(organizerEntity);
    }

    @Test
    void testGetAllStandardUser() {
        UserService userServiceInterface = new UserService(userAccountRepository, standardUserRepository, passwordEncoder, organizerRepository);
        RoleEntity role = new RoleEntity(1, "USER");
        UserAccountEntity userAccountEntity = new UserAccountEntity(1L, "Raluca", "Ardelean", "email", "password", role);
        StandardUserEntity standardUserEntity = new StandardUserEntity(1L, userAccountEntity, "Oradea", "Bihor", new ArrayList<>());

        StandardUserDto standardUserDto = StandardUserDto.builder()
                .firstName("Raluca")
                .lastName("Ardelean")
                .email("email")
                .password("password")
                .role("USER")
                .city("Oradea")
                .county("Bihor")
                .build();

        when(standardUserRepository.findAll()).thenReturn(List.of(standardUserEntity));
        List<StandardUserDto> result = userServiceInterface.getAllStandardUsers();

        assertEquals(List.of(standardUserDto), result);

        verify(standardUserRepository).findAll();
    }


}
