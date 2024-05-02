package com.shimady.magazineaggregator.service;

import com.shimady.magazineaggregator.model.Author;
import com.shimady.magazineaggregator.model.Magazine;
import com.shimady.magazineaggregator.repository.MagazineRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        return magazineRepository.findAllByAuthor(author);
    }

    @Transactional
    public void saveMagazine(Magazine magazine) {
        magazineRepository.save(magazine);
    }

    @Transactional(readOnly = true)
    public Magazine getMagazineWithArticlesById(Long magazineId) {
        return magazineRepository.findAndFetchArticlesById(magazineId)
                .orElseThrow(() -> new RuntimeException("Magazine not found"));
    }

    public Magazine getMagazineById(Long magazineId) {
        return magazineRepository.findById(magazineId)
                .orElseThrow(() -> new RuntimeException("Magazine not found"));
    }

    @Transactional
    public void deleteMagazine(Magazine magazine) {
        magazineRepository.delete(magazine);
    }

    public List<Magazine> getAllByMagazinesByOption(String request, String option) {
        return switch (option) {
            case "title" -> magazineRepository.findALlByTitleStartingWithIgnoreCase(request);
            case "subject" -> magazineRepository.findALlBySubjectStartingWithIgnoreCase(request);
            case "author" -> magazineRepository.findALlByAuthorFirstNameStartingWithOrAuthorLastNameStartingWithAllIgnoreCase(request, request);
            default -> magazineRepository.findAll();
        };
    }
}
