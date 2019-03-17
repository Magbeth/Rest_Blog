package gatsko.blog.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import gatsko.blog.model.Article;
import gatsko.blog.model.Tag;
import gatsko.blog.repository.ArticleRepository;
import gatsko.blog.repository.TagRepository;
import gatsko.blog.service.ArticleService;
import gatsko.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ArticlesController {
    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TagRepository tagRepository;

    @GetMapping(value = "/articles/{articleId}")
    public Article showArticle(@PathVariable("articleId") Long articleId) {
        Article article = articleService.getArticle(articleId);
        return article;
    }

    @GetMapping(value = "/articles")
    public List<Article> getPublicArticlesList() {
        List<Article> articles = articleRepository.findAll();
        return articles;
    }

    @PostMapping(value = "/articles")
    @ResponseStatus(HttpStatus.CREATED)
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
    @ResponseStatus(HttpStatus.OK)
    public String deleteArticle(@PathVariable("articleId") Long articleId) {
        articleService.deleteArticle(articleId);
        return "ok";
    }

    @GetMapping(value = "/{username}/articles")
    public List<Article> getUserArticlesList(@PathVariable("username") String username) {
        List<Article> articles = articleRepository.findAllByUser_Username(username);
        return articles;
    }

    @PutMapping(value = "articles/{articleId}")
    public String editArticle(Article editedArticle, @PathVariable("articleId") Long articleId, @RequestParam("tagNames") String editedTags) {
        editedArticle.setId(articleId);
        List<String> tagNames = Arrays.stream(editedTags.split(",")).map(String::trim).distinct().collect(Collectors.toList());
        Collection<Tag> tags = new ArrayList<>();
        for (String name : tagNames) {
            Tag tag = new Tag();
            tag.setName(name);
            tags.add(tag);
        }
        editedArticle.setTags(tags);
        articleService.updateArticle(editedArticle);
        return "ok";

    }

    @GetMapping(value ="tag-cloud", params = {"tagName"})
    public String getArticlesWithTagCount(@RequestParam("tagName") String tagName) {
        Long count = articleRepository.findArticleCountByTag(tagName);
        return tagName + " " + count;
    }
}
