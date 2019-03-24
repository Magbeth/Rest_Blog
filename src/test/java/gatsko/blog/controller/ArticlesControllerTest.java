package gatsko.blog.controller;

import gatsko.blog.config.JpaConfiguration;
import gatsko.blog.config.SecurityConfig;
import gatsko.blog.config.WebConfig;
import gatsko.blog.model.User;
import gatsko.blog.security.JwtAuthTokenFilter;
import gatsko.blog.service.ApiInterface.ArticleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.nio.charset.Charset;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class, WebConfig.class, JpaConfiguration.class, SecurityConfig.class})
@WebAppConfiguration
public class ArticlesControllerTest {
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8")
    );
    @Test
    public void contextLoads() {
    }

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ArticleService articleService;

    @Autowired
    protected JwtAuthTokenFilter jwtAuthTokenFilter;


    @Before
    public void setUp() {
        Mockito.reset(articleService);
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(jwtAuthTokenFilter)
                .build();
    }

    @Test
    @WithAnonymousUser
    public void getArticles() throws Exception {
        mockMvc.perform(get("/articles"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    public void postArticles() throws Exception {
        mockMvc.perform(get("/my"))
                .andExpect(status().isUnauthorized());
    }







}