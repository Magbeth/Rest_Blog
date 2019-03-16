package gatsko.blog.service;

import gatsko.blog.model.Comment;
import org.springframework.stereotype.Service;

public interface CommentService {
    Comment saveNewComment(Comment comment);
}
