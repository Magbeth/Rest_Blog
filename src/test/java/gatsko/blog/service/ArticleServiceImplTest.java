package gatsko.blog.service;

import gatsko.blog.model.Article;
import gatsko.blog.model.dto.ArticleDto;
import gatsko.blog.repository.ArticleRepository;
import gatsko.blog.service.apiInterface.TagService;
import gatsko.blog.service.apiInterface.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ArticleServiceImplTest {

    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private TagService tagService;
    @Mock
    private UserService userService;
    @Mock
    private PageRequest pageRequest;
    @Mock
    private Authentication authentication;
    @Mock
    private Sort sort;


    @InjectMocks
    private ArticleServiceImpl sut;


    @Test
    public void deleteArticle_givenArticle_shouldDeleteArticleRepository() {

//        when(articleRepository.delete(any(Article.class))).thenReturn("Mockito");
        //given
        Article article = new Article();
        article.setId(1L);
        //when
        sut.deleteArticle(article);
        //then
        verify(articleRepository, times(1)).delete(any(Long.class));

    }

    @Test
    public void getArticlesPage() {
        int pageSize = 10;
        int pageNumber = 1;
        sut.getArticlesPage(pageNumber, pageSize, sort);
        verify(articleRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    public void getArticle() {
        when(articleRepository.existsById(anyLong())).thenReturn(true);
        sut.getArticle(anyLong());
        verify(articleRepository, times(1)).findOne(anyLong());
    }

    @Test
    public void saveNewArticle() {
        Article article = new Article();
        sut.saveNewArticle(article);
        verify(articleRepository, times(1)).saveAndFlush(article);
    }

    @Test
    public void updateArticle() {
        Article article = new Article();
        ArticleDto articleDTO = new ArticleDto();
        sut.updateArticle(article, articleDTO);
        verify(articleRepository, times(1)).saveAndFlush(article);
    }

    @Test
    public void getUserArticlesPage() {
        int pageSize = 10;
        int pageNumber = 1;
        String username = "adm";
        sut.getUserArticlesPage(username, pageNumber, pageSize, sort);
        verify(articleRepository).findAllByUser_Username(anyString(), any(PageRequest.class));
    }

    @Test
    public void countArticlesWithTag() {
        Collection<String> tags = new ArrayList<>();
        sut.countArticlesWithTag(tags);
        verify(articleRepository).findArticleCountByTag(anyCollection(), anyLong());
    }


}