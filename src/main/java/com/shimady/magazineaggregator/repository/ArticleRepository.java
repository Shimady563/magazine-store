package com.shimady.magazineaggregator.repository;

import com.shimady.magazineaggregator.model.Article;
import com.shimady.magazineaggregator.model.Magazine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    List<Article> findALlByMagazineAndTitleStartingWithIgnoreCase(Magazine magazine, String title);

    List<Article> findALlBByMagazineAndThemeStartingWithIgnoreCase(Magazine magazine, String theme);

    List<Article> findAllByMagazine(Magazine magazine);
}
