package ovh.kkazm.bugtracker.security;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.BearerTokenAuthenticationEntryPoint;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.boot.security.autoconfigure.web.servlet.PathRequest.toH2Console;
import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity // Should I use this in a spring boot app?
@EnableMethodSecurity // Should I use this in a spring boot app?
//@EnableGlobalMethodSecurity deprecated
@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class SecurityConfiguration {

    //    private final AuthenticationProvider authenticationProvider;
    private final JpaUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain
    securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
//                .formLogin(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable) // TODO CSRF
                .cors(withDefaults()) // TODO CORS
                .headers(headers -> { // TODO Headers
//                    headers.defaultsDisabled();
                    headers.frameOptions(withDefaults()).disable(); // TODO
//                    headers.xssProtection(Customizer.withDefaults());
//                    headers.contentSecurityPolicy(Customizer.withDefaults());
//                    contentTypeOptions.disable();
//                    cacheControl.disable();
//                    hsts.disable();
                })
                .authorizeHttpRequests(
                        authorize -> authorize

                                .requestMatchers(toH2Console())
                                .permitAll() // TODO

                                .requestMatchers(
                                        "/swagger-ui/**",
                                        "/v3/api-docs/**",
                                        "/actuator/**", // TODO
                                        "/error",
                                        "/signup",
                                        "/login"
                                )
                                .permitAll()

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/projects/**",
                                        "/users/**",
                                        "/issues/**"
                                )
                                .permitAll()

                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/projects/**",
                                        "/users/**",
                                        "/issues/**"
                                )
                                .authenticated()

                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/projects/**",
                                        "/users/**",
                                        "/issues/**"
                                )
                                .denyAll()

                                .anyRequest()
                                .denyAll()
                )

                .sessionManagement(
                        (SessionManagementConfigurer<HttpSecurity> session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .oauth2ResourceServer(
                        (OAuth2ResourceServerConfigurer<HttpSecurity> httpSecurityOAuth2ResourceServerConfigurer) ->
                                httpSecurityOAuth2ResourceServerConfigurer.jwt(Customizer.withDefaults())
                )

                .exceptionHandling(exceptions -> exceptions
                        // TODO
                        .authenticationEntryPoint(new BearerTokenAuthenticationEntryPoint())
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler())
                );

        return httpSecurity.build();
    }

    @Bean
    JwtDecoder
    jwtDecoder(JwtService jwtService) {
        return NimbusJwtDecoder.withPublicKey(jwtService.getPublicKey()).build();
    }

    @Bean
    JwtEncoder
    jwtEncoder(JwtService jwtService) {
        JWK jwk = new RSAKey.Builder(jwtService.getPublicKey()).privateKey(jwtService.getPrivateKey()).build();
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        return new NimbusJwtEncoder(jwks);
    }

    @Bean
    public AuthenticationManager
    authenticationManager(UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

/*
    @Bean
    public AuthenticationProvider authenticationProvider() {
        var daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        return daoAuthenticationProvider;
    }
*/

}
