package gatsko.blog.service;

import gatsko.blog.exception.InvalidTokenRequestException;
import gatsko.blog.exception.ResourceAlreadyInUseException;
import gatsko.blog.exception.ResourceNotFoundException;
import gatsko.blog.model.CustomUserDetails;
import gatsko.blog.model.dto.LoginRequest;
import gatsko.blog.model.dto.RegistrationRequest;
import gatsko.blog.model.Token.EmailVerificationToken;
import gatsko.blog.model.User;
import gatsko.blog.security.JwtProvider;
import gatsko.blog.service.ApiInterface.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserService userService;
    private final JwtProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final EmailVerificationTokenService emailVerificationTokenService;

    public AuthService(UserService userService, JwtProvider tokenProvider,
                       AuthenticationManager authenticationManager,
                       EmailVerificationTokenService emailVerificationTokenService) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.emailVerificationTokenService = emailVerificationTokenService;
    }

    public Optional<User> registerUser(RegistrationRequest newRegistrationRequest) {
        String newRegistrationRequestEmail = newRegistrationRequest.getEmail();
        if (emailAlreadyExists(newRegistrationRequestEmail)) {
            throw new ResourceAlreadyInUseException("Registration failed. Email already used");
        }
        String newRegistrationUsername = newRegistrationRequest.getUsername();
        if (usernameAlreadyExists(newRegistrationUsername)) {
            throw new ResourceAlreadyInUseException("Registration failed. Username already used");
        }

        User newUser = userService.createUser(newRegistrationRequest);
        return Optional.ofNullable(userService.save(newUser));
    }

    private Boolean emailAlreadyExists(String email) {
        return userService.emailExists(email);
    }

    private Boolean usernameAlreadyExists(String username) {
        return userService.usernameExists(username);
    }

    public Optional<Authentication> authenticateUser(LoginRequest loginRequest) {
        return Optional.ofNullable(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword())));
    }

    public String generateToken(CustomUserDetails customUserDetails) {
        return tokenProvider.generateJwtToken(customUserDetails);
    }

    public Optional<User> confirmEmailRegistration(String emailToken) {
        Optional<EmailVerificationToken> emailVerificationTokenOpt =
                emailVerificationTokenService.findByToken(emailToken);
        emailVerificationTokenOpt.orElseThrow(() ->
                new InvalidTokenRequestException("Invalid Token " + emailToken));
        Optional<User> registeredUserOpt = emailVerificationTokenOpt.map(EmailVerificationToken::getUser);
        if (emailVerificationTokenService.isTokenExpiry(emailVerificationTokenOpt.get())) {
            throw new InvalidTokenRequestException("Token expired");
        }
        emailVerificationTokenOpt.ifPresent(EmailVerificationToken::confirmStatus);
        emailVerificationTokenOpt.ifPresent(emailVerificationTokenService::save);
        registeredUserOpt.ifPresent(User::confirmVerification);
        registeredUserOpt.ifPresent(userService::save);
        return registeredUserOpt;
    }

    public Optional<EmailVerificationToken> recreateRegistrationToken(String existingToken) {
        EmailVerificationToken emailVerificationToken = emailVerificationTokenService.findByToken(existingToken)
                .orElseThrow(() -> new ResourceNotFoundException("Token " + existingToken + " not found"));

        if (emailVerificationToken.getUser().isEnabled()) {
            return Optional.empty();
        }
        return Optional.ofNullable(emailVerificationTokenService.updateExistingTokenWithNameAndExpiry(emailVerificationToken));
    }


}
