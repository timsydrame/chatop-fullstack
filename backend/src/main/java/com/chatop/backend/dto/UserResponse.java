package com.chatop.backend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    @JsonProperty("create_at")
    private String createdAt;
    @JsonProperty("update_at")
    private String updatedAt;
}
