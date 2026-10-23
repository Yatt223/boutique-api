package com.boutique.boutique_api.employe;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class EmployeService {

    private final EmployeRepository repo;
    private final PasswordEncoder encoder;

    public EmployeService(EmployeRepository repo, PasswordEncoder encoder) {
        this.repo = repo;
        this.encoder = encoder;
    }

    @Transactional(readOnly = true)
    public List<EmployeDto> lister(String email) {
        if (email != null && !email.isBlank()) {
            return repo.findByEmailIgnoreCase(email.trim()).map(e -> List.of(EmployeDto.de(e))).orElse(List.of());
        }
        return repo.findAll(Sort.by("id")).stream().map(EmployeDto::de).toList();
    }

    @Transactional(readOnly = true)
    public EmployeDto obtenir(Long id) {
        return EmployeDto.de(trouver(id));
    }

    @Transactional
    public EmployeDto creer(CreerEmployeRequest r) {
        if (repo.existsByEmailIgnoreCase(r.email().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cet email est déjà utilisé.");
        }

        Employe e = new Employe();
        e.setNom(r.nom().trim());
        e.setPrenom(r.prenom().trim());
        e.setEmail(r.email().trim());
        e.setMotDePasseHash(encoder.encode(r.motDePasse()));
        e.setRole(r.role());
        e.setActif(r.actif() == null || r.actif());
        e.setDateEmbauche(r.dateEmbauche());

        return EmployeDto.de(repo.save(e));
    }

    @Transactional
    public EmployeDto modifier(Long id, ModifierEmployeRequest r, Long idConnecte) {
        Employe e = trouver(id);

        boolean changeRole = r.role() != null && r.role() != e.getRole();
        boolean desactivation = Boolean.FALSE.equals(r.actif()) && e.isActif();

        // On ne modifie pas son propre rôle et on ne se désactive pas soi-même
        if (id.equals(idConnecte) && (changeRole || desactivation)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Tu ne peux pas modifier ton propre rôle ni désactiver ton propre compte.");
        }

        // Il doit toujours rester un administrateur actif
        boolean quitteAdmin = e.getRole() == Role.ADMIN && e.isActif() && (changeRole || desactivation);
        if (quitteAdmin && repo.countByRoleAndActif(Role.ADMIN, true) <= 1) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Il doit rester au moins un administrateur actif.");
        }

        if (r.email() != null && !r.email().trim().equalsIgnoreCase(e.getEmail())
                && repo.existsByEmailIgnoreCase(r.email().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Cet email est déjà utilisé.");
        }

        if (r.nom() != null) e.setNom(r.nom().trim());
        if (r.prenom() != null) e.setPrenom(r.prenom().trim());
        if (r.email() != null) e.setEmail(r.email().trim());
        if (r.role() != null) e.setRole(r.role());
        if (r.actif() != null) e.setActif(r.actif());
        if (r.dateEmbauche() != null) e.setDateEmbauche(r.dateEmbauche());
        if (r.motDePasse() != null && !r.motDePasse().isBlank()) {
            e.setMotDePasseHash(encoder.encode(r.motDePasse()));
        }

        return EmployeDto.de(e);   // l'entité est suivie par la transaction : pas besoin de save()
    }

    private Employe trouver(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Employé introuvable."));
    }
}