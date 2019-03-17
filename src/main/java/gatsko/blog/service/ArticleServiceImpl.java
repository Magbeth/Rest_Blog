package gatsko.blog.service;

import gatsko.blog.model.Article;
import gatsko.blog.repository.ArticleRepository;
import gatsko.blog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserService userService;


    @Override
    public Page<Article> getArticlesPage(int pageNumber, int pageSize) {
        PageRequest pageRequest = new PageRequest(pageNumber, pageSize, Sort.Direction.DESC, "dateTime");
        //TODO: return only public articles for non-authorized users.
        return articleRepository.findAll(pageRequest);
    }


    //TODO: Check for authorisation and article status.
    @Override
    public Article getArticle(Long id) {
        return articleRepository.findOne(id);
    }

    //TODO: return only public articles for non-authorized users, implement check for status in repository
    @Override
    public Page<Article> findArticleByTag(List<String> tags, int pageNumber, int pageSize) {
        tags = tags.stream().map(String::toLowerCase).collect(Collectors.toList());
        PageRequest pageRequest = new PageRequest(pageNumber, pageSize, Sort.Direction.DESC, "createdAt");
        return articleRepository.findByTags(tags, pageRequest);
    }


    @Override
    public Article saveNewArticle(Article article) {
        article.setCreatedAt(LocalDateTime.now());
        article.setUser(userService.currentUser());
        articleRepository.saveAndFlush(article);
        return article;
    }

    //TODO: check for user is article author
   @Transactional
    @Override
    public void deleteArticle(Long articleId) {
//        Article article = getArticle(articleId);
//        if (article.getUser().equals(userService.currentUser())) {
//
//        }
        articleRepository.delete(articleId);
        articleRepository.flush();
    }

    //TODO: check for user is article author
    @Override
    public Article updateArticle(Article editedArticle) {
        Article article = articleRepository.findOne(editedArticle.getId());
        article.setFullPostText(editedArticle.getFullPostText());
        article.setTitle(editedArticle.getTitle());
        article.setUpdatedAt(LocalDateTime.now());
        article.setStatus(editedArticle.getStatus());
        articleRepository.saveAndFlush(article);
        return article;
    }

}
