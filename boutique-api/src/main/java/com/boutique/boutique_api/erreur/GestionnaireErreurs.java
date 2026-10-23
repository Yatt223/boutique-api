package com.boutique.boutique_api.erreur;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GestionnaireErreurs {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, String>> statut(ResponseStatusException e) {
        String message = e.getReason() != null ? e.getReason() : "Une erreur est survenue.";
        return ResponseEntity.status(e.getStatusCode()).body(Map.of("message", message));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validation(MethodArgumentNotValidException e) {
        Map<String, String> champs = new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors()
                .forEach(f -> champs.put(f.getField(), f.getDefaultMessage()));

        return ResponseEntity.badRequest().body(Map.of("message", "Données invalides.", "champs", champs));
    }
}