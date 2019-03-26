package gatsko.blog.controller;

import gatsko.blog.exception.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class WebRestControllerAdvice extends ResponseEntityExceptionHandler {

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

    //TODO: This handler finished with 500 eternal error then use ExceptionResponse in return.
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

    @ExceptionHandler(UserAlreadyActivatedException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ExceptionResponse handleUserAlreadyActivatedException(UserAlreadyActivatedException ex) {
        return new ExceptionResponse(ex.getMessage());
    }

    @ExceptionHandler(PasswordResetException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ExceptionResponse handlePasswordResetException(PasswordResetException ex) {
        return new ExceptionResponse(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ExceptionResponse handleRuntimeException(RuntimeException ex) {
        return new ExceptionResponse(ex.getMessage() + " ====FROM RUNTIME HANDLER==== " + ex.getClass());
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                HttpHeaders headers, HttpStatus status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for(ObjectError error : ex.getBindingResult().getAllErrors()) {
            details.add(error.getDefaultMessage());
        }
        ExceptionResponse response = new ExceptionResponse(details.toString());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}