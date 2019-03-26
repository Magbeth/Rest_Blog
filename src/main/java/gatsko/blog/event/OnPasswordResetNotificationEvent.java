package gatsko.blog.event;

import org.springframework.context.ApplicationEvent;

import java.util.Locale;

public class OnPasswordResetNotificationEvent extends ApplicationEvent {
    private String email;
    private Locale locale;

    public OnPasswordResetNotificationEvent(String email, Locale locale) {
        super(email);
        this.email = email;
        this.locale = locale;
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
