package com.tskmgmnt.rhine.services;

import com.tskmgmnt.rhine.entities.User;
import com.tskmgmnt.rhine.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserLoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserLoginService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String loginUser (String email, String rawPassword) {
        User user = userRepository.findByEmail(String.valueOf(email))
                .orElseThrow(() -> new IllegalStateException("Invalid email"));
        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalStateException("Invalid Password");
        }
        return "Login successful for user: " + email;
    }
}
