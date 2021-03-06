package gatsko.blog.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import gatsko.blog.model.dto.ArticleDto;
import gatsko.blog.model.enums.ArticleStatus;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "user")
@Entity
@Table(name = "articles")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(length = 250, nullable = false)
    private String title;

    @Column(name = "full_text", columnDefinition = "text", nullable = false)
    private String fullPostText;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ArticleStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToMany(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinTable(name = "articles_tags",
            joinColumns = @JoinColumn(name = "article_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    @OrderBy("name ASC")
    private Collection<Tag> tags;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "article")
    @OrderBy("createdAt ASC")
    private List<Comment> comments;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id", nullable = false)
    private User user;

    public Article(ArticleDto articleDto) {
        this.title = articleDto.getTitle();
        this.fullPostText = articleDto.getFullPostText();
        this.tags = articleDto.getTags();
        this.status = articleDto.getStatus();
    }

    public boolean isPublic() {
        return this.status == ArticleStatus.PUBLIC;
    }
}
