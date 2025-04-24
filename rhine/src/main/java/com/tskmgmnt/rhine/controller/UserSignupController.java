package com.tskmgmnt.rhine.controller;

import com.tskmgmnt.rhine.dto.UserRegReq;
import com.tskmgmnt.rhine.entity.User;
import com.tskmgmnt.rhine.service.UserRegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173")
@Tag(
        name = "CRUD REST APIs for User Registration",
        description = "CRUD REST APIs - Create Account"
)
public class UserSignupController {

    private final UserRegistrationService userRegistrationService;

    public UserSignupController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    @Operation(
            summary = "Register a new user",
            description = "Creates a new user account with the provided details",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully registered user"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "409", description = "User already exists")
            }
    )
    @PostMapping(path = "/register")
    public String register (@RequestBody UserRegReq request) {
        return userRegistrationService.createUser(request).getEmail();
    }

    @Operation(
            summary = "Update user role",
            description = "Updates the role of an existing user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successfully updated user role"),
                    @ApiResponse(responseCode = "400", description = "Invalid input data"),
                    @ApiResponse(responseCode = "404", description = "User not found")
            }
    )
    @PostMapping(path = "/update-role")
    public User updateUserRole (@RequestBody UserRegReq request){
        return userRegistrationService.updateUserRole(request.getEmail(), request.getUserRole());
    }

}
