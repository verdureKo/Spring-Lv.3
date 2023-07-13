package com.sparta.blog.controller;

import com.sparta.blog.dto.UserRequestDto;
import com.sparta.blog.result.ApiResponse;
import com.sparta.blog.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse> signup(@RequestBody @Valid UserRequestDto.SignupRequestDto requestDto, BindingResult bindingResult) {
        ApiResponse apiResponse = new ApiResponse();

        // 유효성 검사 결과에서 발생한 필드 에러들을 가져온다.
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            apiResponse.setStatusCode(404);
            apiResponse.setMessage("회원가입 실패!!");
            return new ResponseEntity<>(apiResponse, HttpStatus.NOT_FOUND);
        }

        return userService.signup(requestDto);
    }
}