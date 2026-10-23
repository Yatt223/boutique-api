package com.boutique.boutique_api.security;


import com.boutique.boutique_api.employe.Employe;
import com.boutique.boutique_api.employe.EmployeDto;
import com.boutique.boutique_api.employe.EmployeRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final EmployeRepository employes;
    private final PasswordEncoder encoder;
    private final JwtService jwt;
    private final String hashFactice;

    public AuthController(EmployeRepository employes, PasswordEncoder encoder, JwtService jwt) {
        this.employes = employes;
        this.encoder = encoder;
        this.jwt = jwt;
        // Sert à faire le même travail de calcul quand l'email n'existe pas (voir plus bas)
        this.hashFactice = encoder.encode("mot-de-passe-factice");
    }

    @PostMapping("/login")
    public ConnexionResponse connexion(@Valid @RequestBody ConnexionRequest requete) {
        Optional<Employe> trouve = employes.findByEmailIgnoreCase(requete.email().trim());

        // On compare toujours un hash, même si l'email est inconnu : le temps de réponse
        // ne révèle pas si un compte existe
        String hash = trouve.map(Employe::getMotDePasseHash).orElse(hashFactice);
        boolean motDePasseValide = encoder.matches(requete.motDePasse(), hash);

        if (trouve.isEmpty() || !trouve.get().isActif() || !motDePasseValide) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email ou mot de passe incorrect.");
        }

        Employe employe = trouve.get();
        return new ConnexionResponse(jwt.generer(employe), EmployeDto.de(employe));
    }

    /** Qui suis-je ? Utile pour vérifier qu'un jeton est toujours valable. */
    @GetMapping("/me")
    public EmployeDto moi(Authentication authentication) {
        Long id = Long.valueOf(authentication.getName());
        return employes.findById(id).map(EmployeDto::de)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }
}