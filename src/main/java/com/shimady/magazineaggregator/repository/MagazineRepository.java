package com.shimady.magazineaggregator.repository;

import com.shimady.magazineaggregator.model.Author;
import com.shimady.magazineaggregator.model.Magazine;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MagazineRepository extends JpaRepository<Magazine, Long> {

    @EntityGraph(type = EntityGraph.EntityGraphType.LOAD, attributePaths = "articles")
    Optional<Magazine> findAndFetchArticlesById(Long id);

    List<Magazine> findAllByAuthor(Author author);

    List<Magazine> findALlByTitleStartingWithIgnoreCase(String title);

    List<Magazine> findALlBySubjectStartingWithIgnoreCase(String subject);

    List<Magazine> findALlByAuthorFirstNameStartingWithOrAuthorLastNameStartingWithAllIgnoreCase(String firstName, String lastName);
}
