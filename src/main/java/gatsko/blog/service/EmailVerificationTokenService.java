package gatsko.blog.service;

import gatsko.blog.exception.ResourceNotFoundException;
import gatsko.blog.model.Token.EmailVerificationToken;
import gatsko.blog.model.Token.TokenStatus;
import gatsko.blog.model.User;
import gatsko.blog.repository.EmailVerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class EmailVerificationTokenService {
    private final EmailVerificationTokenRepository emailVerificationTokenRepository;

    public EmailVerificationTokenService(EmailVerificationTokenRepository emailVerificationTokenRepository) {
        this.emailVerificationTokenRepository = emailVerificationTokenRepository;
    }

    @Autowired
    private RedisTemplate<String, User> redisTemplate;
    @Value("${app.token.email.verification.duration}")
    private Long emailVerificationTokenExpiryDuration;

    public void createVerificationToken(User user, String token) {
//        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
//        emailVerificationToken.setToken(token);
//        emailVerificationToken.setTokenStatus(TokenStatus.STATUS_PENDING);
//        emailVerificationToken.setUser(user);
//        emailVerificationToken.setExpiryDate(Instant.now().plusMillis(emailVerificationTokenExpiryDuration));
//        emailVerificationTokenRepository.save(emailVerificationToken);
        redisTemplate.opsForValue().set(token, user, 2l, TimeUnit.MINUTES);
        System.out.println("USER FROM REDIS: " + redisTemplate.opsForValue().get(token));

    }

    public String generateNewToken() {
        return UUID.randomUUID().toString();
    }

//    public boolean isTokenExpiry(EmailVerificationToken token) {
//        return token.getExpiryDate().compareTo(Instant.now()) < 0;
//    }

//    public EmailVerificationToken save(EmailVerificationToken emailVerificationToken) {
//        return emailVerificationTokenRepository.save(emailVerificationToken);
//    }

//    public EmailVerificationToken findByUserEmail(String email) {
//        return emailVerificationTokenRepository.findByUserEmail(email)
//                .orElseThrow(() -> new ResourceNotFoundException("User with email " + email + " not found"));
//    }

//    public Optional<EmailVerificationToken> findByToken(String token) {
//        return emailVerificationTokenRepository.findByToken(token);
//    }
//
//    public EmailVerificationToken updateExistingTokenWithNameAndExpiry(EmailVerificationToken existingToken) {
//        existingToken.setTokenStatus(TokenStatus.STATUS_PENDING);
//        existingToken.setExpiryDate(Instant.now().plusMillis(emailVerificationTokenExpiryDuration));
//        return save(existingToken);
//    }

    public Optional<User> findUserByToken(String token){
        return Optional.ofNullable(redisTemplate.opsForValue().get(token));
    }


}
