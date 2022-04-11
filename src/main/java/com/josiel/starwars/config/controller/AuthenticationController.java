package com.josiel.starwars.config.controller;

import com.josiel.starwars.config.resources.AuthenticationResponse;
import com.josiel.starwars.config.resources.CredentialsDTO;
import com.josiel.starwars.service.SecurityService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class AuthenticationController {

    private final SecurityService securityService;

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid CredentialsDTO credentialsDTO) {
        final String username = credentialsDTO.getUsername();
        final String password = credentialsDTO.getPassword();

        final String token = securityService.authenticate(username, password);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }
}
