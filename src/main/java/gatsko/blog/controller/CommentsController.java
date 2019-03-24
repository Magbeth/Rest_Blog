package gatsko.blog.controller;

import gatsko.blog.model.Article;
import gatsko.blog.model.Comment;
import gatsko.blog.service.ApiInterface.ArticleService;
import gatsko.blog.service.ApiInterface.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
    public Page<Comment> getArticleCommentsList(@PathVariable("articleId") Long articleId,
                                                @RequestParam(value = "page", defaultValue = "0") Integer pageNumber,
                                                @RequestParam(value = "size", defaultValue = "10") Integer pageSize,
                                                @RequestParam(value = "sort", defaultValue = "createdAt") String properties,
                                                @RequestParam(value = "order", defaultValue = "DESC") String order) {
        Sort sort = createSortRequest(order, properties);
        return commentService.getCommentsForArticle(articleId, pageNumber, pageSize, sort);
    }

    @GetMapping(value = "/articles/{articleId}/comments/{commentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Comment showComment(@PathVariable("articleId") Long articleId, @PathVariable("commentId") Long commentId) {
        return commentService.getComment(commentId, articleId);
    }

    @DeleteMapping(value = "/articles/{articleId}/comments/{commentId}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("articleId") Long articleId, @PathVariable("commentId") Long commentId) {
        Article article = articleService.getArticle(articleId);
        Comment comment = commentService.getComment(commentId, articleId);
        commentService.deleteCommentFromArticle(comment, article);
    }

    //                    =================================
    //                       utility logic. to be replaced
    //                    =================================
    enum CommentProperties {createdAt, id}

    private Sort createSortRequest(String order, String properties) {
        //Splitting passed properties to array and removing invalid properties
        List<String> sortingProperties =
                Arrays.stream(properties.split(","))
                        .map(String::trim)
                        .distinct()
                        .filter(property -> Arrays.stream(CommentProperties.values()).anyMatch(t -> t.name().equals(property)))
                        .collect(Collectors.toList());
        //Setting default sorting value
        if (sortingProperties.size() == 0) {
            sortingProperties.add("createdAt");
        }
        Sort.Direction sortOrder =
                (order.toUpperCase().equals("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return new Sort(sortOrder, sortingProperties);
    }


}
