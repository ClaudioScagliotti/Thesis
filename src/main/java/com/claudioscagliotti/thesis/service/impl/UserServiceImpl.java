package com.claudioscagliotti.thesis.service.impl;

import com.claudioscagliotti.thesis.dto.request.PasswordChangeRequest;
import com.claudioscagliotti.thesis.dto.request.PasswordResetRequest;
import com.claudioscagliotti.thesis.enumeration.RoleEnum;
import com.claudioscagliotti.thesis.exception.UnauthorizedUserException;
import com.claudioscagliotti.thesis.model.GoalEntity;
import com.claudioscagliotti.thesis.model.UserEntity;
import com.claudioscagliotti.thesis.repository.UserRepository;
import com.claudioscagliotti.thesis.service.UserService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing user-related operations.
 */
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AuthenticationServiceImpl authenticationService;
    private final EmailServiceImpl emailService;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs a UserService with the specified UserRepository and AuthenticationService.
     *
     * @param userRepository        The UserRepository to be used.
     * @param authenticationService The AuthenticationService to be used.
     * @param emailService          The EmailService to be used.
     * @param passwordEncoder       The PasswordEncoder to be used.
     */
    public UserServiceImpl(UserRepository userRepository, AuthenticationServiceImpl authenticationService, EmailServiceImpl emailService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.authenticationService = authenticationService;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Saves a user entity.
     *
     * @param userEntity The user entity to save.
     */
    @Transactional
    public void saveUser(UserEntity userEntity) {
         this.userRepository.save(userEntity);
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
     * Finds a user by username and email.
     *
     * @param username The username to search for.
     * @param email The email to search for.
     * @return The found UserEntity.
     * @throws UsernameNotFoundException If no user is found with the specified username.
     */
    public UserEntity findByUsernameAndEmail(String username, String email) {
        Optional<UserEntity> userEntity = userRepository.findByUsernameAndEmail(username, email);
        if (userEntity.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username+ "and email: "+email);
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
     * Updates the user's streak.
     *
     * @param userEntity   The UserEntity.
     * @param updateOrReset   The Boolean parameter that indicate if the streak must be increased or reset.
     */
    public void updateUserStreak(UserEntity userEntity, Boolean updateOrReset) {
        if(updateOrReset){
            userEntity.setStreak(userEntity.getStreak() + 1);
        } else {
            userEntity.setStreak(0);
        }

        userRepository.save(userEntity);
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
    /**
     * Retrieves all users with a specific role.
     *
     * This method fetches all users from the repository that have the specified role.
     *
     * @param roleEnum the role of the users to retrieve
     * @return a list of UserEntity objects representing users with the specified role
     */
    public List<UserEntity> getAllUsersWithRole(RoleEnum roleEnum){
        return userRepository.getAllUserByRole(roleEnum);
    }

    /**
     * Initiates the password reset process for a user.
     *
     * This method finds the user by their username and email, generates a reset token,
     * and sends an email to the user with instructions to reset their password.
     *
     * @param request The PasswordResetRequest containing the username and email of the user.
     */
    public void resetUserPassword(PasswordResetRequest request){
        UserEntity user = findByUsernameAndEmail(request.getUsername(), request.getEmail());
        String token= authenticationService.resetPassword(user);
        sendPasswordResetEmail(user, token);
    }

    /**
     * Sends an email to the user with instructions to reset their password.
     *
     * This method constructs the email content, including the reset link and token,
     * and sends it to the user's email address.
     *
     * @param userEntity The UserEntity representing the user.
     * @param token The reset password token to include in the email.
     */
    private void sendPasswordResetEmail(UserEntity userEntity, String token) {
        String text="Ciao "+userEntity.getFirstName()+", \n"
                +"Abbiamo ricevuto una richiesta di reset password." +
                "Se sei stato tu a richiederla, procedi impostando una nuova password usando il link sottostante: \n"
                +"http://localhost:8080/thesis/api/user/new-password \n" +
                " indicando come header Authorization il token: Bearer "+token+"\n e come body un campo password contenente la nuova password";
        emailService.sendSimpleMessage(userEntity.getEmail(), "Reset password", text);
    }

    /**
     * Changes the user's password.
     *
     * @param request The request containing the new password.
     */
    public void changePassword(String username, PasswordChangeRequest request)  {
        UserEntity user = findByUsername(username);
        String encodedPassword = passwordEncoder.encode(request.getPassword());
        user.setPassword(encodedPassword);
        saveUser(user);
    }

    /**
     * Check if the user has the ADMIN role.
     */
    public void checkIsAdmin(){
        String adminRole = "ROLE_ADMIN";
        if(!authenticationService.hasRole(adminRole)){
            throw new UnauthorizedUserException("The user role must be: ADMIN");
        }
    }
}
