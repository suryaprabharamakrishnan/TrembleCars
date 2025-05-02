package com.trimbleCars.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String token;

    private Long userId;

    private String userName;

    private String name;

    private String roles;

    private String refreshToken;

}
