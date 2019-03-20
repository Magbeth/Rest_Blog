package gatsko.blog.service;

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

    @Autowired
    private UserService userService;

    @Autowired
    private JwtProvider tokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    /**
     * Registers a new user in the database by performing a series of quick checks.
     *
     * @return A user object if successfully created
     */
    public Optional<User> registerUser(RegistrationRequest newRegistrationRequest) {
        String newRegistrationRequestEmail = newRegistrationRequest.getEmail();
//        if (emailAlreadyExists(newRegistrationRequestEmail)) {
//            throw new ResourceAlreadyInUseException("Email", "Address", newRegistrationRequestEmail);
//        }

        User newUser = userService.createUser(newRegistrationRequest);
        User registeredNewUser = userService.save(newUser);
        return Optional.ofNullable(registeredNewUser);
    }

    /**
     * Checks if the given email already exists in the database repository or not
     *
     * @return true if the email exists else false
     */
//    public Boolean emailAlreadyExists(String email) {
//        return userService.existsByEmail(email);
//    }

    /**
     * Checks if the given email already exists in the database repository or not
     *
     * @return true if the email exists else false
     */
//    public Boolean usernameAlreadyExists(String username) {
//        return userService.existsByUsername(username);
//    }

    public Optional<Authentication> authenticateUser(LoginRequest loginRequest) {
        return Optional.ofNullable(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword())));
    }

    public String generateToken(CustomUserDetails customUserDetails) {
        return tokenProvider.generateJwtToken(customUserDetails);
    }



}
