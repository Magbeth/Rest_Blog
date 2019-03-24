# Spring_blog_LeverX_lab
Practice Spring project for LeverX labs.
Simple REST blog with article and comment view and adding. Articles can be searched by tags. TagCloud counts articles number with specified tag.

Authorization passes through JWT tokens.

Security with JWT implemented based on guides from
<a href = https://grokonez.com/spring-framework/spring-security/spring-security-jwt-authentication-restapis-springboot-spring-mvc-spring-security-spring-jpa-mysql> this </a>
and <a href = https://www.toptal.com/java/rest-security-with-jwt-spring-security-and-java> this</a> articles.

#Implemented features:
# Article controller:
- Get all articles (TODO: Get only public articles for non-authenticated users)
- Get article by Id
- Post new article (Only for authenticated user)
- Delete article (Only for article author)
- Update article with new data (Only for article author)
- Get articles with specified tags (TODO: get only public articles for non-authenticated users)
- Get articles number with specified tag (Tag-cloud)

# Comment controller
- Show all comments for specified article
- Show specified comment for specified article
- Post new comment (Only for authenticated user)
- Delete comment (Only for article author or comment author)

# Authentication/Authorization controller
- Registry new user
- login user with generation of Jwt Token for requests authorization;
- Sending activation link to email after registration
- Resending activation link
- Reset password request. Sending token for resetting to email.

# Global exception handler

<b>TODO:</b> Fix hibernate.enable_lazy_load_no_trans anti-pattern. 



