package com.shimady.magazineaggregator.controller;

import com.shimady.magazineaggregator.model.Article;
import com.shimady.magazineaggregator.model.Magazine;
import com.shimady.magazineaggregator.service.ArticleService;
import com.shimady.magazineaggregator.service.MagazineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/articles")
public class ArticleController {


    private final ArticleService articleService;
    private final MagazineService magazineService;

    @Autowired
    public ArticleController(ArticleService articleService, MagazineService magazineService) {
        this.articleService = articleService;
        this.magazineService = magazineService;
    }

    @GetMapping("/{id}")
    public String getArticleById(@PathVariable Long id, Model model) {
        Article article = articleService.getArticleById(id);
        model.addAttribute("article", article);
        return "article";
    }

    @PostMapping("/search")
    public String searchForArticle(@RequestParam Long magazineId, @RequestParam String request, @RequestParam String option, Model model) {
        Magazine magazine = magazineService.getMagazineById(magazineId);
        List<Article> articles = articleService.getAllArticlesByOptionAndMagazine(magazine, request, option);
        model.addAttribute("articles", articles);
        model.addAttribute("magazineId", magazineId);
        return "articles";
    }
}
