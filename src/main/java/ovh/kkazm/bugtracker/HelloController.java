package ovh.kkazm.bugtracker;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloController {

    @GetMapping("/hello")
    public ResponseEntity<String> getString() {
        return ResponseEntity.ok("Hello world!");
    }

}
