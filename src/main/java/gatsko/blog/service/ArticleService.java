package gatsko.blog.service;

import gatsko.blog.model.Article;
import gatsko.blog.model.Tag;
import org.springframework.data.domain.Page;

import java.util.Collection;
import java.util.List;

public interface ArticleService {
    Page<Article> getArticlesPage(int pageNumber, int pageSize);


    Article getArticle(Long id);

    Page<Article> findArticleByTag(List<String> tags, int pageNumber, int pageSize);

    Article saveNewArticle(Article article, Collection<Tag> tags);

    //
//    void setArticleVisibility(Long articleId, boolean hide);
//
    void deleteArticle(Long articleId);

    Article updateArticle(Article article);

}
