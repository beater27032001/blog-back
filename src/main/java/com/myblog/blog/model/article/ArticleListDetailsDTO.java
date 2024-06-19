package com.myblog.blog.model.article;

public record ArticleListDetailsDTO(Long id,
                                    String title,
                                    String content,
                                    String authorName,
                                    String authorEmail) {

    public ArticleListDetailsDTO(Article article){
        this(article.getId(), article.getTitle(), article.getContent(), article.getAuthor().getName(), article.getAuthor().getEmail());
    }
}
