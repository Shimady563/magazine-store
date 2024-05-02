package com.shimady.magazineaggregator.service;

import com.shimady.magazineaggregator.model.Author;
import com.shimady.magazineaggregator.model.Magazine;
import com.shimady.magazineaggregator.repository.MagazineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MagazineService {

    private final MagazineRepository magazineRepository;

    @Autowired
    public MagazineService(MagazineRepository magazineRepository) {
        this.magazineRepository = magazineRepository;
    }

    public List<Magazine> getAllMagazines() {
        return magazineRepository.findAll();
    }

    public List<Magazine> getMagazinesByAuthor(Author author) {
        return magazineRepository.findByAuthor(author);
    }

    public void saveMagazine(Magazine magazine) {
        magazineRepository.save(magazine);
    }

    public Magazine getMagazineWithArticlesById(Long magazineId) {
        return magazineRepository.findAndFetchArticlesById(magazineId)
                .orElseThrow(() -> new RuntimeException("Magazine not found"));
    }
}
