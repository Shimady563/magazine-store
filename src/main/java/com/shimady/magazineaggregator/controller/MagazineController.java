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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/user")
    public String getMagazinesByUser(Authentication authentication, Model model) {
        Author author = (Author) authentication.getPrincipal();
        List<Magazine> magazines = magazineService.getMagazinesByAuthor(author);
        model.addAttribute("magazines", magazines);
        return "users-magazines";
    }

    @GetMapping("/create")
    public String showCreateMagazineForm() {
        return "create-magazine";
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
        redirectAttributes.addFlashAttribute("message", "magazine successfully created");
        return "redirect:/magazines/create";
    }
}
