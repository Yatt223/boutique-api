package com.boutique.boutique_api.security;

public enum Permission {
    DASHBOARD_VOIR("dashboard:voir"),
    PRODUITS_GERER("produits:gerer"),
    STOCKS_GERER("stocks:gerer"),
    VENTES_VOIR("ventes:voir"),
    VENTES_CREER("ventes:creer"),
    VENTES_ANNULER("ventes:annuler"),
    COMMANDES_GERER("commandes:gerer"),
    CAISSE_GERER("caisse:gerer"),
    DEPENSES_GERER("depenses:gerer"),
    EMPLOYES_GERER("employes:gerer");

    private final String code;

    Permission(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }
}