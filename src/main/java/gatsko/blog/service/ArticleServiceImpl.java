package gatsko.blog.service;

import gatsko.blog.model.Article;
import gatsko.blog.model.Tag;
import gatsko.blog.repository.ArticleRepository;
import gatsko.blog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TagService tagService;

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
    public Article saveNewArticle(Article article, Collection<Tag> tags) {
        article.setCreatedAt(LocalDateTime.now());
        article.setUser(userService.currentUser());
        Collection<Tag> articleTags = new ArrayList<>();
        for (Tag tag : tags) {
            articleTags.add(tagService.saveTag(tag));
        }
        article.setTags(articleTags);
        articleRepository.saveAndFlush(article);
        return article;
    }

    @Transactional
    @Override
    public void deleteArticle(Article article){
        articleRepository.delete(article.getId());
    }

    //TODO: preauthorize for author
    @Transactional
    @Override
    public Article updateArticle(Article editedArticle) {
        Article article = articleRepository.findOne(editedArticle.getId());
        article.setFullPostText(editedArticle.getFullPostText());
        article.setTitle(editedArticle.getTitle());
        article.setUpdatedAt(LocalDateTime.now());
        article.setStatus(editedArticle.getStatus());
        Collection<Tag> editedTags = editedArticle.getTags();
        Collection<Tag> articleTags = new ArrayList<>();
        for (Tag tag : editedTags) {
            articleTags.add(tagService.saveTag(tag));
        }
        article.setTags(articleTags);
        return article;
    }

}
