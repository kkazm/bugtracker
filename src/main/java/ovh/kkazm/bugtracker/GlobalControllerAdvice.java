package ovh.kkazm.bugtracker;

import org.slf4j.MDC;
import org.springframework.boot.webmvc.autoconfigure.WebMvcAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ovh.kkazm.bugtracker.exceptions.ResourceNotFoundException;

import java.time.Instant;

/**
 * TODO {@link WebMvcAutoConfiguration.ProblemDetailsErrorHandlingConfiguration#problemDetailsExceptionHandler() ProblemDetailsErrorHandlingConfiguration}
 */
@RestControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {

    // TODO
    @ExceptionHandler(ResourceNotFoundException.class)
    ProblemDetail handle(ResourceNotFoundException ex) {
        ProblemDetail pd = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        pd.setTitle("Resource not found");
//        pd.setType(URI.create("https://api.example.com/problems/resource-not-found"));
        pd.setProperty("errorCode", "RESOURCE_NOT_FOUND");
        pd.setProperty("timestamp", Instant.now());
        pd.setProperty("traceId", MDC.get("traceId"));
        return pd;
    }

}
