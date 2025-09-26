package ovh.kkazm.bugtracker.security;

import org.springframework.beans.factory.InjectionPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration(proxyBeanMethods = false)
public class PasswordEncoderConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder(InjectionPoint injectionPoint) {
//        public PasswordEncoder passwordEncoder(InjectionPoint injectionPoint) {
        return new BCryptPasswordEncoder();
    }

}
