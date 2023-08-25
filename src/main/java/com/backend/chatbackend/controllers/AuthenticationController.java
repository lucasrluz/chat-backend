package com.backend.chatbackend.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.chatbackend.dtos.SignUpDTORequest;
import com.backend.chatbackend.dtos.SignUpDTOResponse;
import com.backend.chatbackend.services.AuthenticationService;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("signup")
    public ResponseEntity<Object> signup(@RequestBody SignUpDTORequest signUpDTORequest) {
        try {
            SignUpDTOResponse signUpDTOResponse = this.authenticationService.signup(signUpDTORequest);

            return ResponseEntity.status(HttpStatus.CREATED).body(signUpDTOResponse);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        }
    }
}
