package com.boutique.boutique_api.security;

import jakarta.validation.constraints.NotBlank;

public record ConnexionRequest(@NotBlank String email, @NotBlank String motDePasse) {
}