package gatsko.blog.service;


import gatsko.blog.model.DTO.RegistrationRequest;
import gatsko.blog.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    User findByUsername(String username);

    boolean emailExists(String email);

    boolean usernameExists(String username);

    User currentUser();
    User save(User user);
    User createUser(RegistrationRequest registrationRequest);

}
