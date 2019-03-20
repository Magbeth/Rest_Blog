package gatsko.blog.security;

import gatsko.blog.model.CustomUserDetails;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import java.time.Instant;
import java.util.Date;

@Component
public class JwtProvider {


    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpiration}")
    private Long jwtExpiration;

    public String generateJwtToken(CustomUserDetails customUserDetails) {

        Instant expiryDate = Instant.now().plusMillis(jwtExpiration);

        return Jwts.builder()
                .setSubject(Long.toString(customUserDetails.getId()))
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expiryDate))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    public String generateTokenFromUserId(Long userId) {
        Instant expiryDate = Instant.now().plusMillis(jwtExpiration);
        return Jwts.builder()
                .setSubject(Long.toString(userId))
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(expiryDate))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }
    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    /**
     * Validates if a token has the correct unmalformed signature and is not expired or unsupported.
     */
    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
//            logger.error("Invalid JWT signature");
//            throw new InvalidTokenRequestException("JWT", authToken, "Incorrect signature");
        } catch (MalformedJwtException ex) {
//            logger.error("Invalid JWT token");
//            throw new InvalidTokenRequestException("JWT", authToken, "Malformed jwt token");
        } catch (ExpiredJwtException ex) {
//            logger.error("Expired JWT token");
//            throw new InvalidTokenRequestException("JWT", authToken, "Token expired. Refresh required.");
        } catch (UnsupportedJwtException ex) {
//            logger.error("Unsupported JWT token");
//            throw new InvalidTokenRequestException("JWT", authToken, "Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
//            logger.error("JWT claims string is empty.");
//            throw new InvalidTokenRequestException("JWT", authToken, "Illegal argument token");
        }
        return false;
    }

    /**
     * Return the jwt expiration for the client so that they can execute
     * the refresh token logic appropriately
     */
    public Long getExpiryDuration() {
        return jwtExpiration;
    }
}