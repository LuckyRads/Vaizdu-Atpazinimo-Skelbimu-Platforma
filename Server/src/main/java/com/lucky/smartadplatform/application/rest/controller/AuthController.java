package com.lucky.smartadplatform.application.rest.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.lucky.smartadplatform.application.rest.model.GenericResponse;
import com.lucky.smartadplatform.application.rest.model.JwtResponse;
import com.lucky.smartadplatform.application.rest.model.LoginRequest;
import com.lucky.smartadplatform.application.rest.model.RegisterRequest;
import com.lucky.smartadplatform.application.rest.model.StatusResponse;
import com.lucky.smartadplatform.application.rest.model.StatusResponse.StatusCode;
import com.lucky.smartadplatform.domain.repository.UserRepository;
import com.lucky.smartadplatform.domain.service.AuthService;
import com.lucky.smartadplatform.infrastructure.model.jpa.JpaUser;
import com.lucky.smartadplatform.infrastructure.security.jwt.JwtUtils;
import com.lucky.smartadplatform.infrastructure.security.service.UserDetailsImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserRepository<JpaUser> userRepository;

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<GenericResponse<JwtResponse>> authenticateUser(
            @Valid @RequestBody LoginRequest loginRequest) {
        var response = new GenericResponse<JwtResponse>();
        boolean areCredentialsValid = authService.areCredentialsValid(loginRequest.getUsername(),
                loginRequest.getPassword());
        if (!areCredentialsValid) {
            response.setMessage("Invalid credentials.");
            response.setStatus(StatusCode.FAILED);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        response.setData(
                new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), roles));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<StatusResponse> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return new ResponseEntity<>(
                    new StatusResponse(StatusCode.FAILED, "Username is already taken."), HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            return new ResponseEntity<>(new StatusResponse(StatusCode.FAILED, "Email is already in use."),
                    HttpStatus.BAD_REQUEST);
        }

        authService.createUser(registerRequest.getUsername(), registerRequest.getEmail(), registerRequest.getPassword(),
                registerRequest.getValidationPassword());
        return new ResponseEntity<>(new StatusResponse("User registered successfully."), HttpStatus.CREATED);
    }

}
