package gatsko.blog.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class TagCloudResponse {
    private List<String> tagName;
    private Long articleCount;

    public TagCloudResponse(List<String> tagName, Long articleCount) {
        this.tagName = tagName;
        this.articleCount = articleCount;
    }
}
