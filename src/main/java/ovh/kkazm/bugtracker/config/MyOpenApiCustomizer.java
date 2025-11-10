package ovh.kkazm.bugtracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.customizers.OpenApiCustomizer;

@Slf4j
public class MyOpenApiCustomizer implements OpenApiCustomizer {
    @Override
    public void customise(OpenAPI openApi) {
        log.info("Customising OpenAPI API");
    }
}
