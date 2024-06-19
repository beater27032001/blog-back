package com.myblog.blog.model.article;

public record ArticleListDTO(Long id,
                             String title,
                             String authorName) {

    public ArticleListDTO(Article article){
        this(article.getId(), article.getTitle(), article.getAuthor().getName());
    }
}
