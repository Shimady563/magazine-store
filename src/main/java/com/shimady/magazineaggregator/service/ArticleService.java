package com.shimady.magazineaggregator.service;

import com.shimady.magazineaggregator.exception.ResourceNotFoundException;
import com.shimady.magazineaggregator.model.Article;
import com.shimady.magazineaggregator.model.Magazine;
import com.shimady.magazineaggregator.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ArticleService {


    private final ArticleRepository articleRepository;

    @Autowired
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public Article getArticleById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Article " + id + " not found"));
    }

    @Transactional(readOnly = true)
    public List<Article> getAllArticlesByOptionAndMagazine(Magazine magazine, String request, String option) {
        return switch (option) {
            case "title" -> articleRepository.findALlByMagazineAndTitleStartingWithIgnoreCase(magazine, request);
            case "theme" -> articleRepository.findALlBByMagazineAndThemeStartingWithIgnoreCase(magazine, request);
            default -> articleRepository.findAllByMagazine(magazine);
        };
    }
}
