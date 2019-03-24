package gatsko.blog.event;

import gatsko.blog.model.User;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class OnRegenerateEmailVerificationEvent extends ApplicationEvent {
    private String redirectUrl;
    private User user;
    private String token;
    private Locale locale;

    public OnRegenerateEmailVerificationEvent(User user, String redirectUrl, Locale locale, String token) {
        super(user);
        this.redirectUrl = redirectUrl;
        this.user = user;
        this.locale = locale;
        this.token = token;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

}
