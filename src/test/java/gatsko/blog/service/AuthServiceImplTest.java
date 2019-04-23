package gatsko.blog.service;

import gatsko.blog.model.CustomUserDetails;
import gatsko.blog.model.User;
import gatsko.blog.model.dto.LoginRequest;
import gatsko.blog.model.dto.RegistrationRequest;
import gatsko.blog.security.JwtProvider;
import gatsko.blog.service.apiInterface.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AuthServiceImplTest {
    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private RegistrationRequest registrationRequest;
    @Mock
    private JwtProvider tokenProvider;
    @Mock
    private LoginRequest loginRequest;
    @Mock
    private CustomUserDetails customUserDetails;
    @Mock
    private EmailVerificationTokenServiceImpl emailVerificationTokenService;

    @InjectMocks
    private AuthServiceImpl sut;


    @Test
    public void registerUser() {
        User user = new User();
        when(userService.createUser(registrationRequest)).thenReturn(user);
        sut.registerUser(registrationRequest);
        verify(userService).createUser(registrationRequest);
        verify(userService).save(any(User.class));
    }

    @Test
    public void authenticateUser() {
        sut.authenticateUser(loginRequest);
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void generateToken() {
        sut.generateToken(customUserDetails);
        verify(tokenProvider).generateJwtToken(any(CustomUserDetails.class));
    }

    @Test
    public void confirmEmailRegistrationWithRedis() {
        String token = UUID.randomUUID().toString();
        Optional<User> user = Optional.of(new User());
        when(emailVerificationTokenService.findUserByToken(token)).thenReturn(user);
        sut.confirmEmailRegistrationWithRedis(token);
        verify(emailVerificationTokenService).findUserByToken(token);
        verify(userService).save(any(User.class));
    }

    @Test
    public void generateNewTokenForUserEmail() {
        User user = new User();
        user.setEnabled(false);
        String email = "email@email.com";
        String token = UUID.randomUUID().toString();
        when(userService.findByEmail(email)).thenReturn(user);
        when(emailVerificationTokenService.generateNewToken()).thenReturn(token);
        sut.generateNewTokenForUserEmail(email);
        verify(emailVerificationTokenService).generateNewToken();
        verify(emailVerificationTokenService).createVerificationToken(eq(user), eq(token));
    }
}