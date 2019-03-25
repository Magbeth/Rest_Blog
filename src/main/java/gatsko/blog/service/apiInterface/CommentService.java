package gatsko.blog.service.apiInterface;

import gatsko.blog.model.Article;
import gatsko.blog.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;

public interface CommentService {
    Comment saveNewComment(Comment comment, Long parentArticleId);

    Comment getComment(Long commentId, Long articleId);

    @PreAuthorize("#comment.user.username == authentication.name OR #article.user.username == authentication.name")
    void deleteCommentFromArticle(Comment comment, Article article);

    Page<Comment> getCommentsForArticle(Long id, int pageNumber, int pageSize, Sort sort);
}
