package ovh.kkazm.bugtracker.hello;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping
@CrossOrigin("*")
public class HelloController {

    @GetMapping("/hello")
    public ResponseEntity<Map<String, String>> hello(Locale locale) {
        return ResponseEntity.ok(Map.of("message", "hello from secured endpoint"));
    }

}
