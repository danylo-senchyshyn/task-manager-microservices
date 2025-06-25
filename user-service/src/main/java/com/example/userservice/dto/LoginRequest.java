package com.example.userservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class LoginRequest {
    private String username;
    private String password;
}
