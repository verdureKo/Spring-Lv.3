package com.sparta.blog.controller;

import com.sparta.blog.dto.ApiResult;
import com.sparta.blog.dto.LoginRequestDto;
import com.sparta.blog.dto.SignupRequestDto;
import com.sparta.blog.entity.User;
import com.sparta.blog.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // 회원가입 API
    @PostMapping("/signup")
    public ApiResult signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        User user = userService.signup(signupRequestDto);
        return new ApiResult("회원가입 성공", HttpStatus.OK.value());
    }


    // 로그인 API
    @ResponseBody
    @PostMapping("/login")
    public ApiResult login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String token = userService.login(loginRequestDto, response);
        return new ApiResult("로그인 성공", HttpStatus.OK.value());
    }
}
