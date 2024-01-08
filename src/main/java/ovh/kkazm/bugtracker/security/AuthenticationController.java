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
@CrossOrigin("*")
//@Validated
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<Map<String, String>> signUpUser(
            @Valid @RequestBody final SignUpUserRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(Map.of("token", authenticationService.signUpUser(request)));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(
            @Valid @RequestBody final LoginUserRequest request,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(Map.of("token", authenticationService.loginUser(request)));
    }

}
