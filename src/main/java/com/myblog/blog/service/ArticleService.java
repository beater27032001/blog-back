package com.myblog.blog.service;

import com.myblog.blog.model.Role;
import com.myblog.blog.model.article.*;
import com.myblog.blog.model.user.User;
import com.myblog.blog.model.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    public ArticleListDetailsDTO createArticle(ArticleCreateDTO articleCreateDTO){
        User author = userRepository.findById(articleCreateDTO.authorId()).orElseThrow(() -> new RuntimeException("Author not found."));
        if(!author.getRole().equals(Role.ROLE_ADMIN)){
            throw new RuntimeException("Only admins can post articles");
        }

        Article article = new Article();
        article.setTitle(articleCreateDTO.title());
        article.setContent(articleCreateDTO.content());
        article.setAuthor(author);

        article = articleRepository.save(article);

        return new ArticleListDetailsDTO(article.getId(), article.getTitle(), article.getContent(), author.getName(), author.getEmail());
    }

    public ArticleListDetailsDTO updateArticle(ArticleUpdateDTO articleUpdateDTO){
        Article article = articleRepository.findById(articleUpdateDTO.id()).orElseThrow(() -> new RuntimeException("Article not found."));

        article.setTitle(articleUpdateDTO.title());
        article.setContent(articleUpdateDTO.content());

        article = articleRepository.save(article);

        return new ArticleListDetailsDTO(article.getId(), article.getTitle(), article.getContent(), article.getAuthor().getName(), article.getAuthor().getEmail());
    }

    public ArticleListDetailsDTO getArticleById(Long id){
        Article article = articleRepository.findById(id).orElseThrow(() -> new RuntimeException("Article not found."));
        return new ArticleListDetailsDTO(article.getId(), article.getTitle(), article.getContent(), article.getAuthor().getName(), article.getAuthor().getEmail());
    }

    public List<ArticleListDTO> getAllArticles(){
        return articleRepository.findAll().stream()
                .map(article -> new ArticleListDTO(article.getId(), article.getTitle(), article.getAuthor().getName()))
                .collect(Collectors.toList());
    }

    public void deleteArticle(Long id){
        articleRepository.deleteById(id);
    }
}
