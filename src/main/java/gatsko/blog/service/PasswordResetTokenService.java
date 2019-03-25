package gatsko.blog.service;

import gatsko.blog.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class PasswordResetTokenService {
    @Value("${app.token.password.reset.duration}")
    private Long keyExpirationInMinutes;

    @Autowired
    private RedisTemplate<String, User> redisTemplate;

    public void saveTokenToRedis(String token, User user) {
        redisTemplate.opsForValue().set(token, user, keyExpirationInMinutes, TimeUnit.MINUTES);
    }

    public Optional<User> findByTokenFromRedis(String token) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(token));
    }
}
