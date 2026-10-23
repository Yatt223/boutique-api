CREATE TABLE employes (
    id                 BIGSERIAL    PRIMARY KEY,
    nom                VARCHAR(100) NOT NULL,
    prenom             VARCHAR(100) NOT NULL,
    email              VARCHAR(255) NOT NULL,
    mot_de_passe_hash  VARCHAR(100) NOT NULL,
    role               VARCHAR(20)  NOT NULL CHECK (role IN ('ADMIN', 'MANAGER', 'CAISSIER')),
    actif              BOOLEAN      NOT NULL DEFAULT TRUE,
    date_embauche      DATE         NOT NULL,
    cree_le            TIMESTAMP    NOT NULL DEFAULT now()
);

-- Un email ne peut exister qu'une fois, sans tenir compte des majuscules
CREATE UNIQUE INDEX ux_employes_email ON employes (lower(email));