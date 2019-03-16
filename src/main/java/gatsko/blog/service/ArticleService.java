package gatsko.blog.service;

import gatsko.blog.model.Article;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ArticleService {
    Page<Article> getArticlesPage(int pageNumber, int pageSize);


    Article getArticle(Long id);

    Page<Article> findArticleByTag(List<String> tags, int pageNumber, int pageSize);

    Article saveNewArticle(Article article);

    //
//    void setArticleVisibility(Long articleId, boolean hide);
//
    void deleteArticle(Long articleId);

    Article updateArticle(Article article);

}
