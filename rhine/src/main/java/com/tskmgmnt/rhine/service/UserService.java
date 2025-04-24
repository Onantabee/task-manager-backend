package com.tskmgmnt.rhine.service;

import com.tskmgmnt.rhine.dto.UserReq;
import com.tskmgmnt.rhine.dto.UserRes;
import com.tskmgmnt.rhine.entity.User;
import com.tskmgmnt.rhine.enums.UserRole;
import com.tskmgmnt.rhine.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    public UserRes updateUserDetails(String userEmail, UserRes user){
        User existingUser = userRepository.findById(userEmail)
                .orElseThrow(() -> new RuntimeException("User not Found"));

//        if (passwordEncoder.matches(newPassword, user.getName())) {
//            throw new RuntimeException("New password cannot be the same as the old password.");
//        }

        existingUser.setName(user.getName());
        existingUser.setUserRole(user.getUserRole());
        User savedUser = userRepository.save(existingUser);
        return mapToUserResponse(savedUser);
    }

    private UserRes mapToUserResponse(User user) {
        UserRes response = new UserRes();
        response.setEmail(user.getEmail());
        response.setName(user.getName());
        response.setUserRole(user.getUserRole());
        return response;
    }

    public String changePassword(String email, String currentPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (!passwordEncoder.matches(currentPassword, user.getPwd())) {
            throw new RuntimeException("Input Correct password");
        }

        if (passwordEncoder.matches(newPassword, user.getPwd())) {
            throw new RuntimeException("New password cannot be the same as the old password.");
        }

        user.setPwd(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return "Password changed successfully.";
    }

//    public UserReq updateIsNewState(String email, UserReq userReq){
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new RuntimeException("Task not Found!"));
//        user.setList(userReq.isList());
//        User updatedViewState = userRepository.save(user);
//        UserReq userReq1 = new UserReq(updatedViewState);
//        messagingTemplate.convertAndSend("/topic/user-task-view-state",
//                new NotificationDto<>("USER_TASK_VIEW_STATE", (updatedTaskState)));
//        return mapToTaskResponse(updatedTaskState);
//    }
//
//    public TaskDto getIsNewState(Long id) {
//        Task task = taskRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Task not found!"));
//
//        TaskDto response = new TaskDto();
//        response.setIsNew(task.getIsNew());
//
//        return response;
//    }
}