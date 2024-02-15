package ovh.kkazm.bugtracker.security;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ovh.kkazm.bugtracker.security.AuthenticationService.LoginUserRequest;
import ovh.kkazm.bugtracker.security.AuthenticationService.SignUpUserRequest;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*") // TODO
//@Validated
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    /**
     * Create a user account and return the generated JWT token
     * @return JWT token
     */
    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>>
    signUpUser(@Valid @RequestBody final SignUpUserRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().build();
        String jwt = authenticationService.signUpUser(request);
        return ResponseEntity.ok(Map.of("token", jwt));
    }

    /**
     * Check for valid credentials and return the JWT token
     * @return JWT token
     */
    @PostMapping(value = "/login")
    public ResponseEntity<Map<String, String>>
    loginUser(@Valid @RequestBody final LoginUserRequest request, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) return ResponseEntity.badRequest().build();
        String jwt = authenticationService.loginUser(request);
        return ResponseEntity.ok(Map.of("token", jwt));
    }

}
