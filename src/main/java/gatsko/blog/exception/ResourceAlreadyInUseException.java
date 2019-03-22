package gatsko.blog.exception;

public class ResourceAlreadyInUseException extends RuntimeException {
    public ResourceAlreadyInUseException(String message) {
        super(message);
    }
}