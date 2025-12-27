package com.demo.project.service;

import com.demo.project.dto.*;
import com.demo.project.entity.OrganizerEntity;
import com.demo.project.entity.RoleEntity;
import com.demo.project.entity.StandardUserEntity;
import com.demo.project.entity.UserAccountEntity;
import com.demo.project.repository.OrganizerRepository;
import com.demo.project.repository.RoleRepository;
import com.demo.project.repository.StandardUserRepository;
import com.demo.project.repository.UserAccountRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;


/**
 * This class handles all the requests related to authentication and registration in application.
 */
@Component
@AllArgsConstructor
@NoArgsConstructor
public class AuthService implements AuthServiceInterface {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private StandardUserRepository standardUserRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
//    @Autowired
//    private JWTGenerator jwtGenerator;
    @Autowired
    private OrganizerRepository organizerRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    /**
     * Registers a standard user and adds it into database.
     *
     * @param standardUserDto an object that contains all the details of a standard user
     * @return boolean - If the user was successfully added in the database it true, otherwise
     * the registration will not succeed and it returns false.
     */
    @Transactional
    public StandardUserDto register(StandardUserDto standardUserDto) {
        if (userAccountRepository.existsByEmail(standardUserDto.getEmail())) {
            return null;
        }

        UserAccountEntity userAcc = new UserAccountEntity();
        userAcc.setFirstName(standardUserDto.getFirstName());
        userAcc.setLastName(standardUserDto.getLastName());
        userAcc.setEmail(standardUserDto.getEmail().toLowerCase());
        userAcc.setPassword(passwordEncoder.encode((standardUserDto.getPassword())));
        RoleEntity role = roleRepository.findByName("USER").get();
        userAcc.setRole(role);
        userAccountRepository.saveAndFlush(userAcc);

        StandardUserEntity user = new StandardUserEntity();
        user.setUserAccountEntity(userAcc);
        user.setCounty(standardUserDto.getCounty());
        user.setCity(standardUserDto.getCity());
        user.setBoughtTickets(new ArrayList<>());
        standardUserRepository.saveAndFlush(user);

        String message = "Welcome " + standardUserDto.getFirstName() + " " + standardUserDto.getLastName();
        EmailEvent emailEvent = new EmailEvent(this, standardUserDto.getEmail(), "Registration", message);
        applicationEventPublisher.publishEvent(emailEvent);

        return new StandardUserDto(user);
    }


    /**
     * Registers a organizer and adds it into database.
     *
     * @param organizerDto an object that contains all the details of an organizer
     * @return boolean - If the organizer was successfully added in the database it returns true, otherwise
     * the registration will not succeed and it returns false;
     */
    @Transactional
    public OrganizerDto registerOrganizer(OrganizerDto organizerDto) {
        if (userAccountRepository.existsByEmail(organizerDto.getEmail())) {
            return null;
        }

        UserAccountEntity userAcc = new UserAccountEntity();
        userAcc.setEmail(organizerDto.getEmail());
        userAcc.setFirstName(organizerDto.getFirstName());
        userAcc.setLastName(organizerDto.getLastName());

        userAcc.setPassword(passwordEncoder.encode((organizerDto.getPassword())));
        RoleEntity role = roleRepository.findByName("ORGANIZER").get();
        userAcc.setRole(role);
        userAccountRepository.saveAndFlush(userAcc);

        OrganizerEntity organizer = new OrganizerEntity();
        organizer.setUserAccountEntity(userAcc);
        organizer.setEvents(new ArrayList<>());
        organizerRepository.saveAndFlush(organizer);

        String message = "Welcome " + organizerDto.getFirstName() + " " + organizerDto.getLastName();
        EmailEvent emailEvent = new EmailEvent(this, organizerDto.getEmail(), "Registration", message);
        applicationEventPublisher.publishEvent(emailEvent);

        return new OrganizerDto(organizer);
    }


    /**
     * If the principal of the input authentication is valid and verified, AuthenticationManager#authenticate returns an Authentication instance with the authenticated flag set to true and. In this case it is send in the SecurityContextHolder and the token is generated based on it.
     * Otherwise, if the principal is not valid, it will throw an AuthenticationException and that means the user can not log in application because the email or password are wrong.
     *
     * @param userLoginDto email and password
     * @return UserAccountDto - user's details if the user login successfully, otherwise it returns 401 Unauthorized.
     */
    public UserAccountDto login(UserLoginDto userLoginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        userLoginDto.getEmail().toLowerCase(),
                        userLoginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println(authentication.getDetails());
//        String token = jwtGenerator.generateToken(authentication);

        UserAccountEntity userAccount = userAccountRepository.findByEmail(userLoginDto.getEmail().toLowerCase()).get();

        String roleType = String.valueOf(authentication.getAuthorities().stream().toList().get(0));
        if (roleType.equals("ORGANIZER")) {
            return new OrganizerDto(organizerRepository.findByUserAccountEntity(userAccount));

        } else if (roleType.equals("USER")) {
            return new StandardUserDto(standardUserRepository.findByUserAccountEntity(userAccount));
        } else {
            return new UserAccountDto(userAccount);
        }
    }
}
