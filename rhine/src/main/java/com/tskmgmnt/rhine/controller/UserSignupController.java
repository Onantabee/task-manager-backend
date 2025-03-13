package com.tskmgmnt.rhine.controller;

import com.tskmgmnt.rhine.dto.UserRegReq;
import com.tskmgmnt.rhine.entities.User;
import com.tskmgmnt.rhine.enums.UserRole;
import com.tskmgmnt.rhine.services.UserRegistrationService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserSignupController {

    private final UserRegistrationService userRegistrationService;

    public UserSignupController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @PostMapping(path = "/register")
    public String register (@RequestBody UserRegReq request) {
        return userRegistrationService.createUser(request).getEmail();
    }

    @PostMapping(path = "/update-role")
    public User updateUserRole (@RequestBody UserRegReq request){
        return userRegistrationService.updateUserRole(request.getEmail(), request.getUserRole());
    }

}
