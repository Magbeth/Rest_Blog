package gatsko.blog.service;

import gatsko.blog.model.User;
import gatsko.blog.model.dto.PasswordResetRequest;
import gatsko.blog.repository.UsersRepository;
import gatsko.blog.service.apiInterface.PasswordResetTokenService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
    @Mock
    private UsersRepository usersRepository;
    @Mock
    private PasswordResetTokenService passwordResetTokenService;
    @Mock
    private PasswordEncoder passwordEncoder;


    @InjectMocks
    private UserServiceImpl sut;

    @Test
    public void findByEmail() {
        String email = "email@email.com";
        sut.findByEmail(email);
        verify(usersRepository).findByEmailIgnoreCase(eq(email));
    }

    @Test
    public void findByUsername() {
        String username = "username";
        sut.findByUsername(username);
        verify(usersRepository).findByUsernameIgnoreCase(eq(username));
    }


    @Test
    public void loadUserById() {
        Optional<User> user = Optional.of(new User());
        when(usersRepository.findById(anyLong())).thenReturn(user);
        sut.loadUserById(anyLong());
        verify(usersRepository).findById(anyLong());
    }

    @Test
    public void loadUserByUsername() {
        Optional<User> user = Optional.of(new User());
        when(usersRepository.findByUsername(anyString())).thenReturn(user);
        sut.loadUserByUsername(anyString());
        verify(usersRepository).findByUsername(anyString());
    }

    @Test
    public void save() {
        User user = new User();
        sut.save(user);
        verify(usersRepository).saveAndFlush(eq(user));
    }

    @Test
    public void emailExists() {
        String email = "email";
        sut.emailExists(email);
        verify(usersRepository).existsByEmail(eq(email));
    }

    @Test
    public void usernameExists() {
        String username = "username";
        sut.usernameExists(username);
        verify(usersRepository).existsByUsername(eq(username));
    }

    @Test
    public void generatePasswordResetToken() {
        User user = new User();
        String email = "email";
        when(sut.findByEmail(email)).thenReturn(user);
        sut.generatePasswordResetToken(email);
        verify(passwordResetTokenService).saveTokenToRedis(anyString(), eq(user));
    }

    @Test
    public void resetPassword() {
        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setPassword("password");
        passwordResetRequest.setToken("token");
        Optional<User> user = Optional.of(new User());
        when(passwordResetTokenService.findByTokenFromRedis(anyString())).thenReturn(user);
        sut.resetPassword(passwordResetRequest);
        verify(usersRepository).save(any(User.class));

    }
}