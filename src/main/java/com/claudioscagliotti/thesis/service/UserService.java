package com.claudioscagliotti.thesis.service;

import com.claudioscagliotti.thesis.dto.request.RegisterRequest;
import com.claudioscagliotti.thesis.mapper.UserMapper;
import com.claudioscagliotti.thesis.model.CourseEntity;
import com.claudioscagliotti.thesis.model.GoalEntity;
import com.claudioscagliotti.thesis.model.UserEntity;
import com.claudioscagliotti.thesis.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public UserEntity findByUsername(String username) {
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        if (userEntity.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        } else{
            return userEntity.get();
        }
    }
    public void updateUserGoal(String username, GoalEntity goalEntity) {
        UserEntity userEntity= findByUsername(username);
        userEntity.setGoalEntity(goalEntity);
        userRepository.updateUserGoal(userEntity.getId(), goalEntity.getId());
    }
    @Transactional
    public void updateUserCourses(Long  userId, List<CourseEntity> newCourseEntityList) {
        userRepository.deleteUserCourses(userId);
        for (CourseEntity courseEntity : newCourseEntityList) {
            userRepository.addUserCourse(userId, courseEntity.getId());
        }
    }
}
