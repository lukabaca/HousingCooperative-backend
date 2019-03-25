package pl.dmcs.blaszczyk.security;
import org.springframework.beans.factory.annotation.Autowired;
import pl.dmcs.blaszczyk.service.AuthService;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    @Autowired
    private AuthService authService;

    public JWTAuthorizationFilter(AuthenticationManager authManager) {
        super(authManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse ressponse,
                                    FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(JWTConfig.getHeaderString());
        if (header == null || !header.startsWith(JWTConfig.getTokenPrefix())) {
            chain.doFilter(request, ressponse);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, ressponse);
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(JWTConfig.getHeaderString());
        if (token != null) {
            String username = Jwts.parser()
                    .setSigningKey(JWTConfig.getSECRET())
                    .parseClaimsJws(token.replace(JWTConfig.getTokenPrefix(), ""))
                    .getBody()
                    .getSubject();
            if (username != null) {
                UserDetails appUser = authService.loadUserByUsername(username);
                return new UsernamePasswordAuthenticationToken(appUser, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}
