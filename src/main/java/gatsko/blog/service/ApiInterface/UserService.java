package gatsko.blog.service.ApiInterface;


import gatsko.blog.model.dto.PasswordResetRequest;
import gatsko.blog.model.dto.RegistrationRequest;
import gatsko.blog.model.Token.PasswordResetToken;
import gatsko.blog.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    User findByUsername(String username);

    boolean emailExists(String email);

    boolean usernameExists(String username);

    User currentUser();

    User save(User user);

    User createUser(RegistrationRequest registrationRequest);

    PasswordResetToken generatePasswordResetToken(String email);

    UserDetails loadUserById(Long id);

    Optional<User> resetPassword(PasswordResetRequest passwordResetRequest);

}
