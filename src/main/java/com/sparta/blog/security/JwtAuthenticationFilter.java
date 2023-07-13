package com.sparta.blog.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.blog.dto.UserRequestDto;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.jwt.JwtUtil;
import com.sparta.blog.result.ApiResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserRequestDto.LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), UserRequestDto.LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) {
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String token = jwtUtil.createToken(username, role);
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
        log.info("로그인 성공!!");
        response.setStatus(200);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8"); // UTF-8 설정 추가
        try {
            String json = new ObjectMapper().writeValueAsString(new ApiResponse(200, "로그인 성공!!"));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        log.info("로그인 실패!!");
        response.setStatus(400);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8"); // UTF-8 설정 추가
        try {
            String json = new ObjectMapper().writeValueAsString(new ApiResponse(400, "회원을 찾을 수 없습니다."));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}