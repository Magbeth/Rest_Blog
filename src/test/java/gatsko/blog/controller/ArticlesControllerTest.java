package gatsko.blog.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import gatsko.blog.config.JpaConfiguration;
import gatsko.blog.config.SecurityConfig;
import gatsko.blog.config.WebConfig;
import gatsko.blog.model.dto.LoginRequest;
import gatsko.blog.security.JwtAuthTokenFilter;
import gatsko.blog.service.apiInterface.ArticleService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestContext.class, WebConfig.class, JpaConfiguration.class, SecurityConfig.class})
@WebAppConfiguration
public class ArticlesControllerTest {
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
                .apply(springSecurity())
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
    public void getUserArticles() throws Exception {
        mockMvc.perform(get("/my"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails("111111")
    public void getAuthUserArticles() throws Exception {
        mockMvc.perform(get("/my"))
                .andExpect(status().isOk());
    }


    @Test
    @WithAnonymousUser
    public void deleteArticleWithAnonymousUser() throws  Exception {
        mockMvc.perform(delete("/articles/76"))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithAnonymousUser
    public void successfulAuthenticationWithAnonymousUser() throws Exception {

        LoginRequest loginRequest = new LoginRequest("555555", "123456");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(loginRequest)))
                .andExpect(status().is2xxSuccessful());
    }


}