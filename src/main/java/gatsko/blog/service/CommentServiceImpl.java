package gatsko.blog.service;

import gatsko.blog.exception.ResourceNotFoundException;
import gatsko.blog.model.Article;
import gatsko.blog.model.Comment;
import gatsko.blog.repository.ArticleRepository;
import gatsko.blog.repository.CommentRepository;
import gatsko.blog.service.apiInterface.CommentService;
import gatsko.blog.service.apiInterface.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service("commentService")
public class CommentServiceImpl implements CommentService {

    private final UserService userService;
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    public CommentServiceImpl(UserService userService, CommentRepository commentRepository,
                              ArticleRepository articleRepository) {
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public Comment saveNewComment(Comment comment, Long parentArticleId) {
        if (!articleRepository.existsById(parentArticleId)) {
            throw new ResourceNotFoundException("Article with id " + parentArticleId + " doesn't exists");
        }
        Article parentArticle = articleRepository.findOne(parentArticleId);
        comment.setArticle(parentArticle);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUser(userService.currentUser());
        commentRepository.saveAndFlush(comment);
        return comment;
    }

    @Override
    public Comment getComment(Long commentId, Long articleId) {
        if (!articleRepository.existsById(articleId)) {
            throw new ResourceNotFoundException("Article with id " + articleId + " doesn't exists");
        }
        if (!commentRepository.exists(commentId)) {
            throw new ResourceNotFoundException("Comment with id " + commentId + " doesn't exist");
        }
        return commentRepository.findOne(commentId);
    }

    @Transactional
    @Override
    public void deleteCommentFromArticle(Comment comment, Article article) {
        commentRepository.delete(comment.getId());
    }

    @Override
    public Page<Comment> getCommentsForArticle(Long id, int pageNumber, int pageSize, Sort sort) {
        if (!articleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Article with id " + id + " doesn't exist");
        }
        PageRequest pageRequest = new PageRequest(pageNumber, pageSize, sort);
        return commentRepository.findAllByArticle_Id(id, pageRequest);
    }
}
