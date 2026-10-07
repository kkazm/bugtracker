package ovh.kkazm.bugtracker.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ovh.kkazm.bugtracker.security.JwtService;

import java.util.List;

import static ovh.kkazm.bugtracker.user.UserRepository.UserInfo;

@Slf4j
@Service
//@Validated
@RequiredArgsConstructor
public class UserService {

    //    private final DataSource[] dataSource;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Transactional
    public String signUpUser(final SignUpUserRequest request) {
        String username = request.username().toLowerCase();
        final var userAccountExists = userRepository.existsByUsername(username);
        if (userAccountExists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username taken");
        }
        final var user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(request.password())); // TODO
        var roles = new SimpleGrantedAuthority("ROLE_USER");
        user.setRoles(List.of(roles)); // TODO What roles should a User have?
        userRepository.save(user);
        return jwtService.generateToken(user);
    }

    public String loginUser(final LoginUserRequest request) {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
            final var user = new User();
            user.setUsername(request.username());
            user.setPassword(passwordEncoder.encode(request.password())); // TODO
            var roles = new SimpleGrantedAuthority("ROLE_USER");
            user.setRoles(List.of(roles));
            return jwtService.generateToken(user);
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials"); // TODO
        }
    }

    @Transactional
    public Page<UserInfo> getAllUsers(final Pageable pageable) {
        return userRepository.findBy(pageable);
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
