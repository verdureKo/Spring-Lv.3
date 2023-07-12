package com.sparta.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class UserRequestDto {
    @Setter
    @Getter
    public static class LoginRequestDto {
        private String username;
        private String password;
    }

    @Getter
    @Setter
    public static class SignupRequestDto {
        @Size(min = 4, max = 10)
        @Pattern(regexp = "^[a-z0-9]*$")
        @NotBlank
        private String username;

        @Size(min = 8, max = 15)
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,15}")
        @NotBlank
        private String password;

        private boolean admin = false;
        private String adminToken = "";
    }

    @Getter
    @AllArgsConstructor
    public static class UserInfoDto {
        String username;
        boolean isAdmin;
    }
}
