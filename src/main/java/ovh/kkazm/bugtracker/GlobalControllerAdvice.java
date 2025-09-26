package ovh.kkazm.bugtracker;

import org.springframework.boot.webmvc.autoconfigure.WebMvcAutoConfiguration;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * TODO {@link WebMvcAutoConfiguration.ProblemDetailsErrorHandlingConfiguration#problemDetailsExceptionHandler() ProblemDetailsErrorHandlingConfiguration}
 */
@RestControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {
}
