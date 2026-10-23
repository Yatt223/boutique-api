package com.boutique.boutique_api.security;


import com.boutique.boutique_api.employe.Employe;
import com.boutique.boutique_api.employe.EmployeRepository;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.AbstractOAuth2TokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtEmployeConverter implements Converter<Jwt, AbstractOAuth2TokenAuthenticationToken<Jwt>> {

    private final EmployeRepository employes;

    public JwtEmployeConverter(EmployeRepository employes) {
        this.employes = employes;
    }

    @Override
    public AbstractOAuth2TokenAuthenticationToken<Jwt> convert(Jwt jwt) {
        Long id = Long.valueOf(jwt.getSubject());

        // Compte supprimé ou désactivé depuis l'émission du jeton : accès refusé (401)
        Employe employe = employes.findById(id)
                .filter(Employe::isActif)
                .orElseThrow(() -> new BadCredentialsException("Compte inconnu ou désactivé."));

        List<GrantedAuthority> droits = RolePermissions.pour(employe.getRole()).stream()
                .<GrantedAuthority>map(p -> new SimpleGrantedAuthority(p.code()))
                .toList();

        return new JwtAuthenticationToken(jwt, droits, String.valueOf(id));
    }
}