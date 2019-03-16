package gatsko.blog.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "blog_user")
public class User {
    public interface CreateValidationGroup {}
    public interface ChangeEmailValidationGroup {}
    public interface ChangePasswordValidationGroup {}
    public interface ProfileInfoValidationGroup {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column (unique = true, nullable = false, length = 50)
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

    @Column (unique = true, nullable = false, length = 50)
    @Email(groups = {CreateValidationGroup.class, ChangeEmailValidationGroup.class})
    @NotBlank(groups = {CreateValidationGroup.class, ChangeEmailValidationGroup.class})
    private String email;

    @Column(nullable = false)
    private boolean enabled;

    @Column(nullable = false)
    private String created_at;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private List<Article> articles = new ArrayList<>();

//    public static User from(UserForm form) {
//        return User.builder()
//                .firstName(form.getFirstName())
//                .lastName(form.getLastName())
//                .build();
//    }
}