package com.myblog.blog.controller;

import com.myblog.blog.model.article.ArticleCreateDTO;
import com.myblog.blog.model.article.ArticleListDTO;
import com.myblog.blog.model.article.ArticleListDetailsDTO;
import com.myblog.blog.model.article.ArticleUpdateDTO;
import com.myblog.blog.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ResponseEntity<List<ArticleListDTO>> getAllArticles(){
        List<ArticleListDTO> articles = articleService.getAllArticles();
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleListDetailsDTO> getArticleById(@PathVariable Long id){
        ArticleListDetailsDTO article = articleService.getArticleById(id);
        return ResponseEntity.ok(article);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<ArticleListDetailsDTO> createArticle(@Valid @RequestBody ArticleCreateDTO articleCreateDTO){
        ArticleListDetailsDTO newArticle = articleService.createArticle(articleCreateDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(newArticle);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<ArticleListDetailsDTO> updateArticle(@PathVariable Long id, @Valid @RequestBody ArticleUpdateDTO articleUpdateDTO){
        ArticleListDetailsDTO articleUpdate = articleService.updateArticle(articleUpdateDTO);
        return ResponseEntity.ok(articleUpdate);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity deleteArticle(@PathVariable Long id){
        articleService.deleteArticle(id);
        return ResponseEntity.noContent().build();
    }



}
