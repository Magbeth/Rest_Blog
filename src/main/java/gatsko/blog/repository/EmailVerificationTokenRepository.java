package gatsko.blog.repository;

import gatsko.blog.model.Token.EmailVerificationToken;
import gatsko.blog.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationTokenRepository extends JpaRepository<EmailVerificationToken, Long> {
    Optional<EmailVerificationToken> findByToken(String token);

    Optional<EmailVerificationToken> findByUserEmail(String email);

}
