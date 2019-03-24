package gatsko.blog.service;

import gatsko.blog.exception.ResourceNotFoundException;
import gatsko.blog.model.Token.PasswordResetToken;
import gatsko.blog.repository.PasswordTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetTokenService {
    @Value("${app.token.password.reset.duration}")
    private Long expiration;
    @Autowired
    private PasswordTokenRepository passwordTokenRepository;

    public PasswordResetToken save(PasswordResetToken passwordResetToken) {
        return passwordTokenRepository.save(passwordResetToken);
    }

    public PasswordResetToken createToken() {
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        String token = UUID.randomUUID().toString();
        passwordResetToken.setToken(token);
        passwordResetToken.setExpiryDate(Instant.now().plusMillis(expiration));
        return passwordResetToken;
    }

    public Optional<PasswordResetToken> findByToken(String token) {
        return passwordTokenRepository.findByToken(token);
    }

    public void verifyExpiration(PasswordResetToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            throw new ResourceNotFoundException("Password Reset Token " + token.getToken() +
                    " Expired token. Please issue a new request");
        }
    }
}
