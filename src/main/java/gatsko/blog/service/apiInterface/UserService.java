package gatsko.blog.service.apiInterface;


import gatsko.blog.model.dto.PasswordResetRequest;
import gatsko.blog.model.dto.RegistrationRequest;
import gatsko.blog.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    User findByUsername(String username);

    boolean emailExists(String email);

    boolean usernameExists(String username);

    User currentUser();

    User save(User user);

    User createUser(RegistrationRequest registrationRequest);

    String generatePasswordResetToken(String email);

    UserDetails loadUserById(Long id);

    void resetPassword(PasswordResetRequest passwordResetRequest);

}
