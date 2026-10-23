package com.boutique.boutique_api.security;



import com.boutique.boutique_api.employe.Role;

import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/** Les droits de chaque rôle. Le serveur fait foi : Angular ne fait que refléter ces règles. */
public final class RolePermissions {

    private static final Map<Role, Set<Permission>> PERMISSIONS = Map.of(
            Role.ADMIN, EnumSet.allOf(Permission.class),
            Role.MANAGER, EnumSet.complementOf(EnumSet.of(Permission.EMPLOYES_GERER)),
            Role.CAISSIER, EnumSet.of(Permission.VENTES_CREER));

    private RolePermissions() {
    }

    public static Set<Permission> pour(Role role) {
        return PERMISSIONS.get(role);
    }
}