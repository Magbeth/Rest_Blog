package gatsko.blog.controller;

import gatsko.blog.event.OnPasswordLinkRequestEvent;
import gatsko.blog.event.OnPasswordResetNotificationEvent;
import gatsko.blog.model.User;
import gatsko.blog.model.dto.PasswordResetLinkRequest;
import gatsko.blog.model.dto.PasswordResetRequest;
import gatsko.blog.service.apiInterface.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import javax.validation.Valid;

@RestController
public class UsersController {

    private final UserService userService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public UsersController(UserService userService,
                           ApplicationEventPublisher applicationEventPublisher) {
        this.userService = userService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostMapping("auth/forgotPassword")
    @ResponseStatus(value = HttpStatus.OK)
    public void resetPasswordRequestLink(@Valid @RequestBody PasswordResetLinkRequest passwordResetLinkRequest, WebRequest request) {
        String email = passwordResetLinkRequest.getEmail();
        OnPasswordLinkRequestEvent onPasswordLinkRequestEvent =
                new OnPasswordLinkRequestEvent(request.getContextPath(), email, request.getLocale());
        applicationEventPublisher.publishEvent(onPasswordLinkRequestEvent);
    }

    @PostMapping("auth/resetPassword")
    @ResponseStatus(value = HttpStatus.OK)
    public void resetPassword(@Valid @RequestBody PasswordResetRequest passwordResetRequest, WebRequest webRequest) {
        User changedUserData = userService.resetPassword(passwordResetRequest);
        OnPasswordResetNotificationEvent onPasswordResetNotificationEvent = new OnPasswordResetNotificationEvent(changedUserData.getEmail(), webRequest.getLocale());
        applicationEventPublisher.publishEvent(onPasswordResetNotificationEvent);
    }

    //TODO: ADD logOut
}
