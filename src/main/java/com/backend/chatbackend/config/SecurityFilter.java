package com.backend.chatbackend.config;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.backend.chatbackend.models.UserModel;
import com.backend.chatbackend.repositories.UserRepository;
import com.backend.chatbackend.services.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class SecurityFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private UserRepository userRepository;

    public SecurityFilter(JwtService jwtService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = getToken(request);

        if (jwt != null) {
            String userId = this.jwtService.validateJwt(jwt);

            Optional<UserModel> findUserModelResponse = this.userRepository.findById(UUID.fromString(userId));

            if (findUserModelResponse.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    public String getToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null) {
            return null;
        }

        return authHeader.replace("Bearer ", "");
    }
    
}
