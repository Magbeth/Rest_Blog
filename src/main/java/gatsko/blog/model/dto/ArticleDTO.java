package gatsko.blog.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import gatsko.blog.model.Article;
import gatsko.blog.model.enums.ArticleStatus;
import gatsko.blog.model.Tag;
import gatsko.blog.utils.JsonDateDeserializer;
import gatsko.blog.utils.JsonDateSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDTO {
    private Long id;
    @NotBlank
    private String title;
    @NotBlank
    private String fullPostText;
    @NotNull
    private ArticleStatus status;
    private Collection<Tag> tags;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private LocalDateTime createdAt;
    @JsonSerialize(using = JsonDateSerializer.class)
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private LocalDateTime updatedAt;

    public ArticleDTO(Article article) {
        this.id = article.getId();
        this.title = article.getFullPostText();
        this.status = article.getStatus();
        this.tags = article.getTags();
        this.createdAt = article.getCreatedAt();
        this.updatedAt = article.getUpdatedAt();
    }


}
