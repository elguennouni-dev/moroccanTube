package com.moroccantube.app.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm; // Correct import for algorithm
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders; // For Base64 decoding
import io.jsonwebtoken.security.Keys; // For secure key creation
import io.jsonwebtoken.security.SignatureException; // More specific for JWT signature issues

import jakarta.annotation.PostConstruct; // Import for @PostConstruct
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey; // Use SecretKey interface for type safety
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secretString; // Rename to avoid confusion with the actual key object

    @Value("${jwt.expiration.access-token-ms}")
    private long accessTokenExpirationMs; // Renamed for consistency

    @Value("${jwt.expiration.refresh-token-ms:0}")
    private long refreshTokenExpirationMs; // Renamed for consistency

    private SecretKey signingKey; // Declare the SecretKey for JWT operations

    /**
     * Initializes the SecretKey from the Base64 encoded string after properties are injected.
     * This is crucial for securely handling JWT secrets and preventing WeakKeyException.
     */
    @PostConstruct
    public void init() {
        // Decode the Base64 secret string into a byte array
        byte[] keyBytes = Decoders.BASE64.decode(secretString);
        // Create a SecretKey using Keys.hmacShaKeyFor which ensures cryptographic strength
        // for the specified algorithm (HS512 needs at least 64 bytes or 512 bits).
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), accessTokenExpirationMs);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, userDetails.getUsername(), refreshTokenExpirationMs);
    }

    private String createToken(Map<String, Object> claims, String subject, long expirationMs) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(signingKey, SignatureAlgorithm.HS512) // Use the pre-initialized SecretKey and specify HS512
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Validates if a token belongs to a specific user and is not expired.
     * Note: This method is functionally identical to `isTokenValid`. Consider consolidating.
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        try {
            // Jwts.parserBuilder() is the modern way to build the parser
            return extractExpiration(token).before(new Date());
        } catch (ExpiredJwtException ex) {
            // Token is expired, this is expected for expired tokens
            return true;
        } catch (MalformedJwtException | SignatureException | UnsupportedJwtException | IllegalArgumentException ex) {
            // Log these exceptions if you want more details, but return true as the token is invalid
            // Logger.warn("Invalid JWT: {}", ex.getMessage());
            return true;
        }
    }

    private Claims extractAllClaims(String token) {
        // Jwts.parserBuilder() is the modern way to build the parser in JJWT 0.11.x and later
        return Jwts.parserBuilder()
                .setSigningKey(signingKey) // Use the SecretKey directly
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * This method is functionally identical to validateToken.
     * It's recommended to choose one name and use it consistently.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}