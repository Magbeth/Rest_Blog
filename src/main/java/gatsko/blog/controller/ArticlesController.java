package gatsko.blog.controller;

import gatsko.blog.model.Article;
import gatsko.blog.model.dto.ArticleDTO;
import gatsko.blog.model.dto.TagCloudResponse;
import gatsko.blog.service.apiInterface.ArticleService;
import gatsko.blog.service.apiInterface.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ArticlesController {
    private UserService userService;
    private final ArticleService articleService;

    public ArticlesController(ArticleService articleService, UserService userService) {
        this.articleService = articleService;
        this.userService = userService;
    }

    @GetMapping(value = "/articles/{articleId}")
    @ResponseStatus(value = HttpStatus.OK)
    public ArticleDTO showArticle(@PathVariable("articleId") Long articleId) {
        return articleService.getArticleForReading(articleId);
    }

    @GetMapping(value = "/articles")
    @ResponseStatus(value = HttpStatus.OK)
    public Page<Article> getPublicArticlesList(@RequestParam(value = "page", defaultValue = "0") Integer pageNumber,
                                               @RequestParam(value = "size", defaultValue = "10") Integer pageSize,
                                               @RequestParam(value = "sort", defaultValue = "createdAt") String properties,
                                               @RequestParam(value = "order", defaultValue = "DESC") String order) {
        Sort sort = createSortRequest(order, properties);
        return articleService.getArticlesPage(pageNumber, pageSize, sort);
    }

    @PostMapping(value = "/articles")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(value = HttpStatus.CREATED)
    public ArticleDTO createArticle(@Valid @RequestBody ArticleDTO article) {
        return articleService.saveNewArticle(article);
    }

    @GetMapping(value = "/articles", params = {"tagged"})
    public Page<Article> searchByTag(@RequestParam("tagged") String tagsStr,
                                     @RequestParam(value = "page", defaultValue = "0") Integer pageNumber,
                                     @RequestParam(value = "size", defaultValue = "10") Integer pageSize,
                                     @RequestParam(value = "sort", defaultValue = "createdAt") String properties,
                                     @RequestParam(value = "order", defaultValue = "DESC") String order
    ) {
        Sort sort = createSortRequest(order, properties);
        List<String> tagNames = Arrays.stream(tagsStr.split(",")).map(String::trim).map(String::toLowerCase).distinct().collect(Collectors.toList());
        return articleService.findArticleByTag(tagNames, pageNumber, pageSize, sort);
    }


    @DeleteMapping(value = "articles/{articleId}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteArticle(@PathVariable("articleId") Long articleId) {
        Article article = articleService.getArticle(articleId);
        articleService.deleteArticle(article);
    }

    @GetMapping(value = "/{username}/articles")
    @ResponseStatus(value = HttpStatus.OK)
    public Page<Article> getUserArticlesList(@PathVariable("username") String username,
                                             @RequestParam(value = "page", defaultValue = "0") Integer pageNumber,
                                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                             @RequestParam(value = "sort", defaultValue = "createdAt") String properties,
                                             @RequestParam(value = "order", defaultValue = "DESC") String order) {
        Sort sort = createSortRequest(order, properties);
        return articleService.getUserArticlesPage(username, pageNumber, pageSize, sort);
    }

    @PutMapping(value = "articles/{articleId}")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(value = HttpStatus.OK)
    public ArticleDTO editArticle(@Valid @RequestBody ArticleDTO editedData, @PathVariable("articleId") Long articleId) {
        Article articleToEdit = articleService.getArticle(articleId);
        return articleService.updateArticle(articleToEdit, editedData);
    }

    @GetMapping(value = "tag-cloud", params = {"tags"})
    @ResponseStatus(value = HttpStatus.OK)
    public TagCloudResponse getArticlesWithTagCount(@RequestParam("tags") String tags) {
        List<String> tagNames = Arrays.stream(tags.split(",")).map(String::trim).map(String::toLowerCase).distinct().collect(Collectors.toList());
        Long count = articleService.countArticlesWithTag(tagNames);
        return new TagCloudResponse(tagNames, count);
    }

    @GetMapping(value = "/my")
    @ResponseStatus(value = HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public Page<Article> getUserArticlesList(@RequestParam(value = "page", defaultValue = "0") Integer pageNumber,
                                             @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize,
                                             @RequestParam(value = "sort", defaultValue = "createdAt") String properties,
                                             @RequestParam(value = "order", defaultValue = "DESC") String order) {
        String myUsername = userService.currentUser().getUsername();
        Sort sort = createSortRequest(order, properties);
        return articleService.getUserArticlesPage(myUsername, pageNumber, pageSize, sort);
    }

    //                    =================================
    //                       utility logic. to be replaced
    //                    =================================

    //enum of article properties for validating passed to URL arguments
    //Maybe here we can just throw exception in case of wrong property
    enum ArticleProperties {
        createdAt, updatedAt, title, id, status
    }

    private Sort createSortRequest(String order, String properties) {
        //Splitting passed properties to array and removing invalid properties
        List<String> sortingProperties =
                Arrays.stream(properties.split(","))
                        .map(String::trim)
                        .distinct()
                        .filter(property -> Arrays.stream(ArticleProperties.values()).anyMatch(t -> t.name().equals(property)))
                        .collect(Collectors.toList());
        //Setting default sorting value
        if (sortingProperties.size() == 0) {
            sortingProperties.add("createdAt");
        }
        Sort.Direction sortOrder =
                (order.toUpperCase().equals("ASC")) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return new Sort(sortOrder, sortingProperties);
    }
}
