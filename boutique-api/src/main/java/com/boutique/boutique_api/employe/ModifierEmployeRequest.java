package com.boutique.boutique_api.employe;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ModifierEmployeRequest(
        @Size(min = 2, max = 100) String nom,
        @Size(min = 2, max = 100) String prenom,
        @Email @Size(max = 255) String email,
        @Size(min = 6, max = 100) String motDePasse,
        Role role,
        Boolean actif,
        LocalDate dateEmbauche) {
}