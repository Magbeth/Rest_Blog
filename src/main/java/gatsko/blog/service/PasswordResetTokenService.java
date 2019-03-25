package gatsko.blog.service;

import gatsko.blog.exception.ResourceNotFoundException;
import gatsko.blog.model.Token.PasswordResetToken;
import gatsko.blog.model.User;
import gatsko.blog.repository.PasswordTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class PasswordResetTokenService {
    @Value("${app.token.password.reset.duration}")
    private Long expiration;
//    @Autowired
//    private PasswordTokenRepository passwordTokenRepository;
    @Autowired
    private RedisTemplate<String, User> redisTemplate;

//    public PasswordResetToken save(PasswordResetToken passwordResetToken) {
//        return passwordTokenRepository.save(passwordResetToken);
//    }

//    public PasswordResetToken createToken() {
//        PasswordResetToken passwordResetToken = new PasswordResetToken();
//        String token = UUID.randomUUID().toString();
//        passwordResetToken.setToken(token);
//        passwordResetToken.setExpiryDate(Instant.now().plusMillis(expiration));
//        return passwordResetToken;
//    }

//    public Optional<PasswordResetToken> findByToken(String token) {
//        return passwordTokenRepository.findByToken(token);
//    }

//    public void verifyExpiration(PasswordResetToken token) {
//        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
//            throw new ResourceNotFoundException("Password Reset Token " + token.getToken() +
//                    " Expired token. Please issue a new request");
//        }
//    }

    public void saveTokenToRedis(String token, User user) {
        redisTemplate.opsForValue().set(token, user, 2l, TimeUnit.MINUTES);
    }

    public Optional<User> findByTokenFromRedis(String token) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(token));
    }
}
