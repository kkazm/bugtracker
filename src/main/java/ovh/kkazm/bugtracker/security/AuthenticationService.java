package ovh.kkazm.bugtracker.security;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ovh.kkazm.bugtracker.user.User;
import ovh.kkazm.bugtracker.user.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public String signUpUser(final SignUpUserRequest request) {
        final var userAccountExists = userRepository.existsByUsername(request.username());
        if (userAccountExists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username taken");
        }
        final User user = User.builder()
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .roles("ROLE_USER") // TODO Which role?
                .build();
        userRepository.save(user);
        return jwtService.generateToken(user);
    }

    @Transactional
    public String loginUser(final LoginUserRequest request) {
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.username(), request.password())
            );
            final User user = User.builder()
                    .username(request.username())
                    .roles("ROLE_USER")
                    .build();
            return jwtService.generateToken(user);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        }
    }

    @Builder
    // TODO Validation
    public record SignUpUserRequest(
            String firstName,
            String lastName,
            @NotNull
            @NotBlank
            String username,
            String password
    ) {
    }

    @Builder
    // TODO Validation
    public record LoginUserRequest(
            @NotNull
            @NotBlank
            String username,
            @NotNull
            @NotBlank
            String password
    ) {
    }

}
