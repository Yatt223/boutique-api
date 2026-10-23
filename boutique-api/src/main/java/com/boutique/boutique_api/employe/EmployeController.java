package com.boutique.boutique_api.employe;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employes")
public class EmployeController {

    private final EmployeService service;

    public EmployeController(EmployeService service) {
        this.service = service;
    }

    @GetMapping
    public List<EmployeDto> lister(@RequestParam(required = false) String email) {
        return service.lister(email);
    }

    @GetMapping("/{id}")
    public EmployeDto obtenir(@PathVariable Long id) {
        return service.obtenir(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('employes:gerer')")
    public EmployeDto creer(@Valid @RequestBody CreerEmployeRequest requete) {
        return service.creer(requete);
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('employes:gerer')")
    public EmployeDto modifier(@PathVariable Long id,
                               @Valid @RequestBody ModifierEmployeRequest requete,
                               Authentication authentication) {
        Long idConnecte = Long.valueOf(authentication.getName());
        return service.modifier(id, requete, idConnecte);
    }
}