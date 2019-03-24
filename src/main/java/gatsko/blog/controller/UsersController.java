package gatsko.blog.controller;

import gatsko.blog.event.OnPasswordLinkRequestEvent;
import gatsko.blog.model.dto.PasswordResetLinkRequest;
import gatsko.blog.model.dto.PasswordResetRequest;
import gatsko.blog.service.ApiInterface.UserService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

@RestController
public class UsersController {

    private final UserService userService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public UsersController(UserService userService,
                           ApplicationEventPublisher applicationEventPublisher) {
        this.userService = userService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

//    @RequestMapping(path = "/users", method = RequestMethod.GET)
//    public User getUsersByFirstName(@RequestParam(name = "username") String username) {
//        User user = userService.findByUsername(username);
//        return user;
//    }

    @PostMapping("auth/forgotPassword")
    @ResponseStatus(value = HttpStatus.OK)
    public void resetPasswordLink(@RequestBody PasswordResetLinkRequest passwordResetLinkRequest, WebRequest request) {
        String email = passwordResetLinkRequest.getEmail();
        OnPasswordLinkRequestEvent onPasswordLinkRequestEvent =
                new OnPasswordLinkRequestEvent(request.getContextPath(), email, request.getLocale());
        applicationEventPublisher.publishEvent(onPasswordLinkRequestEvent);
    }

    //TODO: Add email notification here
    @PostMapping("auth/resetPassword")
    @ResponseStatus(value = HttpStatus.OK)
    public void resetPassword(@RequestBody PasswordResetRequest passwordResetRequest, WebRequest webRequest) {
        userService.resetPassword(passwordResetRequest);
    }

    //TODO: ADD logOut
}
