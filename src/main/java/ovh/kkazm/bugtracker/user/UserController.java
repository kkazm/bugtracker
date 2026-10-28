package ovh.kkazm.bugtracker.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ovh.kkazm.bugtracker.user.UserRepository.UserInfo;
import ovh.kkazm.bugtracker.user.dtos.LoginUserRequest;
import ovh.kkazm.bugtracker.user.dtos.SignUpUserRequest;

import java.util.Map;

/**
 * Endpoints related to the Users
 */
@RestController
@RequiredArgsConstructor
@CrossOrigin("*") // TODO CORS
//@Validated
public class UserController {

    private final UserService userService;

    /**
     * Create a user account and immediately return a JWT Bearer token.
     *
     * @return a JWT Bearer token
     */
    @PostMapping("/signup")
//    @SecurityRequirements
    public ResponseEntity<Map<String, String>>
    signUpUser(@Valid @RequestBody final SignUpUserRequest request) {
        String jwt = userService.signUpUser(request);
        return ResponseEntity.ok(Map.of("token", jwt));
    }

    /**
     * Check for valid credentials and return a JWT Bearer token.
     *
     * @return a JWT Bearer token.
     */
    @PostMapping(value = "/login")
//    @SecurityRequirements
    public ResponseEntity<Map<String, String>>
    loginUser(@Valid @RequestBody final LoginUserRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().build();
        String jwt = userService.loginUser(request);
        return ResponseEntity.ok(Map.of("token", jwt));
    }

    @GetMapping("/users")
    public PagedModel<UserInfo>
    getAPageOfUsers(@PageableDefault(sort = "username") Pageable pageable) {
        if (pageable.getSort().stream().anyMatch(order -> !order.getProperty().equals("username"))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Users can only be sorted by username");
        }
        return userService.getAllUsers(pageable);
    }

}
