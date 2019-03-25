package gatsko.blog.event;

import gatsko.blog.model.User;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class OnRegenerateEmailVerificationEvent extends ApplicationEvent {
    private String redirectUrl;
    private String email;
    private String token;
    private Locale locale;

    public OnRegenerateEmailVerificationEvent(String email, String redirectUrl, Locale locale, String token) {
        super(email);
        this.redirectUrl = redirectUrl;
        this.email = email;
        this.locale = locale;
        this.token = token;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
