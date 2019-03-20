package gatsko.blog.service;

import gatsko.blog.model.Article;
import gatsko.blog.model.Comment;
import gatsko.blog.repository.ArticleRepository;
import gatsko.blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service("commentService")
public class CommentServiceImpl implements CommentService {

    private final UserService userService;
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    public CommentServiceImpl (UserService userService, CommentRepository commentRepository, ArticleRepository articleRepository) {
        this.userService = userService;
        this.commentRepository = commentRepository;
        this.articleRepository = articleRepository;
    }

    @Override
    public Comment saveNewComment(Comment comment, Long parentArticleId) {
        Article parentArticle = articleRepository.findOne(parentArticleId);
        comment.setArticle(parentArticle);
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUser(userService.currentUser());
        commentRepository.saveAndFlush(comment);
        return comment;
    }

    @Override
    public Comment getComment(Long id) {
        return commentRepository.findOne(id);
    }

    @Transactional
    @Override
    public void deleteCommentFromArticle(Comment comment, Article article) {
        commentRepository.delete(comment.getId());
    }

    @Override
    public List<Comment> findAllByArticle_Id(Long id) {
        return commentRepository.findAllByArticle_Id(id);
    }
}
