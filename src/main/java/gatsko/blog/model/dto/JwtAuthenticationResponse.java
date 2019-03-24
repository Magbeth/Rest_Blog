package gatsko.blog.model.dto;

import lombok.Data;

@Data
public class JwtAuthenticationResponse {

    private String accessToken;

    private String tokenType;

    private Long expiryDuration;

    public JwtAuthenticationResponse(String accessToken, Long expiryDuration) {
        this.accessToken = accessToken;
        this.expiryDuration = expiryDuration;
        this.tokenType = "Bearer ";
    }
}
