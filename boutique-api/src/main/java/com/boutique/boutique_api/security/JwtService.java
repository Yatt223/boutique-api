package com.boutique.boutique_api.security;


import com.boutique.boutique_api.employe.Employe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
public class JwtService {

    private final JwtEncoder encoder;
    private final Duration duree;

    public JwtService(JwtEncoder encoder, @Value("${app.jwt.expiration-minutes}") long minutes) {
        this.encoder = encoder;
        this.duree = Duration.ofMinutes(minutes);
    }

    public String generer(Employe employe) {
        Instant maintenant = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("boutique-api")
                .issuedAt(maintenant)
                .expiresAt(maintenant.plus(duree))
                .subject(String.valueOf(employe.getId()))
                .build();

        JwsHeader entete = JwsHeader.with(MacAlgorithm.HS256).build();
        return encoder.encode(JwtEncoderParameters.from(entete, claims)).getTokenValue();
    }
}