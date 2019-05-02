package pl.dmcs.blaszczyk.model.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ResourceForbiddenException extends RuntimeException {
    public ResourceForbiddenException() {

    }
    public ResourceForbiddenException(String message) {
        super(message);
    }
    public ResourceForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
