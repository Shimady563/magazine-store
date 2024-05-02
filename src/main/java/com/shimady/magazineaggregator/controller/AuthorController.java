package com.shimady.magazineaggregator.controller;

import com.shimady.magazineaggregator.model.Author;
import com.shimady.magazineaggregator.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class AuthorController {

    private final AuthorService authorService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AuthorController(AuthorService authorService, PasswordEncoder passwordEncoder) {
        this.authorService = authorService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/login")
    public String getLoginPage(Authentication authentication, Model model) {
        if (authentication != null
        && !(authentication instanceof AnonymousAuthenticationToken)) {
            Author author = (Author) authentication.getPrincipal();
            model.addAttribute("user", author);
            return "profile";
        }

        return "sign-in";
    }

    @GetMapping("/sign-up")
    public String getSignUpPage() {
        return "sign-up";
    }

    @GetMapping("/profile")
    public String loadProfile(Authentication authentication, Model model) {
        Author author = (Author) authentication.getPrincipal();
        model.addAttribute("user", author);
        return "profile";
    }

    @PostMapping("/sign-up")
    public String createUser(
            @RequestParam String username,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password
    ) {

        Author author = new Author(
                username,
                firstName,
                lastName,
                email,
                passwordEncoder.encode(password)
        );

        authorService.saveAuthorIfNotExists(author);
        return "redirect:/users/login";
    }

    @PostMapping("/update")
    public String updateUser(
            @RequestParam Long id,
            @RequestParam String username,
            @RequestParam String firstName,
            @RequestParam String lastName,
            @RequestParam String email,
            @RequestParam String password,
            RedirectAttributes attributes
    ) {
        if (password.isBlank()) {
            attributes.addFlashAttribute("message", "You must write you old password or change it");
            return "redirect:/users/profile";
        }

        Author author = new Author(
                id,
                username,
                firstName,
                lastName,
                email,
                passwordEncoder.encode(password)
        );

        System.out.println(password);
        authorService.updateAuthor(author);
        Authentication newAuth = new UsernamePasswordAuthenticationToken(author, null, author.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);

        attributes.addFlashAttribute("message", "Credentials are successfully changed");
        return "redirect:/users/profile";
    }
}
