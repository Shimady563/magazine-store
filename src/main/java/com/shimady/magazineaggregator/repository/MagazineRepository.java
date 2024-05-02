package com.shimady.magazineaggregator.repository;

import com.shimady.magazineaggregator.model.Author;
import com.shimady.magazineaggregator.model.Magazine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MagazineRepository extends JpaRepository<Magazine, Long> {

    List<Magazine> findByAuthor(Author author);
}
