package com.tskmgmnt.rhine.services;

import com.tskmgmnt.rhine.dto.UserRegReq;
import com.tskmgmnt.rhine.entities.User;
import com.tskmgmnt.rhine.enums.UserRole;
import com.tskmgmnt.rhine.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserRegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(UserRegReq userRegReq) {
        if (userRepository.findByEmail(userRegReq.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = new User();
        user.setName(userRegReq.getName());
        user.setEmail(userRegReq.getEmail());
        user.setPwd(passwordEncoder.encode(userRegReq.getPwd()));
        user.setUserRole(UserRole.EMPLOYEE); // by default

        return userRepository.save(user);
    }

    public User updateUserRole(String email, UserRole role){
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            User existingUser = user.get();
            existingUser.setUserRole(role);
            return userRepository.save(existingUser);
        } else {
            throw new IllegalArgumentException("User with email " + email + " not found");
        }
}
}
