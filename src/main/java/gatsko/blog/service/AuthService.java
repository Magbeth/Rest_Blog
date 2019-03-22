package gatsko.blog.service;

import gatsko.blog.exception.ResourceAlreadyInUseException;
import gatsko.blog.model.CustomUserDetails;
import gatsko.blog.model.DTO.LoginRequest;
import gatsko.blog.model.DTO.RegistrationRequest;
import gatsko.blog.model.User;
import gatsko.blog.security.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserService userService;
    private final JwtProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserService userService, JwtProvider tokenProvider,
                       PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.tokenProvider = tokenProvider;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    /**
     * Registers a new user in the database by performing a series of quick checks.
     *
     * @return A user object if successfully created
     */
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

    /**
     * Checks if the given email already exists in the database repository or not
     *
     * @return true if the email exists else false
     */
    private Boolean emailAlreadyExists(String email) {
        return userService.emailExists(email);
    }

    /**
     * Checks if the given email already exists in the database repository or not
     *
     * @return true if the email exists else false
     */
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



}
