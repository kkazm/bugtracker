package ovh.kkazm.bugtracker.security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    /**
     * TODO Filter only when required
     */
    @Override
    protected void
    doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
/*
        if (!this.requestMatcher.matches(request)) {
            logger.debug("Did not match request to " + this.requestMatcher);
            filterChain.doFilter(request, response);
            return;
        }
*/
        final var authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        final var jwt = authHeader.substring(7);
        Claims claims;
        try {
            claims = jwtService.verifyAndExtractAllClaims(jwt);
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

/*
        Date expiration = claims.getExpiration();
        if (expiration.before(new Date())) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
*/

        final var username = claims.getSubject();
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            final var authToken = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")));
            var webAuthenticationDetails = new WebAuthenticationDetailsSource().buildDetails(request);
            Map<String, Object> details
                    = Map.of("claims", claims, "webAuthenticationDetails", webAuthenticationDetails);
            authToken.setDetails(details);
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
        filterChain.doFilter(request, response);
        return;
    }

}
