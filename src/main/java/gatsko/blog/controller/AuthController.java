package gatsko.blog.controller;


import gatsko.blog.exception.UserLoginException;
import gatsko.blog.model.*;
import gatsko.blog.model.DTO.JwtAuthenticationResponse;
import gatsko.blog.model.DTO.LoginRequest;
import gatsko.blog.model.DTO.RegistrationRequest;
import gatsko.blog.security.JwtProvider;
import gatsko.blog.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtProvider tokenProvider;

    public AuthController(AuthService authService, JwtProvider jwtProvider) {
        this.authService = authService;
        this.tokenProvider = jwtProvider;
    }

//    @Autowired
//    private ApplicationEventPublisher applicationEventPublisher;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@Valid RegistrationRequest registrationRequest) {
        authService.registerUser(registrationRequest);
    }


    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Optional<Authentication> authenticationOpt = authService.authenticateUser(loginRequest);
        authenticationOpt.orElseThrow(() -> new UserLoginException("Couldn't login user [" + loginRequest + "]"));
        Authentication authentication = authenticationOpt.get();
        CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwtToken = authService.generateToken(customUserDetails);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwtToken, tokenProvider.getExpiryDuration()));
    }
}