package com.rayan.easy_ecommerce.user;

import com.rayan.easy_ecommerce.infra.exceptions.custom.UserAlreadyExistsException;
import com.rayan.easy_ecommerce.infra.exceptions.custom.UserNotFoundException;
import com.rayan.easy_ecommerce.user.dto.CreateUserRequestPayload;
import com.rayan.easy_ecommerce.user.dto.UpdateUserRequestPayload;
import jakarta.transaction.Transactional;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {


    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Transactional
    public Long createUser(CreateUserRequestPayload payload) {
        validateUserEmail(payload.email());
        User newUser = new User(null, payload.name(), payload.email(), payload.phone(), payload.password());
        userRepository.save(newUser);
        return newUser.getId();
    }


    public User getUser(Long userId) {
        return this.userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
    }


    public User getUser(String email) {
        return this.userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }


    public Page<User> getAllUsers(Pageable pageable) {
        return this.userRepository.findAll(pageable);
    }


    @Transactional
    public Long updateUser(Long userId, UpdateUserRequestPayload payload) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));
        updateUserFields(user, payload);
        userRepository.save(user);
        return user.getId();
    }


    @Transactional
    public void deleteUser(Long userId) {
        if (!this.userRepository.existsById(userId)) throw new UserNotFoundException("User not found with id: " + userId);
        userRepository.deleteById(userId);
    }


    private void validateUserEmail(String email) {
        Optional<User> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            throw new UserAlreadyExistsException("There is already a user registered with this email.");
        }
    }


    private void updateUserFields(User user, UpdateUserRequestPayload payload) {
        user.setName(payload.name());
        user.setPhone(payload.phone());
        user.setPassword(payload.password());
    }
}
