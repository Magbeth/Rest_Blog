package gatsko.blog.event;

import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class OnPasswordLinkRequestEvent extends ApplicationEvent {
    private String redirectUrl;
    private String email;
    private Locale locale;

    public OnPasswordLinkRequestEvent(String redirectUrl, String email, Locale locale) {
        super(email);
        this.redirectUrl = redirectUrl;
        this.email = email;
        this.locale = locale;
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

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
