package gatsko.blog.controller;

import gatsko.blog.service.apiInterface.ArticleService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TestContext {
    @Bean
    public ArticleService articleService() {
        return Mockito.mock(ArticleService.class);
    }
}