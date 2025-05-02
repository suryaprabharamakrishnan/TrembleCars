package com.trimbleCars.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequest {
    private String name;
    private String email;
    private String phoneNumber;
    private String password;
    private String role;
}
