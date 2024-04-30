package com.shimady.magazineaggregator.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Set;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "first_name")
    private String firstName;

    @NotBlank
    @Column(name = "secondName")
    private String secondName;

    @NotNull
    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Email
    @Column(name = "email")
    private String email;

    @OneToMany(mappedBy = "author", fetch = FetchType.LAZY)
    private Set<Magazine> magazines;
}
