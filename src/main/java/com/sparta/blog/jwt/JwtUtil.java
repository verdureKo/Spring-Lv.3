package com.sparta.blog.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
@RequiredArgsConstructor
@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {
        public static final String AUTHORIZATION_HEADER = "Authorization";  // Header Authorization KEY 값(Cookie의 name값)
        public static final String BEARER_PREFIX = "Bearer "; //Token 식별자,  토큰을 만들 때 앞에 붙음 (규칙같은것)
        private final long TOKEN_TIME = 60 * 60 * 1000L;  // 토큰 만료시간(60분) 정해져있지않음

        @Value("${jwt.secret.key}") // Application.properties에 넣어놓은 값 Base64 Encode한 SecretKey
        private String secretKey;
        private Key key; // Token 만들 때 넣어줄 Key 값
        private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256; // 암호화 복호화앙대는 알고리즘 HS256

        @PostConstruct // 처음 객체 생성시 초기화 함수
        public void init() {
                byte[] bytes = Base64.getDecoder().decode(secretKey); // Base64는 인코딩한 값이기때문에 디코드 한 후 사용
                key = Keys.hmacShaKeyFor(bytes);
        }

        // header 토큰 가져오기
        public String getJwtFromHeader(HttpServletRequest request) { // HttpServletRequset 안에는 우리가 가져와야 할 토큰이 헤더에 들어있음
                String bearerToken = request.getHeader(AUTHORIZATION_HEADER); // 파라미터로 가져올 값을 넣어주면 됨
                if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) { // 코드가 있는지, BEARER로 시작하는지 확인
                        return bearerToken.substring(7); // "BEARER " -> 공백포함 앞 7글자 자름
                }
                return null;
        }

        // 토큰 생성
        public String createToken(String username) {
                Date date = new Date();

                return BEARER_PREFIX +
                        Jwts.builder()
                                .setSubject(username) // 사용자 식별자값(ID)
                                .setExpiration(new Date(date.getTime() + TOKEN_TIME))  // 토큰 만료시간 설정: getTime()으로 현재 시간 기준함, 토큰타임 시간동안 유효함
                                .setIssuedAt(date) // 발급일
                                .signWith(key, signatureAlgorithm) // 어떤 알고리즘을 사용하여 암호화 할 것인가
                                .compact(); // String 형식의 JWT 토큰으로 반환 됨
        }

        // 토큰 검증
        public boolean validateToken(String token) {
                try {
                        Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
                        return true;
                } catch (SecurityException | MalformedJwtException | SignatureException e) {
                        log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
                } catch (ExpiredJwtException e) {
                        log.error("Expired JWT token, 만료된 JWT token 입니다.");
                } catch (UnsupportedJwtException e) {
                        log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
                } catch (IllegalArgumentException e) {
                        log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
                }
                return false;
        }

        // 토큰에서 사용자 정보 가져오기
        public Claims getUserInfoFromToken(String token) {
                return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody(); // 마지막에 getBody를 통하여 그 안에 들어있는 정보를 가져옴
        }
}