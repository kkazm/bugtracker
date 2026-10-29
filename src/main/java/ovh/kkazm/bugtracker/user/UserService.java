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
import ovh.kkazm.bugtracker.exceptions.ResourceNotFoundException;
import ovh.kkazm.bugtracker.security.JwtService;
import ovh.kkazm.bugtracker.user.dtos.*;

import java.util.List;

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
    private final UserMapper userMapper;

    @Transactional
    public SignUpUserResponse signUpUser(final SignUpUserRequest request) {
        String username = request.username().toLowerCase();
        final var userAccountExists = userRepository.existsByUsername(username);
        if (userAccountExists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username taken");
        }
        var user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(request.password()));
        var roles = new SimpleGrantedAuthority("ROLE_USER");
        user.setRoles(List.of(roles));
        User savedUser = userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new SignUpUserResponse(token, savedUser.getId());
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

    public UserDto get(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        return userMapper.toUserDto(user);
    }

    public void delete(Long id) {
        userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        userRepository.deleteById(id);
    }

}
