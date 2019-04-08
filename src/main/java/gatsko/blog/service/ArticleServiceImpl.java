package gatsko.blog.service;

import gatsko.blog.exception.ResourceNotFoundException;
import gatsko.blog.model.Article;
import gatsko.blog.model.enums.ArticleStatus;
import gatsko.blog.model.dto.ArticleDTO;
import gatsko.blog.model.Tag;
import gatsko.blog.repository.ArticleRepository;
import gatsko.blog.service.apiInterface.ArticleService;
import gatsko.blog.service.apiInterface.TagService;
import gatsko.blog.service.apiInterface.UserService;
import org.hibernate.Hibernate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.AccessControlException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service("articleService")
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;
    private final TagService tagService;
    private final UserService userService;

    public ArticleServiceImpl(ArticleRepository articleRepository, TagService tagService, UserService userService) {
        this.articleRepository = articleRepository;
        this.tagService = tagService;
        this.userService = userService;
    }


    @Override
    public Page<Article> getArticlesPage(int pageNumber, int pageSize, Sort sort) {
        PageRequest pageRequest = new PageRequest(pageNumber, pageSize, sort);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return articleRepository.findAllByStatus(ArticleStatus.PUBLIC, pageRequest);
        } else return articleRepository.findAll(pageRequest);
    }


    @Override
    public Article getArticle(Long id) {
        Article article = articleRepository.findOne(id);
        if (article == null) {
            throw new ResourceNotFoundException("Article with id " + id + " not found");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken && article.getStatus() != ArticleStatus.PUBLIC) {
            throw new AccessControlException("You have no permission to view this article. Please, Log in");
        } else return article;
    }

    @Override
    @Transactional
    public Article getArticleForReading(Long id) {
        Article article = articleRepository.findOne(id);
        if (article == null) {
            throw new ResourceNotFoundException("Article with id " + id + " not found");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken && article.getStatus() != ArticleStatus.PUBLIC) {
            throw new AccessControlException("You have no permission to view this article. Please, Log in");
        } else {
            Hibernate.initialize(article.getTags());
            return article;
        }
    }

    @Override
    public Page<Article> findArticleByTag(List<String> tags, int pageNumber, int pageSize, Sort sort) {
        tags = tags.stream().map(String::toLowerCase).collect(Collectors.toList());
        PageRequest pageRequest = new PageRequest(pageNumber, pageSize, sort);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return articleRepository.findPublicArticlesByTags(tags, pageRequest);
        }
        return articleRepository.findByTags(tags, pageRequest);
    }


    @Override
    public Article saveNewArticle(ArticleDTO article) {
        Article newArticle = new Article(article);
        newArticle.setCreatedAt(LocalDateTime.now());
        newArticle.setUser(userService.currentUser());
        if (article.getTags() != null) {
            Collection<Tag> articleTags = new ArrayList<>();
            for (Tag tag : article.getTags()) {
                articleTags.add(tagService.saveTag(tag));
            }
            newArticle.setTags(articleTags);
        }
        return articleRepository.saveAndFlush(newArticle);
    }

    @Transactional
    @Override
    public void deleteArticle(Article article) {
        articleRepository.delete(article.getId());
    }

    @Transactional
    @Override
    public Article updateArticle(Article article, ArticleDTO editedArticle) {
        article.setFullPostText(editedArticle.getFullPostText());
        article.setTitle(editedArticle.getTitle());
        article.setUpdatedAt(LocalDateTime.now());
        article.setStatus(editedArticle.getStatus());
        if (editedArticle.getTags() != null) {
            Collection<Tag> editedTags = editedArticle.getTags();
            Collection<Tag> articleTags = new ArrayList<>();
            for (Tag tag : editedTags) {
                articleTags.add(tagService.saveTag(tag));
            }
            article.setTags(articleTags);
        }
        return articleRepository.saveAndFlush(article);
    }

    @Override
    public Page<Article> getUserArticlesPage(String username, int pageNumber, int pageSize, Sort sort) {
        PageRequest pageRequest = new PageRequest(pageNumber, pageSize, sort);
        return articleRepository.findAllByUser_Username(username, pageRequest);
    }

    @Override
    public Long countArticlesWithTag(Collection<String> tags) {
        return articleRepository.findArticleCountByTag(tags, (long) tags.size());
    }

}
