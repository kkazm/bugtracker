package ovh.kkazm.bugtracker.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ovh.kkazm.bugtracker.user.UserService.UserInfo;
import ovh.kkazm.bugtracker.user.UserService.LoginUserRequest;
import ovh.kkazm.bugtracker.user.UserService.SignUpUserRequest;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*") // TODO CORS
//@Validated
public class UserController {

    private final UserService userService;

    /**
     * Create a user account and return a JWT token
     *
     * @return JWT token
     */
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>>
    signUpUser(@Valid @RequestBody final SignUpUserRequest request) {
        String jwt = userService.signUpUser(request);
        return ResponseEntity.ok(Map.of("token", jwt));
    }

    /**
     * Check for valid credentials and return the JWT token
     *
     * @return JWT token
     */
    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, String>>
    loginUser(@Valid @RequestBody final LoginUserRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().build();
        String jwt = userService.loginUser(request);
        return ResponseEntity.ok(Map.of("token", jwt));
    }

    @GetMapping("/users")
    public Page<UserInfo>
    getAllUsers(@SortDefault(sort = "username") Pageable pageable,
                @RequestParam(required = false) @Max(20) Integer size) {
        Pageable pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize()); // TODO Sort
        return userService.getAllUsers(pageRequest);
    }

}
