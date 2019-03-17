package gatsko.blog.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import gatsko.blog.utils.JsonDateDeserializer;
import gatsko.blog.utils.JsonDateSerializer;
import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "articles")
@Builder
@Entity
@Table(name = "blog_user")
public class User {
    public interface CreateValidationGroup {
    }

    public interface ChangeEmailValidationGroup {
    }

    public interface ChangePasswordValidationGroup {
    }

    public interface ProfileInfoValidationGroup {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    @Size.List({
            @Size(min = 3, message = "Username too short", groups = {CreateValidationGroup.class}),
            @Size(max = 25, message = "Username too long", groups = {CreateValidationGroup.class})
    })
    @NotBlank(groups = {CreateValidationGroup.class})
    private String username;

    @Column(name = "first_name", nullable = false, length = 80)
    @NotBlank(groups = {CreateValidationGroup.class})
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 80)
    @NotBlank(groups = {CreateValidationGroup.class})
    private String lastName;

    @Column(nullable = false, length = 80)
    @Size.List({
            @Size(min = 6, message = "Password too short", groups = {CreateValidationGroup.class, ChangePasswordValidationGroup.class}),
            @Size(max = 80, message = "Password too long", groups = {CreateValidationGroup.class, ChangePasswordValidationGroup.class})
    })
    @NotBlank(groups = {CreateValidationGroup.class, ChangePasswordValidationGroup.class})
    private String password;

    @Column(unique = true, nullable = false, length = 50)
    @Email(groups = {CreateValidationGroup.class, ChangeEmailValidationGroup.class})
    @NotBlank(groups = {CreateValidationGroup.class, ChangeEmailValidationGroup.class})
    private String email;

    @Column(nullable = false)
    private boolean enabled;

    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<Article> articles = new ArrayList<>();
}