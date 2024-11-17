package com.claudioscagliotti.thesis.service.impl;

import com.claudioscagliotti.thesis.model.UserEntity;
import com.claudioscagliotti.thesis.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of {@link UserDetailsService} to load user-specific data.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    /**
     * Constructs a UserDetailsServiceImpl with the specified UserRepository.
     *
     * @param userRepository The UserRepository to be used.
     */
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Locates the user based on the username.
     *
     * @param username The username identifying the user whose data is required.
     * @return A fully populated user record (never null).
     * @throws UsernameNotFoundException if the user could not be found or the user has no GrantedAuthority.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userEntityOpt = userRepository.findByUsername(username);
        if (userEntityOpt.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        UserEntity userEntity = userEntityOpt.get();
        return User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .roles(userEntity.getRole().name())
                .build();
    }
}
