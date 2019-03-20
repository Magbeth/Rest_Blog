package gatsko.blog.service;

import gatsko.blog.model.*;
import gatsko.blog.model.DTO.RegistrationRequest;
import gatsko.blog.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


@Service("userService")
public class UserServiceImpl implements UserService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public User findByEmail(String email) {
        return usersRepository.findByEmailIgnoreCase(email);
    }

    @Override
    public User findByUsername(String username) {
        return usersRepository.findByUsernameIgnoreCase(username);
    }

    @Override
    public boolean usernameExists(String username) {
        return findByUsername(username) != null;
    }

    @Override
    public boolean emailExists(String email) {
        return findByEmail(email) != null;
    }

    @Override
    public User currentUser() {
        return findByUsername("1111");
    }

    public UserDetails loadUserById(Long id) {
        Optional<User> dbUser = usersRepository.findById(id);
//        logger.info("Got user: " + dbUser + " for " + id);
        return dbUser.map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find a matching id in the " +
                        "database for " + id));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> dbUser = usersRepository.findByUsername(username);
//        logger.info("Got user: " + dbUser + " for " + email);
        return dbUser.map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("Couldn't find a matching email in the " +
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
        newUser.setEnabled(true);
        newUser.setRoles(getRolesForNewUser());
        newUser.setCreatedAt(LocalDateTime.now());
        return newUser;
    }

    private Set<Role> getRolesForNewUser() {
        Set<Role> newUserRoles = new HashSet<>(roleService.findAll());
        return newUserRoles;
    }

}
