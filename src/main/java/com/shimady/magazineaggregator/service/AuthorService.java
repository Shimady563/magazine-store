package com.shimady.magazineaggregator.service;

import com.shimady.magazineaggregator.exception.ResourceNotFoundException;
import com.shimady.magazineaggregator.exception.UserAlreadyExistsException;
import com.shimady.magazineaggregator.model.Author;
import com.shimady.magazineaggregator.repository.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional(readOnly = true)
    public Author getAuthorByUsername(String username) {
        return authorRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User " + username + " not found"));
    }

    @Transactional(readOnly = true)
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User " + id + " not found"));
    }

    @Transactional
    public void saveAuthorIfNotExists(Author author) {
        if (authorRepository.findByUsername(author.getUsername()).isEmpty()) {
            authorRepository.save(author);
        } else {
            throw new UserAlreadyExistsException("User " + author.getUsername() + " already exists");
        }
    }

    @Transactional
    public void updateAuthor(Author author) {
        authorRepository.save(author);
        Authentication newAuth = new UsernamePasswordAuthenticationToken(author, null, author.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }
}
