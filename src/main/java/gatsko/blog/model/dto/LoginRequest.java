package gatsko.blog.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @Size.List({
            @Size(min = 3, message = "Username too short"),
            @Size(max = 25, message = "Username too long")
    })
    @NotBlank
    private String username;

    @Size.List({
            @Size(min = 6, message = "Password too short"),
            @Size(max = 80, message = "Password too long")
    })
    @NotBlank
    private String password;
}