package gatsko.blog.model.DTO;

import gatsko.blog.model.ArticleStatus;
import gatsko.blog.model.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleDTO {
    @NotBlank
    private String title;
    @NotBlank
    private String fullPostText;
    @NotBlank
    private ArticleStatus status;
    private Collection<Tag> tags;
}
