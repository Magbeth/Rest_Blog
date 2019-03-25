package gatsko.blog.service;

import gatsko.blog.model.User;
import gatsko.blog.service.apiInterface.PasswordResetTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {
    @Value("${app.token.password.reset.duration}")
    private Long keyExpirationInMinutes;
    private final RedisTemplate<String, User> redisTemplate;

    public PasswordResetTokenServiceImpl(RedisTemplate<String, User> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void saveTokenToRedis(String token, User user) {
        redisTemplate.opsForValue().set(token, user, keyExpirationInMinutes, TimeUnit.MINUTES);
    }

    @Override
    public Optional<User> findByTokenFromRedis(String token) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(token));
    }
}
