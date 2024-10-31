package org.scu301.remoteserver.security;

import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

public class JwtUtils {
    private static final long EXP_HOURS = 2L;
    private static final SecretKey secretKey =  Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private JwtUtils() {}

    public static String createToken(Claims claims) {
        long now = System.currentTimeMillis();
        Date expiryDate = new Date(now + EXP_HOURS * 3600 * 1000); // 设置过期时间
        return Jwts.builder()
                .id(claims.id().toString())
                .subject(claims.sub())
                .issuedAt(new Date(now))
                .expiration(expiryDate)
                .claim("role", claims.role())
                .signWith(secretKey)
                .compact();
    }

    public static Claims parseToken(String token) throws JwtException {
        Jws<io.jsonwebtoken.Claims> claims = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
        io.jsonwebtoken.Claims parsedClaims = claims.getPayload();
        return new Claims(
                Integer.parseInt(parsedClaims.getId()),
                parsedClaims.getSubject(),
                parsedClaims.get("role", String.class),
                parsedClaims.getExpiration().getTime());
    }
}
