package com.shimady.magazineaggregator.service;

import com.shimady.magazineaggregator.model.Author;
import com.shimady.magazineaggregator.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorService implements UserDetailsService {

    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return authorRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Transactional
    public void saveAuthorIfNotExists(Author author) {
        if (authorRepository.findByUsername(author.getUsername()).isEmpty()) {
            authorRepository.save(author);
        } else {
            throw new IllegalArgumentException("User already exists");
        }
    }
}
