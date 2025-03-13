package com.tskmgmnt.rhine.controller;

import com.tskmgmnt.rhine.dto.UserLogReq;
import com.tskmgmnt.rhine.services.UserLoginService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserLoginController {
    private final UserLoginService userLoginService;


    public UserLoginController(UserLoginService userLoginService) {
        this.userLoginService = userLoginService;
    }

    @PostMapping(path = "/login")
    public String login(@RequestBody UserLogReq loginRequest) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        return userLoginService.loginUser(username, password);
    }
}
