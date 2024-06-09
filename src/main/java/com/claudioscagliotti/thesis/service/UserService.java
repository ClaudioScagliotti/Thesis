package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.request.RegisterRequest;
import com.claudioscagliotti.thesis.mapper.UserMapper;
import com.claudioscagliotti.thesis.model.GoalEntity;
import com.claudioscagliotti.thesis.model.UserEntity;
import com.claudioscagliotti.thesis.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    public UserEntity registerUser(RegisterRequest user) {
        UserEntity userEntity = userMapper.toUserEntity(user);
        return this.userRepository.save(userEntity);
    }
    @Transactional
    public UserEntity saveUser(UserEntity userEntity){
        return this.userRepository.save(userEntity);
    }
    public Optional<UserEntity> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    public void updateUserGoal(String username, GoalEntity goalEntity) {
        Optional<UserEntity> userEntity= findByUsername(username);
        userEntity.ifPresent(entity -> entity.setGoalEntity(goalEntity));
        userEntity.ifPresent(t -> userRepository.updateUser(t.getId(), goalEntity.getId()));
    }
}
