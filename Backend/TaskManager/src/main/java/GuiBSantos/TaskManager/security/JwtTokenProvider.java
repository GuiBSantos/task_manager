package GuiBSantos.TaskManager.security;

import GuiBSantos.TaskManager.dto.TokenDTO;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Service
public class JwtTokenProvider {

    @Value("${security.jwt.token.secret-key:secret}")
    private String secretKey = "secret";

    @Value("${security.jwt.token.expire-length:3600000}")
    private long validityInMilliseconds = 3600000; // 1h

    private UserDetailsService userDetailsService;

    private Algorithm algorithm;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        algorithm = Algorithm.HMAC256(secretKey.getBytes());
    }

    public void setUserDetailsService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public TokenDTO createAccessToken(String username, List<String> roles) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        String accessToken = generateAccessToken(username, roles, now, validity);
        String refreshToken = generateRefreshToken(username, roles, now);

        return new TokenDTO(username, true, now, validity, accessToken, refreshToken);
    }

    public TokenDTO refreshToken(String refreshToken) {
        String token = resolveBearerToken(refreshToken);

        DecodedJWT decodedJWT = verifyToken(token);
        String username = decodedJWT.getSubject();
        List<String> roles = decodedJWT.getClaim("roles").asList(String.class);

        return createAccessToken(username, roles);
    }

    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = verifyToken(token);
        UserDetails userDetails = this.userDetailsService.loadUserByUsername(decodedJWT.getSubject());
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        return hasBearerPrefix(bearerToken) ? bearerToken.substring(7) : null;
    }

    public boolean validateToken(String token) {
        DecodedJWT decodedJWT = verifyToken(token);
        Date expiration = decodedJWT.getExpiresAt();
        return expiration != null && expiration.after(new Date());
    }

    private String generateAccessToken(String username, List<String> roles, Date now, Date validity) {
        String issuerUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        return JWT.create()
                .withSubject(username)
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .withIssuer(issuerUrl)
                .sign(algorithm);
    }

    private String generateRefreshToken(String username, List<String> roles, Date now) {
        Date refreshValidity = new Date(now.getTime() + validityInMilliseconds * 3);
        return JWT.create()
                .withSubject(username)
                .withClaim("roles", roles)
                .withIssuedAt(now)
                .withExpiresAt(refreshValidity)
                .sign(algorithm);
    }

    private DecodedJWT verifyToken(String token) {
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    private String resolveBearerToken(String token) {
        return hasBearerPrefix(token) ? token.substring(7) : token;
    }

    private boolean hasBearerPrefix(String token) {
        return StringUtils.isNotBlank(token) && token.startsWith("Bearer ");
    }
}
