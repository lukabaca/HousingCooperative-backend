package pl.dmcs.blaszczyk.model.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ActivationTokenExpiredException extends RuntimeException {
    public ActivationTokenExpiredException() {
    }
    public ActivationTokenExpiredException(String message) {
        super(message);
    }
    public ActivationTokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
