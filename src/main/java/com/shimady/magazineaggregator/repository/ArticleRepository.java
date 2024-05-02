package com.shimady.magazineaggregator.repository;

import com.shimady.magazineaggregator.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
