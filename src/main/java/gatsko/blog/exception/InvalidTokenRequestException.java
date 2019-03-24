package gatsko.blog.exception;

public class InvalidTokenRequestException extends RuntimeException {

//    private String tokenType;
//    private String token;
//    private String message;

    public InvalidTokenRequestException(String message) {
        super(message);
//        this.tokenType = tokenType;
//        this.token = token;
//        this.message = message;
    }
}
