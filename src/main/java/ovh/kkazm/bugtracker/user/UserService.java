package ovh.kkazm.bugtracker.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
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
import ovh.kkazm.bugtracker.user.dtos.LoginUserRequest;
import ovh.kkazm.bugtracker.user.dtos.SignUpUserRequest;

import java.util.List;

import static ovh.kkazm.bugtracker.user.UserRepository.UserInfo;

@Slf4j
@Service
//@Validated
@RequiredArgsConstructor
public class UserService {

    //    private final DataSource[] dataSource;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
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

    public PagedModel<UserInfo>
    getAllUsers(final Pageable pageable) {
        Page<UserInfo> page = userRepository.findBy(pageable);
        return new PagedModel<>(page);
    }

}
