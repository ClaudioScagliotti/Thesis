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

/**
 * Service class for managing user-related operations.
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    /**
     * Constructs a UserService with the specified UserRepository and UserMapper.
     *
     * @param userRepository The UserRepository to be used.
     * @param userMapper     The UserMapper to be used.
     */
    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    /**
     * Registers a new user.
     *
     * @param user The registration request containing user details.
     * @return The saved UserEntity.
     */
    public UserEntity registerUser(RegisterRequest user) {
        UserEntity userEntity = userMapper.toUserEntity(user);
        return this.userRepository.save(userEntity);
    }

    /**
     * Saves a user entity.
     *
     * @param userEntity The user entity to save.
     * @return The saved UserEntity.
     */
    @Transactional
    public UserEntity saveUser(UserEntity userEntity) {
        return this.userRepository.save(userEntity);
    }

    /**
     * Finds a user by username.
     *
     * @param username The username to search for.
     * @return The found UserEntity.
     * @throws UsernameNotFoundException If no user is found with the specified username.
     */
    public UserEntity findByUsername(String username) {
        Optional<UserEntity> userEntity = userRepository.findByUsername(username);
        if (userEntity.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        } else {
            return userEntity.get();
        }
    }

    /**
     * Updates the user's goal.
     *
     * @param username   The username of the user.
     * @param goalEntity The new goal entity to be set.
     */
    public void updateUserGoal(String username, GoalEntity goalEntity) {
        UserEntity userEntity = findByUsername(username);
        userEntity.setGoalEntity(goalEntity);
        userRepository.updateUserGoal(userEntity.getId(), goalEntity.getId());
    }

    /**
     * Updates the user's courses.
     *
     * @param userId              The ID of the user.
     * @param newCourseEntityList The new list of course entities to be set.
     */
    @Transactional
    public void updateUserCourses(Long userId, List<CourseEntity> newCourseEntityList) {
        userRepository.deleteUserCourses(userId);
        for (CourseEntity courseEntity : newCourseEntityList) {
            userRepository.addUserCourse(userId, courseEntity.getId());
        }
    }

    /**
     * Adds points to the user's total points.
     *
     * @param userEntity The user entity to update.
     * @param points     The number of points to add.
     */
    public void addPoints(UserEntity userEntity, int points) {
        int totalPoints = userEntity.getPoints() + points;
        userEntity.setPoints(totalPoints);
        userRepository.save(userEntity);
    }
}
