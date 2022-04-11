package com.josiel.starwars.config.resources;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class AuthenticationResponse {

    private final String token;
}
