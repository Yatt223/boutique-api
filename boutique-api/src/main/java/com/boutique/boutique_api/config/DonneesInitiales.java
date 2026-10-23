package com.boutique.boutique_api.config;

import com.boutique.boutique_api.employe.Employe;
import com.boutique.boutique_api.employe.EmployeRepository;
import com.boutique.boutique_api.employe.Role;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Crée deux comptes de test au premier démarrage, UNIQUEMENT avec le profil « dev ».
 * Ils reçoivent les identifiants 1 et 2, comme dans db.json : les ventes déjà enregistrées
 * dans json-server continuent de pointer vers les bons employés.
 */
@Component
@Profile("dev")
public class DonneesInitiales implements ApplicationRunner {

    private final EmployeRepository employes;
    private final PasswordEncoder encoder;

    public DonneesInitiales(EmployeRepository employes, PasswordEncoder encoder) {
        this.employes = employes;
        this.encoder = encoder;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (employes.count() > 0) {
            return;
        }
        creer("Admin", "Super", "admin@boutique.local", "admin123", Role.ADMIN, LocalDate.of(2026, 1, 5));
        creer("Diallo", "Awa", "awa@boutique.local", "caisse123", Role.CAISSIER, LocalDate.of(2026, 3, 12));
    }

    private void creer(String nom, String prenom, String email, String motDePasse, Role role, LocalDate date) {
        Employe e = new Employe();
        e.setNom(nom);
        e.setPrenom(prenom);
        e.setEmail(email);
        e.setMotDePasseHash(encoder.encode(motDePasse));
        e.setRole(role);
        e.setActif(true);
        e.setDateEmbauche(date);
        employes.save(e);
    }
}