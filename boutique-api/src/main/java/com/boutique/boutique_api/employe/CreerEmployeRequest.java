package com.boutique.boutique_api.employe;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record CreerEmployeRequest(
        @NotBlank @Size(min = 2, max = 100) String nom,
        @NotBlank @Size(min = 2, max = 100) String prenom,
        @NotBlank @Email @Size(max = 255) String email,
        @NotBlank @Size(min = 6, max = 100) String motDePasse,
        @NotNull Role role,
        Boolean actif,
        @NotNull LocalDate dateEmbauche) {
}