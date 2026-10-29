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
import ovh.kkazm.bugtracker.user.dtos.*;

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
    public ResponseEntity<SignUpUserResponse>
    signUpUser(@Valid @RequestBody final SignUpUserRequest request) {
        SignUpUserResponse result = userService.signUpUser(request);
        return ResponseEntity.ok(result);
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

    /**
     * Get a single User searching by ID.
     *
     * @param id The ID of the User to find.
     * @return A User
     */
    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.get(id));
    }

    /**
     * Delete a User by ID.
     *
     * @param id The id of the User to delete
     * @return HTTP 204 on successful delete
     */
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Object> deleteProjectById(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
