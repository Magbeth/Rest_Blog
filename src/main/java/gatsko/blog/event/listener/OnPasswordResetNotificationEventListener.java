package gatsko.blog.event.listener;

import gatsko.blog.event.OnPasswordLinkRequestEvent;
import gatsko.blog.event.OnPasswordResetNotificationEvent;
import gatsko.blog.service.MailService;
import gatsko.blog.service.apiInterface.UserService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class OnPasswordResetNotificationEventListener implements ApplicationListener<OnPasswordResetNotificationEvent> {
    private MessageSource messageSource;
    private MailSender mailSender;
    private MailService mailService;

    public OnPasswordResetNotificationEventListener(MessageSource messageSource, MailSender mailSender,
                                              MailService mailService) {
        this.mailSender = mailSender;
        this.mailService = mailService;
        this.messageSource = messageSource;
    }

    @Override
    public void onApplicationEvent(OnPasswordResetNotificationEvent event) {
        sendResetLink(event);
    }

    private void sendResetLink(OnPasswordResetNotificationEvent event) {
        String email = event.getEmail();
        String subject = "Password changed";
        String message = messageSource.getMessage("Your password has been changed. ", null, event.getLocale());
        SimpleMailMessage resetEmail = mailService.constructEmail(subject, message, email);
        mailSender.send(resetEmail);
    }
}
