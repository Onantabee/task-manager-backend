package com.tskmgmnt.rhine.services;

import com.tskmgmnt.rhine.entities.User;
import com.tskmgmnt.rhine.enums.UserRole;
import com.tskmgmnt.rhine.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not Found"));
    }

    public List<User> getNonAdminUsers() {
        return userRepository.findByUserRoleNot(UserRole.ADMIN);
    }
}
