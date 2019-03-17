package gatsko.blog.repository;

import gatsko.blog.model.Article;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findAllByUser_Username(String usernameUser);
//    @Query("SELECT a FROM Article a WHERE :tagCount = (SELECT COUNT(DISTINCT t.id) FROM Article a2 JOIN a2.tags t WHERE LOWER(t.name) in (:tags) and a = a2)")
//    Page<Article> findByTags(@Param("tags") Collection<String> tags, @Param("tagCount") Long tagCount, Pageable pageable);

    @Query("SELECT a2 FROM Article a2 JOIN a2.tags t WHERE LOWER(t.name) in (:tags)")
    Page<Article> findByTags(@Param("tags") Collection<String> tags, Pageable pageable);

//    List<Article> findAllByStatus();

}
