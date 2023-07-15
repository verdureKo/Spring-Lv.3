package com.sparta.blog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.result.ApiResponse;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String tokenValue = jwtUtil.getJwtFromHeader(req);

        if (StringUtils.hasText(tokenValue)) {
            if (!jwtUtil.validateToken(tokenValue)) {
                jwtExceptionHandler(res, "토큰이 유효하지 않습니다.", HttpStatus.BAD_REQUEST.value());
                return;
            }

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                res.setContentType("application/json");
                req.setCharacterEncoding("UTF-8");
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    public void setAuthentication(String username) {
        Authentication authentication = createAuthentication(username);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    protected void jwtExceptionHandler(HttpServletResponse response, String message, int statusCode) {
        log.info("작성자만 삭제/수정할 수 있습니다.");
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ApiResponse apiResponse = new ApiResponse(400, "작성자만 삭제/수정할 수 있습니다.");
        try {
            String json = new ObjectMapper().writeValueAsString(apiResponse);
            response.getWriter().write(json);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
