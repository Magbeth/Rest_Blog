package gatsko.blog.controller;

import gatsko.blog.model.Article;
import gatsko.blog.model.User;
import gatsko.blog.repository.ArticleRepository;
import gatsko.blog.service.ArticleService;
import gatsko.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
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

    @GetMapping(value = "/article/{articleId}")
    public String showArticle(@PathVariable("articleId") Long articleId) {
        Article article = articleService.getArticle(articleId);
        return article.toString();
    }

    @GetMapping(value = {"/articles"})
    public String getArticlesList() {
        List<Article> articles = articleRepository.findAll();
        return articles.toString();
    }

    @PostMapping(value = "/article/create")
    @ResponseStatus(HttpStatus.CREATED)
    public String createArticle(Article article) {
        return articleService.saveNewArticle(article).toString();
    }

    @RequestMapping(value = "/articles", method = RequestMethod.GET, params = {"tagged"})
    public String searchByTag(@RequestParam("tagged") String tagsStr, @RequestParam(value = "page", defaultValue = "0") Integer pageNumber
    ) {
        List<String> tagNames = Arrays.stream(tagsStr.split(",")).map(String::trim).distinct().collect(Collectors.toList());
        Page<Article> postsPage = articleService.findArticleByTag(tagNames, pageNumber, 10);
        return postsPage.getContent().toString();
    }

    @DeleteMapping(value = "article/{articleId}/delete")
    @ResponseStatus(HttpStatus.OK)
    public String deleteArticle(@PathVariable("articleId") Long articleId) {
        articleService.deleteArticle(articleId);
        return "ok";
    }

    @GetMapping(value = {"/{username}/articles"})
    public String getUserArticlesList(@PathVariable("username") String username) {
        List<Article> articles = articleRepository.findAllByUser_Username(username);
        return articles.toString();
    }


}
