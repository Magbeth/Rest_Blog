package gatsko.blog.event;

import gatsko.blog.model.User;
import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class OnUserRegistrationCompleteEvent extends ApplicationEvent {
    private String redirectUrl;
    private User user;
    private Locale locale;

    public OnUserRegistrationCompleteEvent(User user, String redirectUrl, Locale locale) {
        super(user);
        this.redirectUrl = redirectUrl;
        this.user = user;
        this.locale = locale;
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

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }


}
