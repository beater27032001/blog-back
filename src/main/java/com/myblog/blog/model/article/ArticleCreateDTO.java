package com.myblog.blog.model.article;

import jakarta.validation.constraints.NotBlank;

public record ArticleCreateDTO(
        @NotBlank
        String title,

        @NotBlank
        String content,

        @NotBlank
        Long authorId) {
}
