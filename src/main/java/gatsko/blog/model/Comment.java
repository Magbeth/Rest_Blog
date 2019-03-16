package gatsko.blog.model;


import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"article", "user"})
@Builder
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(columnDefinition = "text", nullable = false)
    @NotBlank
    private String commentText;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;
}
