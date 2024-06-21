package com.myblog.blog.model.article;

import com.myblog.blog.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findByTitleContaining(String title);

    List<Article> findByAuthor(User author);
}
