package com.example.userservice.dto;

import lombok.*;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String username;
}