package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.mapper.UserMapper;
import com.claudioscagliotti.thesis.model.UserEntity;
import com.claudioscagliotti.thesis.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    public UserDetailsServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
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
                //.roles(userEntity.getRoles().toArray(new String[0]))
                .build();
    }
}
