package gatsko.blog.service;

import gatsko.blog.model.Article;
import gatsko.blog.repository.ArticleRepository;
import gatsko.blog.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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


        return articleRepository.findAll(pageRequest);
    }


    @Override
    public Article getArticle(Long id) {
        return articleRepository.findOne(id);
    }



    @Override
    public Page<Article> findArticleByTag(List<String> tags, int pageNumber, int pageSize) {
        tags = tags.stream().map(String::toLowerCase).collect(Collectors.toList());

        PageRequest pageRequest = new PageRequest(pageNumber, pageSize, Sort.Direction.DESC, "dateTime");


        return articleRepository.findByTags(tags, pageRequest);
    }

    @Override
    public Article saveNewArticle(Article article) {
        article.setDateTime(LocalDateTime.now());
        article.setUser(userService.currentUser());
        articleRepository.saveAndFlush(article);
        return article;
    }

}
