package com.tskmgmnt.rhine.controller;

import com.tskmgmnt.rhine.dto.UserReq;
import com.tskmgmnt.rhine.dto.UserRes;
import com.tskmgmnt.rhine.entity.User;
import com.tskmgmnt.rhine.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public List<User> getAllUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("/{email}")
    public UserReq getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    @GetMapping("/non-admin")
    public List<UserRes> getNonAdminUsers() {
        return userService.getNonAdminUsers();
    }
}
