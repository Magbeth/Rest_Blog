package gatsko.blog.controller;

import gatsko.blog.model.Article;
import gatsko.blog.model.DTO.ArticleDTO;
import gatsko.blog.model.Tag;
import gatsko.blog.repository.ArticleRepository;
import gatsko.blog.repository.TagRepository;
import gatsko.blog.service.ArticleService;
import gatsko.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ArticlesController {

    private final ArticleService articleService;
    private final ArticleRepository articleRepository;

    public ArticlesController(ArticleService articleService, ArticleRepository articleRepository) {
        this.articleService = articleService;
        this.articleRepository = articleRepository;
    }

    @GetMapping(value = "/articles/{articleId}")
    @ResponseStatus(value = HttpStatus.OK)
    public Article showArticle(@PathVariable("articleId") Long articleId) {
        return articleService.getArticle(articleId);
    }

    @GetMapping(value = "/articles")
    @PreAuthorize("isAnonymous()")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Article> getPublicArticlesList() {
        List<Article> articles = articleRepository.findAll();
        return articles;
    }

    @PostMapping(value = "/articles")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(value = HttpStatus.CREATED)
    public Article createArticle(Article article, @RequestParam("tagNames") String tags) {
        List<String> tagNames = Arrays.stream(tags.split(",")).map(String::trim).distinct().collect(Collectors.toList());
        Collection<Tag> tags2 = new ArrayList<>();
        for (String name : tagNames) {
            Tag tag = new Tag();
            tag.setName(name);
            tags2.add(tag);
        }
//        article.setTags(tags2);
        return articleService.saveNewArticle(article, tags2);
    }

    @GetMapping(value = "/articles", params = {"tagged"})
    public List<Article> searchByTag(@RequestParam("tagged") String tagsStr, @RequestParam(value = "page", defaultValue = "0") Integer pageNumber
    ) {
        List<String> tagNames = Arrays.stream(tagsStr.split(",")).map(String::trim).distinct().collect(Collectors.toList());
        Page<Article> articlesPage = articleService.findArticleByTag(tagNames, pageNumber, 10);
        return articlesPage.getContent();
    }


    @DeleteMapping(value = "articles/{articleId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteArticle(@PathVariable("articleId") Long articleId) {
        Article article = articleService.getArticle(articleId);
        articleService.deleteArticle(article);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{username}/articles")
    @ResponseStatus(value = HttpStatus.OK)
    public List<Article> getUserArticlesList(@PathVariable("username") String username) {
        List<Article> articles = articleRepository.findAllByUser_Username(username);
        return articles;
    }

    @PutMapping(value = "articles/{articleId}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Article editArticle(ArticleDTO editedData, @PathVariable("articleId") Long articleId, @RequestParam("tagNames") String editedTags) {
        List<String> tagNames = Arrays.stream(editedTags.split(",")).map(String::trim).distinct().collect(Collectors.toList());
        Collection<Tag> tags = new ArrayList<>();
        for (String name : tagNames) {
            Tag tag = new Tag();
            tag.setName(name);
            tags.add(tag);
        }
        editedData.setTags(tags);
        Article articleToEdit = articleService.getArticle(articleId);
        return articleService.updateArticle(articleToEdit, editedData);
    }

    @GetMapping(value ="tag-cloud", params = {"tagName"})
    @ResponseStatus(value = HttpStatus.OK)
    public String getArticlesWithTagCount(@RequestParam("tagName") String tagName) {
        Long count = articleRepository.findArticleCountByTag(tagName);
        return tagName + " " + count;
    }
}
