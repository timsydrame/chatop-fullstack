package com.chatop.backend.dto;

import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String created_at;
    private String updated_at;
}
