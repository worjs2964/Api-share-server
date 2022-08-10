package link.projectjg.apiserver.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import link.projectjg.apiserver.exception.CustomException;
import link.projectjg.apiserver.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;

@Component
@RequiredArgsConstructor
public class JwtTokenUtil {

    private final ObjectMapper objectMapper;

    @Value("${spring.jwt.secret}")
    private String SECRET_KEY;

    @Value("${spring.jwt.accessTokenExpiration}")
    private Long ACCESS_TOKEN_EXPIRATION_TIME;

    @Value("${spring.jwt.refreshTokenExpiration}")
    private Long REFRESH_TOKEN_EXPIRATION_TIME;

    // 토큰에서 Claim 가져옴
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // secretKey 암호화
    private Key getSigningKey(String secretKey) {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰에서 MemberUid 가져옴
    public String getMemberUid(String token) {
        return extractAllClaims(token).get("memberUid", String.class);
    }

    // 토큰 만료 확인
    public Boolean isTokenExpired(String token) {
        try {
            extractAllClaims(token).getExpiration();
        } catch (ExpiredJwtException e) {
            return true;
        } catch (JwtException e) {
            throw new CustomException(ErrorCode.INVALID_AUTH_TOKEN);
        }
        return false;
    }

    // accessToken 생성
    public String generateAccessToken(String memberUid) {
        return doGenerateToken(memberUid, ACCESS_TOKEN_EXPIRATION_TIME);
    }

    // refreshToken 생성
    public String generateRefreshToken() {
        return doGenerateToken(null, REFRESH_TOKEN_EXPIRATION_TIME);
    }

    // token 생성
    private String doGenerateToken(String memberUid, long expireTime) {
        Claims claims = Jwts.claims();
        if (memberUid != null) {
            claims.put("memberUid", memberUid);
        }

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expireTime))
                .signWith(getSigningKey(SECRET_KEY), SignatureAlgorithm.HS256)
                .compact();
    }


    // 만료되도 확인이 가능한 메소드
    public String getPayloadInfo(String token, String info) {
        String[] tokens = token.split("\\.");
        String payload = tokens[1];
        byte[] decode = Base64.getDecoder().decode(payload.getBytes(StandardCharsets.UTF_8));
        try {
            HashMap<String, Object> hashMap = objectMapper.readValue(new String(decode, StandardCharsets.UTF_8), HashMap.class);
            return hashMap.get(info).toString();
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.INVALID_AUTH_TOKEN);
        }
    }

    public long getRemainMilliSeconds(String token) {
        try {
            return extractAllClaims(token).getExpiration().getTime() - Instant.now().toEpochMilli();
        } catch (ExpiredJwtException e) {
            return 0L;
        }
    }
}
