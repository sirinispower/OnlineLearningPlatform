package com.onlinelearning.dto;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private String type = "Bearer";
    private Long userId;
    private String username;
    private String nickname;
    private String avatar;
    private String role;
}
