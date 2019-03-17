package gatsko.blog.controller;

import gatsko.blog.model.Article;
import gatsko.blog.model.Comment;
import gatsko.blog.repository.CommentRepository;
import gatsko.blog.service.ArticleService;
import gatsko.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public String createComment(@PathVariable("articleId") Long articleId, Comment comment) {
        Article article = articleService.getArticle(articleId);
        comment.setArticle(article);
        commentService.saveNewComment(comment);
        return "ok";
    }

    @GetMapping(value = {"/articles/{articleId}/comments"})
    public List<Comment> getUserArticlesList(@PathVariable("articleId") Long articleId) {
        List<Comment> comments = commentRepository.findAllByArticle_Id(articleId);
        return comments;
    }

    @GetMapping(value = "/articles/{articleId}/comments/{commentId}")
    public Comment showArticle(@PathVariable("articleId") Long articleId, @PathVariable("commentId") Long commentId) {
        Comment comment = commentService.getComment(commentId);
        return comment;
    }

    @DeleteMapping(value = "/articles/{articleId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public String deleteComment(@PathVariable("articleId") Long articleId, @PathVariable("commentId") Long commentId) {
        commentService.deleteComment(commentId);
        return "ok";
    }


}
