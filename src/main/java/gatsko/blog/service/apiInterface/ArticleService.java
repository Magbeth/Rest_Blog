package gatsko.blog.service.apiInterface;

import gatsko.blog.model.Article;
import gatsko.blog.model.dto.ArticleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Collection;
import java.util.List;

public interface ArticleService {
    Page<Article> getArticlesPage(int pageNumber, int pageSize, Sort sort);

    Page<Article> getUserArticlesPage(String username, int pageNumber, int pageSize, Sort sort);

    Article getArticle(Long id);

    Article getArticleForReading(Long id);

    Page<Article> findArticleByTag(List<String> tags, int pageNumber, int pageSize, Sort sort);

    Article saveNewArticle(Article article);

    Long countArticlesWithTag(Collection<String> tags);

    @PreAuthorize("#article.user.username == authentication.name")
    void deleteArticle(Article article);

    @PreAuthorize("#article.user.username == authentication.name")
    Article updateArticle(Article article, ArticleDto editedArticle);

}
