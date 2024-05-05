package com.shimady.magazineaggregator.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.*;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "magazine")
public class Magazine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @NotBlank
    @Column(name = "title")
    private String title;

    @Setter
    @NotBlank
    @Column(name = "subject")
    private String subject;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "author_id",
            foreignKey = @ForeignKey(name = "fk_magazine_author")
    )
    private Author author;

    @OneToMany(mappedBy = "magazine", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Article> articles = new ArrayList<>();

    public Magazine(String title, String subject) {
        this.subject = subject;
        this.title = title;
    }

    public void addArticle(Article article) {
        article.setMagazine(this);
        this.articles.add(article);
    }

    public void removeArticle(Article article) {
        article.setMagazine(null);
        this.articles.remove(article);
    }

    public void setArticles(List<Article> articles) {
        articles.forEach(article -> article.setMagazine(this));
        this.articles = articles;
    }
}
