package com.boutique.boutique_api.security;

import com.boutique.boutique_api.employe.EmployeDto;



public record ConnexionResponse(String token, EmployeDto utilisateur) {
}