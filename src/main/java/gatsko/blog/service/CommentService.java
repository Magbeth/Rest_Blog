package gatsko.blog.service;

import gatsko.blog.model.Comment;

public interface CommentService {
    Comment saveNewComment(Comment comment);
    Comment getComment(Long commentId);
    void deleteComment(Long commentId);
}
