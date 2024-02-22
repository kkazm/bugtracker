package ovh.kkazm.bugtracker.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ovh.kkazm.bugtracker.user.User;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    // TODO Use asymmetric key
    private final SecretKey key = Jwts.SIG.HS256.key().build();
//    private final String secretString = Encoders.BASE64.encode(key.getEncoded());
//    KeyPair keyPair = Jwts.SIG.RS256.keyPair().build();

    public String generateToken(User user) {
        return Jwts
                .builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(key)
                .compact();
    }

    public Claims verifyAndExtractAllClaims(String jwt) {
        try {
            return Jwts
                    .parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(jwt)
                    .getPayload();
        } catch (JwtException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

}
