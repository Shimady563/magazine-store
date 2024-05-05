package com.shimady.magazineaggregator.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @NotBlank
    @Column(name = "title")
    private String title;

    @Setter
    @NotBlank
    @Column(name = "theme")
    private String theme;

    @Setter
    @NotBlank
    @Column(name = "text")
    private String text;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "magazine_id",
            foreignKey = @ForeignKey(name = "fk_article_magazine")
    )
    private Magazine magazine;

    public Article( String title,  String theme, String text) {
        this.text = text;
        this.theme = theme;
        this.title = title;
    }
}
