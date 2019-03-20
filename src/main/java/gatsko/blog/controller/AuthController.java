package gatsko.blog.controller;


import gatsko.blog.model.*;
import gatsko.blog.model.DTO.JwtAuthenticationResponse;
import gatsko.blog.model.DTO.LoginRequest;
import gatsko.blog.model.DTO.RegistrationRequest;
import gatsko.blog.security.JwtProvider;
import gatsko.blog.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtProvider tokenProvider;

//    @Autowired
//    private ApplicationEventPublisher applicationEventPublisher;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid RegistrationRequest registrationRequest) {
        HttpHeaders responseHeader = new HttpHeaders();
        authService.registerUser(registrationRequest);
        return new ResponseEntity<>(registrationRequest, responseHeader, HttpStatus.CREATED);
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid LoginRequest loginRequest) {
        Optional<Authentication> authenticationOpt = authService.authenticateUser(loginRequest);
        Authentication authentication = authenticationOpt.get();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = authService.generateToken(customUserDetails);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwtToken, tokenProvider.getExpiryDuration()));
    }
}