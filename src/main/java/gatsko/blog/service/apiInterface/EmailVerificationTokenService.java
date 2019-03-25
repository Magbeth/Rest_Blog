package gatsko.blog.service.apiInterface;

import gatsko.blog.model.User;

import java.util.Optional;

public interface EmailVerificationTokenService {
    void createVerificationToken(User user, String token);

    String generateNewToken();

    Optional<User> findUserByToken(String token);
}
