package ovh.kkazm.bugtracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OpenApiLocaleCustomizer;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Slf4j
@Component
public class MyOpenApiLocaleCustomizer implements OpenApiLocaleCustomizer {

    @Override
    public void customise(OpenAPI openApi, Locale locale) {
        log.info("Customizing OpenAPI for locale {}", locale);
    }

}
