package ovh.kkazm.bugtracker.config;

import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class RestClientConfiguration {

/*
    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder
                .requestFactory(new HttpComponentsClientHttpRequestFactory())
                .build();
    }
*/

}
