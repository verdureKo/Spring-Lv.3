package com.sparta.blog.service;

import com.sparta.blog.dto.UserRequestDto;
import com.sparta.blog.entity.User;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.repository.UserRepository;
import com.sparta.blog.result.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private static final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public ResponseEntity<ApiResponse> signup(UserRequestDto.SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        // 회원 중복 확인
        if (userRepository.existsByUsername(username)) {
            ApiResponse apiResponse = new ApiResponse(400, "중복된 username 입니다.");
            return ResponseEntity.badRequest().body(apiResponse);
        }

        // Token 확인
        UserRoleEnum role = UserRoleEnum.USER;
        if (requestDto.isAdmin()) {
            if (!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                ApiResponse apiResponse = new ApiResponse(400, "토큰이 유효하지 않습니다.");
                return ResponseEntity.badRequest().body(apiResponse);
            }
            role = UserRoleEnum.ADMIN;
        }

        // 회원 저장
        User user = new User(username, password, role);
        userRepository.save(user);
        ApiResponse apiResponse = new ApiResponse(200, "회원가입 성공!!");
        return ResponseEntity.ok(apiResponse);
    }
}
