package gatsko.blog.service;

import gatsko.blog.model.Article;
import gatsko.blog.model.Comment;
import gatsko.blog.repository.ArticleRepository;
import gatsko.blog.repository.CommentRepository;
import gatsko.blog.service.apiInterface.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@RunWith(MockitoJUnitRunner.class)
public class CommentServiceImplTest {
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private UserService userService;
    @Mock
    private PageRequest pageRequest;
    @Mock
    private Sort sort;

    @InjectMocks
    private CommentServiceImpl sut;

    @Test
    public void deleteComment_givenComment_shouldDeleteCommentRepository() {
        //given
        Article article = new Article();
        Comment comment = new Comment();
        comment.setId(1L);
        //when
        sut.deleteCommentFromArticle(comment, article);
        //then
        verify(commentRepository, times(1)).delete(any(Long.class));

    }

    @Test
    public void getComment() {
        Long commentId = 1L;
        Long articleId = 1L;
        when(articleRepository.existsById(articleId)).thenReturn(true);
        when(commentRepository.exists(commentId)).thenReturn(true);
        sut.getComment(commentId, articleId);
        verify(commentRepository, times(1)).findOne(any(Long.class));

    }

    @Test
    public void saveComment() {
        Comment comment = new Comment();
        when(articleRepository.existsById(any(Long.class))).thenReturn(true);
        sut.saveNewComment(comment, any(Long.class));
        verify(articleRepository, times(1)).findOne(any(Long.class));
        verify(commentRepository, times(1)).saveAndFlush(comment);
    }

    @Test
    public void getCommentsForArticle() {
        int pageNumber = 1;
        int pageSize = 20;
        when(articleRepository.existsById(any(Long.class))).thenReturn(true);
        sut.getCommentsForArticle(any(Long.class), pageNumber, pageSize, sort);
        verify(commentRepository, times(1)).findAllByArticle_Id(any(Long.class), any(PageRequest.class));

    }

}