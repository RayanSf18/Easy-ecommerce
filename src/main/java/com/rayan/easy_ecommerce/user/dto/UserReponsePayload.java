package com.rayan.easy_ecommerce.user.dto;

import com.rayan.easy_ecommerce.user.User;

public record UserReponsePayload(
    Long user_id,
    String name,
    String email,
    String phone,
    String password
) {
    public static UserReponsePayload toResponse(User user) {
        return new UserReponsePayload(
            user.getId(),
            user.getName(),
            user.getEmail(),
            user.getPhone(),
            user.getPassword()
        );
    }
}
