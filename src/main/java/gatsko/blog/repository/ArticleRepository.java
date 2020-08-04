package gatsko.blog.repository;

import gatsko.blog.model.Article;
import gatsko.blog.model.enums.ArticleStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findAllByUser_Username(String usernameUser, Pageable pageable);

    @Query("SELECT a FROM Article a JOIN a.tags t WHERE LOWER(t.name) in (:tags)")
    Page<Article> findByTags(@Param("tags") Collection<String> tags, Pageable pageable);

    @Query("SELECT a FROM Article a JOIN a.tags t WHERE LOWER(t.name) in (:tags) and a.status = 'PUBLIC'")
    Page<Article> findPublicArticlesByTags(@Param("tags") Collection<String> tags, Pageable pageable);

    Page<Article> findAllByStatus(ArticleStatus status, Pageable pageable);

    @Query("SELECT count(a) FROM Article a WHERE :tagCount = (SELECT COUNT(DISTINCT t.id) "
            + "FROM Article a2 JOIN a2.tags t WHERE LOWER(t.name) in (:tagNames) and a = a2)")
    Long findArticleCountByTag(@Param("tagNames") Collection<String> tagNames, @Param("tagCount") Long tagCount);

    Boolean existsById(Long id);

}
