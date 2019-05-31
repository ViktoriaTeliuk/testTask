package rest.files.web.security;

import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import rest.files.model.User;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final SecretKey jwtSecret = Keys.secretKeyFor(SignatureAlgorithm.HS512);

    private final int jwtExpirationInMs = 30*60*1000;

    private Date expiredDate;

    public String generateToken(User user) {//Authentication authentication) {

       // AuthorizedUser userPrincipal = (AuthorizedUser) authentication.getPrincipal();

        Date now = new Date();
        expiredDate = new Date(now.getTime() + jwtExpirationInMs);

        return Jwts.builder()
                .setSubject(Long.toString(user.getId()))
                .setIssuedAt(new Date())
                .setExpiration(expiredDate)
                .signWith(jwtSecret, SignatureAlgorithm.HS512)
                .compact();
    }

    public Long getUserIdFromJWT(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .getBody();

        return Long.parseLong(claims.getSubject());
    }

    public boolean validateToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (MalformedJwtException ex) {
            logger.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            logger.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            logger.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            logger.error("JWT claims string is empty.");
        }
        return false;
    }

    public Date getExpiredDate() {
        return expiredDate;
    }
}