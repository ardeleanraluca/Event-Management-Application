package com.demo.project.security;

import com.demo.project.entity.RoleEntity;
import com.demo.project.entity.UserAccountEntity;
import com.demo.project.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;


/**
 * This class allows to pull users from the database user and return them in user details form.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserAccountRepository userRepository;

    /**
     * Loads the user from the users table by email. If not found, throw UsernameNotFoundException,
     * else it converts/wraps the user to a UserDetails object and return it.
     * @param email
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserAccountEntity user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Email not found"));
        return new User(user.getEmail(), user.getPassword(), mapRoleToAuthorities(user.getRole()));
    }

    /**
     * Converts a Role object into a GrantedAuthority object
     * @param role
     * @return grantedAuthority
     */
    private Collection<GrantedAuthority> mapRoleToAuthorities(RoleEntity role) {
        return Collections.singleton(new SimpleGrantedAuthority(role.getName()));
    }
}
