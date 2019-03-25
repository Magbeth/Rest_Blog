package gatsko.blog.service.apiInterface;

import gatsko.blog.model.CustomUserDetails;
import gatsko.blog.model.User;
import gatsko.blog.model.dto.LoginRequest;
import gatsko.blog.model.dto.RegistrationRequest;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public interface AuthService {
    Optional<User> registerUser(RegistrationRequest newRegistrationRequest);

    Optional<Authentication> authenticateUser(LoginRequest loginRequest);

    String generateToken(CustomUserDetails customUserDetails);

    Optional<User> confirmEmailRegistrationWithRedis(String emailToken);

    String generateNewTokenForUserEmail(String email);

}
