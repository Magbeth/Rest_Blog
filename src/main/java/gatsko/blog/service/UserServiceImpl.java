package gatsko.blog.service;

import gatsko.blog.exception.PasswordResetException;
import gatsko.blog.exception.ResourceNotFoundException;
import gatsko.blog.model.*;
import gatsko.blog.model.dto.PasswordResetRequest;
import gatsko.blog.model.dto.RegistrationRequest;
import gatsko.blog.repository.UsersRepository;
import gatsko.blog.service.apiInterface.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Service("userService")
public class UserServiceImpl implements UserService {
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final UsersRepository usersRepository;
    private final PasswordResetTokenService passwordResetTokenService;

    public UserServiceImpl(PasswordEncoder passwordEncoder, RoleService roleService,
                           UsersRepository usersRepository, PasswordResetTokenService passwordResetTokenService) {
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
        this.passwordResetTokenService = passwordResetTokenService;
    }

    @Override
    public User findByEmail(String email) {
        return usersRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public User findByUsername(String username) {
        return usersRepository.findByUsernameIgnoreCase(username);
    }

    @Override
    public User currentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public UserDetails loadUserById(Long id) {
        Optional<User> dbUser = usersRepository.findById(id);
        return dbUser.map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find a matching id in the " +
                        "database for " + id));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> dbUser = usersRepository.findByUsername(username);
        return dbUser.map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find a matching username in the " +
                        "database for " + username));
    }

    @Override
    public User save(User user) {
        return usersRepository.saveAndFlush(user);
    }

    @Override
    public User createUser(RegistrationRequest registrationRequest) {
        User newUser = new User();
        newUser.setEmail(registrationRequest.getEmail());
        newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        newUser.setUsername(registrationRequest.getUsername());
        newUser.setLastName(registrationRequest.getLastName());
        newUser.setFirstName(registrationRequest.getFirstName());
        newUser.setEnabled(false);
        newUser.setRoles(getRolesForNewUser());
        newUser.setCreatedAt(LocalDateTime.now());
        return newUser;
    }

    private Set<Role> getRolesForNewUser() {
        return new HashSet<>(roleService.findAll());
    }

    @Override
    public boolean emailExists(String email) {
        return usersRepository.existsByEmail(email);
    }

    @Override
    public boolean usernameExists(String username) {
        return usersRepository.existsByUsername(username);
    }

    @Override
    public String generatePasswordResetToken(String email) {
        User user = findByEmail(email);
        if (user == null) {
            throw new ResourceNotFoundException("User with email " + email + " not found");
        }
        String token = UUID.randomUUID().toString();
        passwordResetTokenService.saveTokenToRedis(token, user);
        return token;
    }

    @Override
    public void resetPassword(PasswordResetRequest passwordResetRequest) {
        String token = passwordResetRequest.getToken();
        String encodedPassword = passwordEncoder.encode(passwordResetRequest.getPassword());
        User user = passwordResetTokenService.findByTokenFromRedis(token)
                .orElseThrow(() -> new PasswordResetException("Password reset link expired"));
        user.setPassword(encodedPassword);
        usersRepository.save(user);
    }
}
