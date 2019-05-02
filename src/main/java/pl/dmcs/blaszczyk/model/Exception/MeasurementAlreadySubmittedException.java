package pl.dmcs.blaszczyk.model.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class MeasurementAlreadySubmittedException extends RuntimeException {
    public MeasurementAlreadySubmittedException() {
    }
    public MeasurementAlreadySubmittedException(String message) {
        super(message);
    }
}
