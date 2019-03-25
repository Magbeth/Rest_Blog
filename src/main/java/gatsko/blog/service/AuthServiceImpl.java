package gatsko.blog.service;

import gatsko.blog.exception.InvalidTokenRequestException;
import gatsko.blog.exception.ResourceAlreadyInUseException;
import gatsko.blog.exception.ResourceNotFoundException;
import gatsko.blog.exception.UserAlreadyActivatedException;
import gatsko.blog.model.CustomUserDetails;
import gatsko.blog.model.dto.LoginRequest;
import gatsko.blog.model.dto.RegistrationRequest;
import gatsko.blog.model.User;
import gatsko.blog.security.JwtProvider;
import gatsko.blog.service.apiInterface.AuthService;
import gatsko.blog.service.apiInterface.EmailVerificationTokenService;
import gatsko.blog.service.apiInterface.UserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserService userService;
    private final JwtProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final EmailVerificationTokenService emailVerificationTokenService;

    public AuthServiceImpl(UserService userService, JwtProvider tokenProvider,
                           AuthenticationManager authenticationManager,
                           EmailVerificationTokenService emailVerificationTokenService) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.emailVerificationTokenService = emailVerificationTokenService;
    }

    @Override
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

    @Override
    public Optional<Authentication> authenticateUser(LoginRequest loginRequest) {
        return Optional.ofNullable(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword())));
    }

    @Override
    public String generateToken(CustomUserDetails customUserDetails) {
        return tokenProvider.generateJwtToken(customUserDetails);
    }

    @Override
    public Optional<User> confirmEmailRegistrationWithRedis(String emailToken) {
        Optional<User> user =
                emailVerificationTokenService.findUserByToken(emailToken);
        user.orElseThrow(() ->
                new InvalidTokenRequestException("Invalid Token " + emailToken));
        user.ifPresent(User::confirmVerification);
        user.ifPresent(userService::save);
        return user;
    }

    @Override
    public String generateNewTokenForUserEmail(String email) {
        String token = emailVerificationTokenService.generateNewToken();
        User registeredUser = Optional.ofNullable(userService.findByEmail(email))
                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not exists"));
        if (registeredUser.isEnabled()) {
            throw new UserAlreadyActivatedException("User with email " + email + " already activated");
        }
        emailVerificationTokenService.createVerificationToken(registeredUser, token);
        return token;
    }


}
