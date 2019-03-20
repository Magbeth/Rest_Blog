package gatsko.blog.service;

import gatsko.blog.model.Article;
import gatsko.blog.model.Comment;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface CommentService {
    Comment saveNewComment(Comment comment, Long parentArticleId);

    Comment getComment(Long commentId);

    @PreAuthorize("#comment.user.username == authentication.name OR #article.user.username == authentication.name")
    void deleteCommentFromArticle(Comment comment, Article article);

    List<Comment> findAllByArticle_Id(Long id);
}
