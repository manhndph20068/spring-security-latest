package com.javamanh.dto;

import com.javamanh.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JwtResponse {

    private String accessToken;
    private String refreshToken;
    private Account userInfo;
    private String message;
}
