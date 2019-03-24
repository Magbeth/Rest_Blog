package gatsko.blog.controller;

import gatsko.blog.exception.ExceptionResponse;
import gatsko.blog.exception.InvalidTokenRequestException;
import gatsko.blog.exception.ResourceAlreadyInUseException;
import gatsko.blog.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.security.AccessControlException;

@RestControllerAdvice
public class WebRestControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ExceptionResponse handleResourceNotFoundException(ResourceNotFoundException ex) {
        return new ExceptionResponse(ex.getMessage());
    }

    @ExceptionHandler(ResourceAlreadyInUseException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ExceptionResponse handleUserRegistrationException(ResourceAlreadyInUseException ex) {
        return new ExceptionResponse(ex.getMessage());
    }

    @ExceptionHandler(InvalidTokenRequestException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public void handleTokenRequestException(InvalidTokenRequestException ex) {
        ExceptionResponse response = new ExceptionResponse(ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ExceptionResponse handleIllegalArgumentException(Exception ex) {
        return new ExceptionResponse(ex.getCause().getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.EXPECTATION_FAILED)
    public ExceptionResponse handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return new ExceptionResponse(ex.getMessage());
    }

    @ExceptionHandler(AccessControlException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse handleAccessControlException(AccessControlException ex) {
        return new ExceptionResponse(ex.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ExceptionResponse handleAccessDeniedException(AccessDeniedException ex) {
        return new ExceptionResponse(ex.getMessage());
    }

    @ExceptionHandler(DisabledException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionResponse handleDisabledException(DisabledException ex) {
        return new ExceptionResponse(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ExceptionResponse handleRuntimeException(RuntimeException ex) {
        return new ExceptionResponse(ex.getMessage() + " ====FROM RUNTIME HANDLER==== " + ex.getClass());
    }

}