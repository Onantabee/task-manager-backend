package com.tskmgmnt.rhine.controller;

import com.tskmgmnt.rhine.dto.UserLogReq;
import com.tskmgmnt.rhine.service.UserLoginService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
@Tag(
        name = "CRUD REST APIs for User Authentication And Login",
        description = "CRUD REST APIs - User Login"
)
public class UserLoginController {
    private final UserLoginService userLoginService;


    public UserLoginController(UserLoginService userLoginService) {
        this.userLoginService = userLoginService;
    }

    @Operation(
            summary = "Authenticate user",
            description = "Authenticates a user with their email and password and returns a token/session ID",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Authentication successful",
                            content = @Content(
                                    schema = @Schema(implementation = String.class),
                                    examples = @ExampleObject(value = "\"eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...\"")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid request format or missing parameters"
                    ),
                    @ApiResponse(
                            responseCode = "401",
                            description = "Unauthorized - Invalid credentials"
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal server error"
                    )
            }
    )
    @PostMapping(path = "/login")
    public String login(@RequestBody UserLogReq loginRequest) {
        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();
        return userLoginService.loginUser(username, password);
    }
}
