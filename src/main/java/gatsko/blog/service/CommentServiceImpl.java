package gatsko.blog.service;

import gatsko.blog.model.Article;
import gatsko.blog.model.Comment;
import gatsko.blog.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service("commentService")
public class CommentServiceImpl implements CommentService {
    @Autowired
    private UserService userService;

    @Autowired
    private CommentRepository commentRepository;

    @Override
    public Comment saveNewComment(Comment comment) {
        comment.setCreatedAt(LocalDateTime.now());
        comment.setUser(userService.currentUser());
        commentRepository.saveAndFlush(comment);
        return comment;
    }

    @Override
    public Comment getComment(Long id) {
        return commentRepository.findOne(id);
    }

    @Override
    public void deleteComment(Long id) {
        commentRepository.delete(id);
        commentRepository.flush();
    }
}
