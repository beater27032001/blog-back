package com.myblog.blog.model.article;

import jakarta.validation.constraints.NotNull;

public record ArticleUpdateDTO(
        @NotNull
        Long id,
        String title,
        String content) {
}
