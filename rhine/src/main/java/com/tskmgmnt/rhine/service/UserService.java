package com.tskmgmnt.rhine.service;

import com.tskmgmnt.rhine.dto.UserReq;
import com.tskmgmnt.rhine.dto.UserRes;
import com.tskmgmnt.rhine.entity.User;
import com.tskmgmnt.rhine.enums.UserRole;
import com.tskmgmnt.rhine.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public UserReq getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new UserReq(user.getEmail(), user.getName(), user.getUserRole());
    }

    public List<UserRes> getNonAdminUsers() {
        return userRepository.findByUserRoleNot(UserRole.ADMIN)
                .stream()
                .map(user -> new UserRes(user.getEmail(), user.getName(), user.getUserRole()))
                .collect(Collectors.toList());
    }
}