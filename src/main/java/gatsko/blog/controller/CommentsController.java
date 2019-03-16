package gatsko.blog.controller;

import gatsko.blog.model.Article;
import gatsko.blog.model.Comment;
import gatsko.blog.service.ArticleService;
import gatsko.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommentsController {
    @Autowired
    private ArticleService articleService;

    @Autowired
    private CommentService commentService;

    @PostMapping(value = "{articleId}/reply")
    @ResponseStatus(HttpStatus.CREATED)
    public String createComment(@PathVariable("articleId") Long articleId, Comment comment) {
        Article article = articleService.getArticle(articleId);
        comment.setArticle(article);
        commentService.saveNewComment(comment);
        return "ok";
    }


}
