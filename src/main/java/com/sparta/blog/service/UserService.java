package com.sparta.blog.service;

import com.sparta.blog.dto.UserRequestDto;
import com.sparta.blog.entity.User;
import com.sparta.blog.entity.UserRoleEnum;
import com.sparta.blog.repository.UserRepository;
import com.sparta.blog.result.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // ADMIN_TOKEN
    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    public ResponseEntity<ApiResponse> signup(UserRequestDto.SignupRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = passwordEncoder.encode(requestDto.getPassword());

        ApiResponse apiResponse = new ApiResponse();

        // 회원 중복 확인
        Optional<User> checkUsername = userRepository.findByUsername(username);
        if (checkUsername.isPresent()) {
            apiResponse.setStatusCode(400);
            apiResponse.setMessage("중복된 username 입니다.");
            return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
        }

        UserRoleEnum role = UserRoleEnum.USER;
        if(requestDto.isAdmin()){
            if(!ADMIN_TOKEN.equals(requestDto.getAdminToken())) {
                apiResponse.setStatusCode(400);
                apiResponse.setMessage("관리자 암호를 다시 입력해주세요.");
                return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
            }
            role = UserRoleEnum.ADMIN;
        }

        // 사용자 등록
        User user = new User(username, password, role);
        userRepository.save(user);
        apiResponse.setMessage("회원가입 성공!!");

        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
}