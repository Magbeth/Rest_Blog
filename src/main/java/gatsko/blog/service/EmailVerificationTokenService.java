package gatsko.blog.service;

import gatsko.blog.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class EmailVerificationTokenService {
    private final RedisTemplate<String, User> redisTemplate;
    @Value("${app.token.email.verification.duration}")
    private Long keyExpirationInMinutes;

    public EmailVerificationTokenService(RedisTemplate<String, User> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void createVerificationToken(User user, String token) {
        redisTemplate.opsForValue().set(token, user, keyExpirationInMinutes, TimeUnit.MINUTES);
        System.out.println("USER FROM REDIS: " + redisTemplate.opsForValue().get(token));

    }

    public String generateNewToken() {
        return UUID.randomUUID().toString();
    }

    public Optional<User> findUserByToken(String token){
        return Optional.ofNullable(redisTemplate.opsForValue().get(token));
    }


}
