package com.josiel.starwars.service;

import com.josiel.starwars.config.utils.JWTUtils;
import com.josiel.starwars.model.User;
import com.josiel.starwars.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@AllArgsConstructor
@Service
public class SecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    private final AuthenticationManager authenticationManager;

    @Transactional
    public String authenticate(final String username, final String password) {
        final Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(username, password)
                );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final User user = this.loadUserByUsername(username);

        return JWTUtils.generateToken(user.getId(), user.getUsername(), user.getRole());
    }

    @Transactional
    public void authenticate(final String token) {
        final Claims claims = JWTUtils.parseToken(token);

        User user = new User();
        user.setId(Integer.parseInt(claims.getSubject()));
        user.setPassword("");
        user.setUsername(claims.get(JWTUtils.TOKEN_CLAIM_USERNAME).toString());
        user.setRole(claims.get(JWTUtils.TOKEN_CLAIM_ROLES).toString());

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );
    }

    @Transactional
    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Transactional
    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid username or password."));
    }

}
