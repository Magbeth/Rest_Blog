package gatsko.blog.controller;

import gatsko.blog.model.Article;
import gatsko.blog.model.Comment;
import gatsko.blog.model.dto.CommentDto;
import gatsko.blog.service.apiInterface.ArticleService;
import gatsko.blog.service.apiInterface.CommentService;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class CommentsController {
    private final CommentService commentService;
    private final ArticleService articleService;
    private final ModelMapper modelMapper;

    public CommentsController(CommentService commentService,
                              ArticleService articleService,
                              ModelMapper modelMapper) {
        this.commentService = commentService;
        this.articleService = articleService;
        this.modelMapper = modelMapper;
    }

    @PostMapping(value = "articles/{articleId}/comments")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable("articleId") Long articleId,
                                    @Valid @RequestBody CommentDto comment) {
        Comment newComment = modelMapper.map(comment, Comment.class);
        Comment savedComment = commentService.saveNewComment(newComment, articleId);
        return modelMapper.map(savedComment, CommentDto.class);
    }

    @GetMapping(value = {"/articles/{articleId}/comments"})
    @ResponseStatus(value = HttpStatus.OK)
    public Page<CommentDto> getArticleCommentsList(@PathVariable("articleId") Long articleId,
                                                   @RequestParam(value = "page", defaultValue = "0")
                                                           Integer pageNumber,
                                                   @RequestParam(value = "size", defaultValue = "10")
                                                           Integer pageSize,
                                                   @RequestParam(value = "sort", defaultValue = "createdAt")
                                                           String properties,
                                                   @RequestParam(value = "order", defaultValue = "DESC")
                                                           String order) {
        Sort sort = createSortRequest(order, properties);
        Page<Comment> commentPage = commentService.getCommentsForArticle(articleId, pageNumber, pageSize, sort);
        return commentPage.map(comment -> modelMapper.map(comment, CommentDto.class));
    }

    @GetMapping(value = "/articles/{articleId}/comments/{commentId}")
    @ResponseStatus(value = HttpStatus.OK)
    public CommentDto showComment(@PathVariable("articleId") Long articleId,
                                  @PathVariable("commentId") Long commentId) {
        Comment comment = commentService.getComment(commentId, articleId);
        return modelMapper.map(comment, CommentDto.class);
    }

    @DeleteMapping(value = "/articles/{articleId}/comments/{commentId}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable("articleId") Long articleId,
                              @PathVariable("commentId") Long commentId) {
        Article article = articleService.getArticle(articleId);
        Comment comment = commentService.getComment(commentId, articleId);
        commentService.deleteCommentFromArticle(comment, article);
    }

    //                    =================================
    //                       utility logic. to be replaced
    //                    =================================
    enum CommentProperties {
        createdAt, id
    }

    private Sort createSortRequest(String order, String properties) {
        //Splitting passed properties to array and removing invalid properties
        List<String> sortingProperties =
                Arrays.stream(properties.split(","))
                        .map(String::trim)
                        .distinct()
                        .filter(property ->
                                Arrays.stream(CommentProperties.values())
                                        .anyMatch(t -> t.name().equals(property)))
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
