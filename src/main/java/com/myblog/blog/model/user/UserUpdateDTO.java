package com.myblog.blog.model.user;

import jakarta.validation.constraints.NotNull;

public record UserUpdateDTO(
        @NotNull
        Long id,
        String name,
        String email,
        String phone) {
}
