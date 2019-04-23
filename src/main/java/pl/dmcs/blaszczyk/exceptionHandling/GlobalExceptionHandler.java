package pl.dmcs.blaszczyk.exceptionHandling;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.util.WebUtils;
import pl.dmcs.blaszczyk.model.Exception.*;
import pl.dmcs.blaszczyk.model.Utils.ApiError;

import javax.validation.UnexpectedTypeException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ ResourceNotFoundException.class, BadRequestException.class, ActivationTokenExpiredException.class, ServerException.class, UserAlreadyExistException.class,
    WrongMeasurementDateException.class})
    public final ResponseEntity<?> handleException(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        ApiError apiError = new ApiError(ex.getMessage());
        HttpStatus status = getHttpStatusByExceptionType(ex);
        return handleExceptionInternal(ex, headers, status, request, apiError);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, HttpMessageNotReadableException.class, IllegalArgumentException.class, UnexpectedTypeException.class})
    public ResponseEntity handleBindingErrors(Exception ex, WebRequest request) {
        HttpHeaders headers = new HttpHeaders();
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String msg = ex.getMessage();
        ApiError apiError = new ApiError(msg);
        return handleExceptionInternal(ex, headers, status, request, apiError);
    }

    protected ResponseEntity<?> handleExceptionInternal(Exception ex, HttpHeaders headers, HttpStatus status, WebRequest request, ApiError body) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
        }
        return new ResponseEntity<>(body, headers, status);
    }

    private HttpStatus getHttpStatusByExceptionType(Exception ex) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (ex instanceof ResourceNotFoundException) {
            status = HttpStatus.NOT_FOUND;
        } else if (ex instanceof BadRequestException) {
            status = HttpStatus.NOT_FOUND;
        } else if (ex instanceof ActivationTokenExpiredException) {
            status = HttpStatus.CONFLICT;
        } else if (ex instanceof ServerException) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        } else if (ex instanceof UserAlreadyExistException) {
            status = HttpStatus.CONFLICT;
        } else if (ex instanceof WrongMeasurementDateException) {
            status = HttpStatus.BAD_REQUEST;
        } else {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return status;
    }
}
