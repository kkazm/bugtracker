package ovh.kkazm.bugtracker.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    public static final String BUGTRACKER_AUTHENTICATION_OPENAPI_SECURITY_SCHEMA = "bugtracker_authentication";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Bugtracker API")
                        .description("API for the bugtracker")
                        .version("v1.0.0"))
                .schemaRequirement(BUGTRACKER_AUTHENTICATION_OPENAPI_SECURITY_SCHEMA, new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("You can obtain a JWT Bearer token from /signup or /login endpoints."))
                .addSecurityItem(new SecurityRequirement()
                        .addList(BUGTRACKER_AUTHENTICATION_OPENAPI_SECURITY_SCHEMA))
                .externalDocs(new ExternalDocumentation()
                        .description("Bugtracker source repository")
                        .url("https://github.com/kkazm/bugtracker"));
    }

}
