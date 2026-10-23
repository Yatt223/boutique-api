package com.boutique.boutique_api.employe;

import java.time.LocalDate;

public record EmployeDto(
        Long id,
        String nom,
        String prenom,
        String email,
        Role role,
        boolean actif,
        LocalDate dateEmbauche) {

    public static EmployeDto de(Employe e) {
        return new EmployeDto(e.getId(), e.getNom(), e.getPrenom(), e.getEmail(),
                e.getRole(), e.isActif(), e.getDateEmbauche());
    }
}