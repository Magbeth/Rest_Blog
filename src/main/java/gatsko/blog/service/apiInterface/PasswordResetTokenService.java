package gatsko.blog.service.apiInterface;

import gatsko.blog.model.User;

import java.util.Optional;

public interface PasswordResetTokenService {
    void saveTokenToRedis(String token, User user);

    Optional<User> findByTokenFromRedis(String token);
}
