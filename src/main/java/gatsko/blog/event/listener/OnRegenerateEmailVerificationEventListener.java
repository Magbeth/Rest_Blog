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
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MailSender mailSender;
    @Autowired
    private MailService mailService;

    @Override
    @Async
    public void onApplicationEvent(OnRegenerateEmailVerificationEvent onRegenerateEmailVerificationEvent) {
        sendRegeneratedToken(onRegenerateEmailVerificationEvent);
    }

    private void sendRegeneratedToken(OnRegenerateEmailVerificationEvent event) {
        User user = event.getUser();
        String token = event.getToken();
        String subject = "Registration Confirmation";
//        String recipientAddress = user.getEmail();
        String confirmationUrl
                = event.getRedirectUrl() + "/registrationConfirm.html?token=" + token;
        String message = messageSource.getMessage("Thank you for registering. Please click on the below link to activate your account. ", null, event.getLocale());
        String mailBody = message + "http://localhost:8080/auth" + confirmationUrl;
        SimpleMailMessage email = mailService.constructEmail(subject, mailBody, user.getEmail());
//        email.setTo(recipientAddress);
//        email.setSubject(subject);
//        email.setText(message + "http://localhost:8080/auth" + confirmationUrl);
        mailSender.send(email);
    }
}
