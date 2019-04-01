package pl.dmcs.blaszczyk.security;
import io.jsonwebtoken.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import pl.dmcs.blaszczyk.model.Entity.AppUser;

import java.util.Date;

@Component
public class JWTTokenProvider {

    public String generateToken(Authentication authentication) {
        AppUser user = (AppUser) authentication.getPrincipal();
        Date expiryDate = new Date(System.currentTimeMillis() + JWTConfig.getExpirationTime());
        return Jwts.builder()
                .setSubject(Long.toString(user.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWTConfig.getSECRET())
                .compact();
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(JWTConfig.getSECRET())
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(JWTConfig.getSECRET()).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException ex) {
        } catch (MalformedJwtException ex) {
        } catch (ExpiredJwtException ex) {
        } catch (UnsupportedJwtException ex) {
        } catch (IllegalArgumentException ex) {
        }
        return false;
    }
}
