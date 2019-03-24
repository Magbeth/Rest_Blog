package gatsko.blog.controller;


import gatsko.blog.event.OnRegenerateEmailVerificationEvent;
import gatsko.blog.event.OnUserRegistrationCompleteEvent;
import gatsko.blog.exception.InvalidTokenRequestException;
import gatsko.blog.exception.UserLoginException;
import gatsko.blog.model.*;
import gatsko.blog.model.dto.JwtAuthenticationResponse;
import gatsko.blog.model.dto.LoginRequest;
import gatsko.blog.model.dto.RegistrationRequest;
import gatsko.blog.model.Token.EmailVerificationToken;
import gatsko.blog.security.JwtProvider;
import gatsko.blog.service.AuthService;
import gatsko.blog.service.EmailVerificationTokenService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtProvider tokenProvider;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final EmailVerificationTokenService emailVerificationTokenService;

    public AuthController(AuthService authService, JwtProvider jwtProvider,
                          ApplicationEventPublisher applicationEventPublisher,
                          EmailVerificationTokenService emailVerificationTokenService) {
        this.authService = authService;
        this.tokenProvider = jwtProvider;
        this.applicationEventPublisher = applicationEventPublisher;
        this.emailVerificationTokenService = emailVerificationTokenService;

    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void registerUser(@Valid @RequestBody RegistrationRequest registrationRequest, WebRequest request) {
        Optional<User> registeredUserOpt = authService.registerUser(registrationRequest);
        registeredUserOpt.orElseThrow(RuntimeException::new);
        String appUrl = request.getContextPath();
        OnUserRegistrationCompleteEvent onUserRegistrationCompleteEvent =
                new OnUserRegistrationCompleteEvent(registeredUserOpt.get(), appUrl, request.getLocale());
        applicationEventPublisher.publishEvent(onUserRegistrationCompleteEvent);
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

    @GetMapping("/registrationConfirm")
    @ResponseStatus(value = HttpStatus.OK)
    public void confirmRegistration(@RequestParam("token") String token) {
        Optional<User> verifiedUserOpt = authService.confirmEmailRegistration(token);
        verifiedUserOpt.orElseThrow(() -> new InvalidTokenRequestException("Email Verification Token" + token +
                "Failed to confirm. Please generate a new email verification request"));
    }

    @GetMapping("/resendRegistrationToken")
    @ResponseStatus(value = HttpStatus.OK)
    public void resendVerification(@RequestParam("email") String email, WebRequest request) {
        EmailVerificationToken existingToken = emailVerificationTokenService.findByUserEmail(email);
        EmailVerificationToken newToken = authService.recreateRegistrationToken(existingToken.getToken())
                .orElseThrow(() -> new InvalidTokenRequestException("User is already registered"));
        User registeredUser = newToken.getUser();
        String appUrl = request.getContextPath();
        OnRegenerateEmailVerificationEvent onRegenerateEmailVerificationEvent =
                new OnRegenerateEmailVerificationEvent(registeredUser, appUrl, request.getLocale(), newToken.getToken());
        applicationEventPublisher.publishEvent(onRegenerateEmailVerificationEvent);
    }
}