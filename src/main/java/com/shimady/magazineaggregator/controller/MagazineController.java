package com.shimady.magazineaggregator.controller;

import com.shimady.magazineaggregator.model.Article;
import com.shimady.magazineaggregator.model.Author;
import com.shimady.magazineaggregator.model.Magazine;
import com.shimady.magazineaggregator.service.AuthorService;
import com.shimady.magazineaggregator.service.MagazineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/magazines")
public class MagazineController {

    private final MagazineService magazineService;
    private final AuthorService authorService;

    @Autowired
    public MagazineController(MagazineService magazineService, AuthorService authorService) {
        this.magazineService = magazineService;
        this.authorService = authorService;
    }

    @GetMapping("")
    public String getMagazines(Model model) {
        List<Magazine> magazines = magazineService.getAllMagazines();
        model.addAttribute("magazines", magazines);
        return "magazines";
    }

    @GetMapping("/{id}")
    public String getMagazineById(@PathVariable Long id, Model model) {
        Magazine magazine = magazineService.getMagazineWithArticlesById(id);
        model.addAttribute("magazine", magazine);
        return "articles";
    }

    @GetMapping("/user")
    public String getMagazinesByUser(Authentication authentication, Model model) {
        Author author = (Author) authentication.getPrincipal();
        List<Magazine> magazines = magazineService.getMagazinesByAuthor(author);
        model.addAttribute("magazines", magazines);
        return "users-magazines";
    }

    @GetMapping("/create")
    public String showCreateMagazinePage() {
        return "create-magazine";
    }

    @GetMapping("/edit/{id}")
    public String getEditingPage(@PathVariable(value = "id") Long magazineId, Authentication authentication, ModelMap model) {
        Magazine magazine = magazineService.getMagazineWithArticlesById(magazineId);
        if (!magazine.getAuthor().getId()
                .equals(
                ((Author) authentication.getPrincipal()).getId())
        ) {
            throw new RuntimeException("Access denied");
        }

        model.addAttribute("magazine", magazine);
        return "edit-magazine";
    }

    @PostMapping("/edit/{id}")
    public String updateMagazine(
            @PathVariable Long id,
            @RequestParam String title,
            @RequestParam String theme,
            @RequestParam(value = "articleTitle[]") String[] articleTitles,
            @RequestParam(value = "articleTheme[]") String[] articleThemes,
            @RequestParam(value = "articleContent[]") String[] articleContents,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        Magazine magazine = magazineService.getMagazineWithArticlesById(id);
        if (!magazine.getAuthor().getId()
                .equals(
                        ((Author) authentication.getPrincipal()).getId())
        ) {
            throw new RuntimeException("Access denied");
        }

        magazine.setTitle(title);
        magazine.setSubject(theme);

        List<Article> articles = magazine.getArticles();
        for (int i = 0; i < articleTitles.length; i++) {
            Article article = articles.get(i);
            article.setTitle(articleTitles[i]);
            article.setTheme(articleThemes[i]);
            article.setText(articleContents[i]);
        }

        magazineService.saveMagazine(magazine);
        redirectAttributes.addFlashAttribute("message", "Magazine successfully updated");
        return "redirect:/magazines/edit/" + magazine.getId();
    }

    @PostMapping("/delete/{id}")
    public String deleteMagazine(@PathVariable Long id, Authentication authentication) {
        Magazine magazine = magazineService.getMagazineById(id);
        if (!magazine.getAuthor().getId()
                .equals(
                        ((Author) authentication.getPrincipal()).getId())
        ) {
            throw new RuntimeException("Access denied");
        }

        magazineService.deleteMagazine(magazine);
        return "redirect:/magazines/user";
    }

    @PostMapping("/create")
    public String createMagazine(
            @RequestParam String title,
            @RequestParam String theme,
            @RequestParam(value = "articleTitle[]") String[] articleTitles,
            @RequestParam(value = "articleTheme[]") String[] articleThemes,
            @RequestParam(value = "articleContent[]") String[] articleContents,
            Authentication authentication,
            RedirectAttributes redirectAttributes
    ) {
        String username = authentication.getName();
        Author author = authorService.getAuthorByUsername(username);
        Magazine magazine = new Magazine(title, theme);
        author.addMagazine(magazine);

        for (int i = 0; i < articleTitles.length; i++) {
            Article article = new Article(
                    articleTitles[i],
                    articleThemes[i],
                    articleContents[i]
            );
            magazine.addArticle(article);
        }

        authorService.updateAuthor(author);
        redirectAttributes.addFlashAttribute("message", "Magazine successfully created");
        return "redirect:/magazines/create";
    }
}
