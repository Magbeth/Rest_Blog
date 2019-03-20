package gatsko.blog.controller;

import gatsko.blog.model.Article;
import gatsko.blog.model.Comment;
import gatsko.blog.repository.CommentRepository;
import gatsko.blog.service.ArticleService;
import gatsko.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentsController {
    private final CommentService commentService;
    private final ArticleService articleService;

    public CommentsController(CommentService commentService, ArticleService articleService) {
        this.commentService = commentService;
        this.articleService = articleService;
    }

    @PostMapping(value = "articles/{articleId}/comments")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Comment createComment(@PathVariable("articleId") Long articleId, Comment comment) {
        return commentService.saveNewComment(comment, articleId);
    }

    @GetMapping(value = {"/articles/{articleId}/comments"})
    @ResponseStatus(value = HttpStatus.OK)
    public List<Comment> getUserCommentsList(@PathVariable("articleId") Long articleId) {
        return commentService.findAllByArticle_Id(articleId);
    }

    @GetMapping(value = "/articles/{articleId}/comments/{commentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Comment showComment(@PathVariable("articleId") Long articleId, @PathVariable("commentId") Long commentId) {
        return commentService.getComment(commentId);
    }

    @DeleteMapping(value = "/articles/{articleId}/comments/{commentId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteComment(@PathVariable("articleId") Long articleId, @PathVariable("commentId") Long commentId) {
        Article article = articleService.getArticle(articleId);
        Comment comment = commentService.getComment(commentId);
        commentService.deleteCommentFromArticle(comment, article);
        return ResponseEntity.noContent().build();
    }


}
