package gatsko.blog.event.listener;

import gatsko.blog.event.OnPasswordLinkRequestEvent;
import gatsko.blog.service.MailService;
import gatsko.blog.service.apiInterface.UserService;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

@Component
public class OnPasswordLinkRequestEventListener implements ApplicationListener<OnPasswordLinkRequestEvent> {
    private MessageSource messageSource;
    private MailSender mailSender;
    private MailService mailService;
    private UserService userService;

    public OnPasswordLinkRequestEventListener(MessageSource messageSource, MailSender mailSender,
                                              MailService mailService, UserService userService) {
        this.mailSender = mailSender;
        this.mailService = mailService;
        this.messageSource = messageSource;
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(OnPasswordLinkRequestEvent event) {
        sendResetLink(event);
    }

    private void sendResetLink(OnPasswordLinkRequestEvent event) {
        String email = event.getEmail();
        String token = userService.generatePasswordResetToken(email);
        String subject = "Password reset";
        String confirmationUrl
                = event.getRedirectUrl() + "/auth/resetPassword.html?token=" + token;
        String message = messageSource
                .getMessage("Please click on the below link to reset your password. ", null, event.getLocale());
        String mailBody = message + "http://localhost:8080" + confirmationUrl;
        SimpleMailMessage resetEmail = mailService.constructEmail(subject, mailBody, email);
        mailSender.send(resetEmail);
    }
}
