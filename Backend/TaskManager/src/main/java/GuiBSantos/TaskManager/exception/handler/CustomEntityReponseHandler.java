package GuiBSantos.TaskManager.exception.handler;

import GuiBSantos.TaskManager.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

@ControllerAdvice
@RestController
public class CustomEntityReponseHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(SecurityException.class)
    public final ResponseEntity<ExceptionResponse> handleSecurityException(Exception ex, WebRequest request) {
        return buildResponse(ex, request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public final ResponseEntity<ExceptionResponse> handleNotFoundException(Exception ex, WebRequest request) {
        return buildResponse(ex, request, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(RequiredObjectIsNullException.class)
    public final ResponseEntity<ExceptionResponse> handleRequiredObjectException(Exception ex, WebRequest request) {
        return buildResponse(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidJWTAuthenticationException.class)
    public final ResponseEntity<ExceptionResponse> handleInvalidJWTException(Exception ex, WebRequest request) {
        return buildResponse(ex, request, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ExceptionResponse> handleBadRequestException(Exception ex, WebRequest request) {
        return buildResponse(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception ex, WebRequest request) {
        return buildResponse(ex, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ExceptionResponse> buildResponse(Exception ex, WebRequest request, HttpStatus status) {
        ExceptionResponse response = new ExceptionResponse(
                new Date(),
                ex.getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(response, status);
    }
}
