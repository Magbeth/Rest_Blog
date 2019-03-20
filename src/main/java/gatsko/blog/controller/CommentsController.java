package gatsko.blog.controller;

import gatsko.blog.model.Article;
import gatsko.blog.model.Comment;
import gatsko.blog.repository.CommentRepository;
import gatsko.blog.service.ArticleService;
import gatsko.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CommentsController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @PostMapping(value = "articles/{articleId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public String createComment(@PathVariable("articleId") Long articleId, Comment comment) {
        Article article = articleService.getArticle(articleId);
        comment.setArticle(article);
        commentService.saveNewComment(comment);
        return "ok";
    }

    @GetMapping(value = {"/articles/{articleId}/comments"})
    public List<Comment> getUserCommentsList(@PathVariable("articleId") Long articleId) {
        return commentRepository.findAllByArticle_Id(articleId);
    }

    @GetMapping(value = "/articles/{articleId}/comments/{commentId}")
    public Comment showComment(@PathVariable("articleId") Long articleId, @PathVariable("commentId") Long commentId) {
        return commentService.getComment(commentId);
    }

    @DeleteMapping(value = "/articles/{articleId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public String deleteComment(@PathVariable("articleId") Long articleId, @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return "ok";
    }


}
