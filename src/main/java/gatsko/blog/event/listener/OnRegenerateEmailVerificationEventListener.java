package gatsko.blog.event.listener;

import gatsko.blog.event.OnRegenerateEmailVerificationEvent;
import gatsko.blog.model.User;
import gatsko.blog.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OnRegenerateEmailVerificationEventListener implements ApplicationListener<OnRegenerateEmailVerificationEvent> {
    private final MessageSource messageSource;
    private final MailSender mailSender;
    private final MailService mailService;

    public OnRegenerateEmailVerificationEventListener(MailService mailService, MessageSource messageSource,
                                                      MailSender mailSender) {
        this.mailSender = mailSender;
        this.mailService = mailService;
        this.messageSource = messageSource;
    }

    @Override
    @Async
    public void onApplicationEvent(OnRegenerateEmailVerificationEvent onRegenerateEmailVerificationEvent) {
        sendRegeneratedToken(onRegenerateEmailVerificationEvent);
    }

    private void sendRegeneratedToken(OnRegenerateEmailVerificationEvent event) {
        String email = event.getEmail();
        String token = event.getToken();
        String subject = "Registration Confirmation";
        String confirmationUrl
                = event.getRedirectUrl() + "/registrationConfirm.html?token=" + token;
        String message = messageSource.getMessage("Thank you for registering. Please click on the below link to activate your account. ", null, event.getLocale());
        String mailBody = message + "http://localhost:8080/auth" + confirmationUrl;
        SimpleMailMessage emailMessage = mailService.constructEmail(subject, mailBody, email);
        mailSender.send(emailMessage);
    }
}
