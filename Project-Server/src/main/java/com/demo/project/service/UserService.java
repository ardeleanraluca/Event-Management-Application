package com.demo.project.service;

import com.demo.project.dto.OrganizerDto;
import com.demo.project.dto.StandardUserDto;
import com.demo.project.entity.OrganizerEntity;
import com.demo.project.entity.StandardUserEntity;
import com.demo.project.entity.UserAccountEntity;
import com.demo.project.repository.OrganizerRepository;
import com.demo.project.repository.StandardUserRepository;
import com.demo.project.repository.UserAccountRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * This class handles all requests related to users, whatever if they are standard users or organizers.
 */
@Component
@NoArgsConstructor
@AllArgsConstructor
public class UserService implements UserServiceInterface {
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private StandardUserRepository standardUserRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OrganizerRepository organizerRepository;

    /**
     * Deletes a user from the database, if it exists.
     *
     * @param id
     * @return boolean - true if the user was deleted successfully, otherwise false.
     */
    public boolean delete(Long id) {
        if (standardUserRepository.findById(id).isEmpty()) {
            return false;
        }

        standardUserRepository.deleteById(id);
        return true;
    }

    /**
     * Deletes an organizer with his events from the database, if it exists.
     *
     * @param id
     * @return boolean - true if the user was deleted successfully, otherwise false.
     */
    public boolean deleteOrganizer(Long id) {
        if (organizerRepository.findById(id).isEmpty()) {
            return false;
        }

        organizerRepository.deleteById(id);
        return true;
    }

    /**
     * Updates a user's data in database, if it exists.
     *
     * @param newUser an object that contains all the details of a standard user that will be updated.
     * @param id      The user ID that is being updated.
     * @return StandardUserDto - the updated user if the user was updated successfully, otherwise null.
     */
    public StandardUserDto update(StandardUserDto newUser, Long id) {
        if (userAccountRepository.findById(id).isEmpty()) {
            return null;
        }

        UserAccountEntity userEntity = userAccountRepository.findById(id).get();
        StandardUserEntity standardUserEntity = standardUserRepository.findByUserAccountEntity(userEntity);

        userEntity.setFirstName(newUser.getFirstName());
        userEntity.setLastName(newUser.getLastName());
        userEntity.setPassword(passwordEncoder.encode((newUser.getPassword())));
        userAccountRepository.saveAndFlush(userEntity);

        standardUserEntity.setUserAccountEntity(userEntity);
        standardUserEntity.setCounty(newUser.getCounty());
        standardUserEntity.setCity(newUser.getCity());
        standardUserRepository.saveAndFlush(standardUserEntity);

        return new StandardUserDto(standardUserEntity);
    }

    /**
     * Updates an organizer's data in database, if it exists.
     *
     * @param newOrganizer an object that contains all the details of a standard user that will be updated.
     * @param id           The user ID that is being updated.
     * @return OrganizerDto - the updated organizer if the user was updated successfully, otherwise null.
     */
    public OrganizerDto updateOrganizer(OrganizerDto newOrganizer, Long id) {
        if (userAccountRepository.findById(id).isEmpty()) {
            return null;
        }

        UserAccountEntity userEntity = userAccountRepository.findById(id).get();
        OrganizerEntity organizerEntity = organizerRepository.findByUserAccountEntity(userEntity);

        userEntity.setFirstName(newOrganizer.getFirstName());
        userEntity.setLastName(newOrganizer.getLastName());
        userEntity.setPassword(passwordEncoder.encode((newOrganizer.getPassword())));
        userAccountRepository.saveAndFlush(userEntity);

        organizerEntity.setUserAccountEntity(userEntity);
        organizerRepository.saveAndFlush(organizerEntity);

        return new OrganizerDto(organizerEntity);
    }

    /**
     * Finds all standard user in database and returns them
     *
     * @return all standard users from database
     */
    public List<StandardUserDto> getAllStandardUsers() {
        List<StandardUserEntity> users = standardUserRepository.findAll();
        return users.stream().map(StandardUserDto::new).toList();
    }

    /**
     * Finds the organizer by id in database and returns it
     *
     * @return the organizer with given id
     */
    public OrganizerDto getOrgbyId(Long id){
        return new OrganizerDto(organizerRepository.findById(id).get());
    }

    /**
     * Finds all organizers in database and returns them
     *
     * @return all organizers from database
     */
    public List<OrganizerDto> getAllOrganizers(){
        List<OrganizerEntity> organizerEntities = organizerRepository.findAll();
        return organizerEntities.stream().map(OrganizerDto::new).toList();
    }
}
