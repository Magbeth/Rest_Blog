package gatsko.blog.event.listener;

import gatsko.blog.event.OnUserRegistrationCompleteEvent;
import gatsko.blog.model.User;
import gatsko.blog.service.EmailVerificationTokenService;
import gatsko.blog.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class OnUserRegistrationCompleteEventListener implements ApplicationListener<OnUserRegistrationCompleteEvent> {
    private final EmailVerificationTokenService emailVerificationTokenService;
    private final MessageSource messageSource;
    private final MailSender mailSender;
    private final MailService mailService;

    public OnUserRegistrationCompleteEventListener(EmailVerificationTokenService emailVerificationTokenService,
                                           MessageSource messageSource, MailSender mailSender,
                                           MailService mailService) {
        this.mailSender = mailSender;
        this.emailVerificationTokenService = emailVerificationTokenService;
        this.mailService = mailService;
        this.messageSource = messageSource;
    }

    @Override
    public void onApplicationEvent(OnUserRegistrationCompleteEvent onUserRegistrationCompleteEvent) {
        sendEmailVerification(onUserRegistrationCompleteEvent);
    }

    private void sendEmailVerification(OnUserRegistrationCompleteEvent event) {
        User user = event.getUser();
        String token = emailVerificationTokenService.generateNewToken();
        emailVerificationTokenService.createVerificationToken(user, token);

        String subject = "Registration Confirmation";
        String confirmationUrl
                = event.getRedirectUrl() + "/registrationConfirm.html?token=" + token;
        String message = messageSource.getMessage("Thank you for registering. Please click on the below link to activate your account. ", null, event.getLocale());
        String mailBody = message + "http://localhost:8080/auth" + confirmationUrl;
        SimpleMailMessage email = mailService.constructEmail(subject, mailBody, user.getEmail());
        mailSender.send(email);
    }
}

