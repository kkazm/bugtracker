package ovh.kkazm.bugtracker.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    //    private final AuthenticationProvider authenticationProvider;
    private final JpaUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain
    securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable) // TODO CSRF
                .cors(withDefaults()) // TODO CORS
                .headers(headers -> { // TODO Headers
//                    headers.defaultsDisabled();
                    headers.frameOptions(withDefaults()).disable();
//                    headers.xssProtection(Customizer.withDefaults());
//                    headers.contentSecurityPolicy(Customizer.withDefaults());
//                    contentTypeOptions.disable();
//                    cacheControl.disable();
//                    hsts.disable();
                })
                .authorizeHttpRequests(
                        authorize -> authorize
                                .requestMatchers(toH2Console())
                                .permitAll()

                                .requestMatchers(
                                        "/swagger-ui**/**",
                                        "/v3/api-docs**/**",
                                        "/actuator/**",
                                        "/error",
                                        "/signup",
                                        "/login"
                                )
                                .permitAll()

                                .requestMatchers(
                                        HttpMethod.GET,
                                        "/projects**/**",
                                        "/users**/**",
                                        "/issues**/**"
                                )
                                .permitAll()

                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/projects**/**",
                                        "/users**/**",
                                        "/issues**/**"
                                )
                                .authenticated()

                                .requestMatchers(
                                        HttpMethod.PATCH,
                                        "/projects**/**",
                                        "/users**/**",
                                        "/issues**/**"
                                )
                                .authenticated()

                                .requestMatchers(
                                        HttpMethod.DELETE,
                                        "/projects**/**",
                                        "/users**/**",
                                        "/issues**/**"
                                )
                                .denyAll()

                                .anyRequest()
                                .denyAll()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authenticationProvider(authenticationProvider) // TODO Which AuthenticationProvider?
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
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

    /**
     * Expose AuthenticationManger bean
     */
    @Bean
    public AuthenticationManager
    authenticationManager(UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(authenticationProvider);
    }

}
