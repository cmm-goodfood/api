-- 1. Cleanup

ALTER TABLE IF EXISTS T_UTILISATEUR DROP CONSTRAINT IF EXISTS "T_UTILISATEUR_Fk";
ALTER TABLE IF EXISTS T_RESTAURANT DROP CONSTRAINT IF EXISTS "T_RESTAURANT_Fk1";
ALTER TABLE IF EXISTS T_RESTAURANT DROP CONSTRAINT IF EXISTS "T_RESTAURANT_Fk2";
ALTER TABLE IF EXISTS T_CODE_PROMO DROP CONSTRAINT IF EXISTS "T_CODE_PROMO_Fk";
ALTER TABLE IF EXISTS T_COMMANDE DROP CONSTRAINT IF EXISTS "T_COMMANDE_Fk1";
ALTER TABLE IF EXISTS T_COMMANDE DROP CONSTRAINT IF EXISTS "T_COMMANDE_Fk2";
ALTER TABLE IF EXISTS T_COMMANDE DROP CONSTRAINT IF EXISTS "T_COMMANDE_Fk3";
ALTER TABLE IF EXISTS T_COMMANDE DROP CONSTRAINT IF EXISTS "T_COMMANDE_Fk4";
ALTER TABLE IF EXISTS T_COMMANDE DROP CONSTRAINT IF EXISTS "T_COMMANDE_Fk5";
ALTER TABLE IF EXISTS T_PRODUIT DROP CONSTRAINT IF EXISTS "T_PRODUIT_Fk1";
ALTER TABLE IF EXISTS T_PRODUIT DROP CONSTRAINT IF EXISTS "T_PRODUIT_Fk2";
ALTER TABLE IF EXISTS T_LIEN_COMMANDE_PRODUIT DROP CONSTRAINT IF EXISTS "T_LIEN_COMMANDE_PRODUIT_Fk1";
ALTER TABLE IF EXISTS T_LIEN_COMMANDE_PRODUIT DROP CONSTRAINT IF EXISTS "T_LIEN_COMMANDE_PRODUIT_Fk2";

DROP TABLE IF EXISTS T_LIEN_COMMANDE_PRODUIT;
DROP TABLE IF EXISTS T_PRODUIT;
DROP TABLE IF EXISTS T_CATEGORIE_PRODUIT;
DROP TABLE IF EXISTS T_COMMANDE;
DROP TABLE IF EXISTS T_TVA;
DROP TABLE IF EXISTS T_TYPE_COMMANDE;
DROP TABLE IF EXISTS T_ETAT_COMMANDE;
DROP TABLE IF EXISTS T_CODE_PROMO;
DROP TABLE IF EXISTS T_RESTAURANT;
DROP TABLE IF EXISTS T_UTILISATEUR;
DROP TABLE IF EXISTS T_ADRESSE;

-- 2. Tables

-- 2.1. Entities
CREATE TABLE T_ADRESSE
(
    id                  SERIAL PRIMARY KEY,
    numeroVoie          VARCHAR(15),
    voie                VARCHAR(500),
    complement          VARCHAR(500),
    ville               VARCHAR(250),
    codePostal          INT
);

CREATE TABLE T_UTILISATEUR
(
    id                  SERIAL PRIMARY KEY,
    email               VARCHAR(250),
    telephone           VARCHAR(15),
    motDePasse          VARCHAR(250),
    prenom              VARCHAR(250),
    nom                 VARCHAR(250),
    permissionsAPI      INT,

    idAdresse           INT
);

CREATE TABLE T_RESTAURANT
(
    id                  SERIAL PRIMARY KEY,
    nom                 VARCHAR(250),
    description         VARCHAR(500),
    rayonLivraison      INT,
    horaireOuverture    TIME,
    horaireFermeture    TIME,

    idUtilisateur       INT,
    idAdresse           INT
);

CREATE TABLE T_CODE_PROMO
(
    id                  SERIAL PRIMARY KEY,
    code                VARCHAR(250),
    dateDebut           TIMESTAMP,
    dateFin             TIMESTAMP,
    pourcentage         DECIMAL,

    idRestaurant        INT
);

CREATE TABLE T_ETAT_COMMANDE
(
    id                  SERIAL PRIMARY KEY,
    nom                 VARCHAR(250)
);

CREATE TABLE T_TYPE_COMMANDE
(
    id                  SERIAL PRIMARY KEY,
    nom                 VARCHAR(250)
);

CREATE TABLE T_TVA
(
    id                  SERIAL PRIMARY KEY,
    taux                INT,
    dateDebut           TIMESTAMP,
    dateFin             TIMESTAMP
);

CREATE TABLE T_COMMANDE
(
    id                  SERIAL PRIMARY KEY,
    dateCreation        TIMESTAMP,
    dateFin             TIMESTAMP,
    prixHT              DECIMAL,

    idEtat              INT,
    idType              INT,
    idUtilisateur       INT,
    idTVA               INT,
    idRestaurant        INT
);

CREATE TABLE T_CATEGORIE_PRODUIT
(
    id                  SERIAL PRIMARY KEY,
    nom                 VARCHAR(250)
);

CREATE TABLE T_PRODUIT
(
    id                  SERIAL PRIMARY KEY,
    nom                 VARCHAR(250),
    description         VARCHAR(500),
    sourceImage         VARCHAR(500),
    prixHT              DECIMAL,
    tauxReduction       DECIMAL,
    tempsPreparation    INT,
    quantiteStock       INT,
    valeurNutri100g     DECIMAL,
    allergenes          VARCHAR(500),

    idRestaurant        INT,
    idCategorie         INT
);

-- 2.2. RelationShips
CREATE TABLE T_LIEN_COMMANDE_PRODUIT
(
    id                  SERIAL PRIMARY KEY,
    idCommande          INT,
    idProduit           INT,
    quantiteProduit     INT
);

-- 3. Constraints

ALTER TABLE T_UTILISATEUR
ADD CONSTRAINT T_UTILISATEUR_Fk FOREIGN KEY (idAdresse) REFERENCES T_ADRESSE(id);

ALTER TABLE T_RESTAURANT
ADD CONSTRAINT T_RESTAURANT_Fk1 FOREIGN KEY (idUtilisateur) REFERENCES T_UTILISATEUR(id),
ADD CONSTRAINT T_RESTAURANT_Fk2 FOREIGN KEY (idAdresse) REFERENCES T_ADRESSE(id);

ALTER TABLE T_CODE_PROMO
ADD CONSTRAINT T_CODE_PROMO_Fk FOREIGN KEY (idRestaurant) REFERENCES T_RESTAURANT(id);

ALTER TABLE T_COMMANDE
ADD CONSTRAINT T_COMMANDE_Fk1 FOREIGN KEY (idEtat) REFERENCES T_ETAT_COMMANDE(id),
ADD CONSTRAINT T_COMMANDE_Fk2 FOREIGN KEY (idType) REFERENCES T_TYPE_COMMANDE(id),
ADD CONSTRAINT T_COMMANDE_Fk3 FOREIGN KEY (idUtilisateur) REFERENCES T_UTILISATEUR(id),
ADD CONSTRAINT T_COMMANDE_Fk4 FOREIGN KEY (idTVA) REFERENCES T_TVA(id),
ADD CONSTRAINT T_COMMANDE_Fk5 FOREIGN KEY (idRestaurant) REFERENCES T_RESTAURANT(id);

ALTER TABLE T_PRODUIT
ADD CONSTRAINT T_PRODUIT_Fk1 FOREIGN KEY (idRestaurant) REFERENCES T_RESTAURANT(id),
ADD CONSTRAINT T_PRODUIT_Fk2 FOREIGN KEY (idCategorie) REFERENCES T_CATEGORIE_PRODUIT(id);

ALTER TABLE T_LIEN_COMMANDE_PRODUIT
ADD CONSTRAINT T_LIEN_COMMANDE_PRODUIT_Fk1 FOREIGN KEY (idCommande) REFERENCES T_COMMANDE(id),
ADD CONSTRAINT T_LIEN_COMMANDE_PRODUIT_Fk2 FOREIGN KEY (idProduit) REFERENCES T_PRODUIT(id);

-- Main requests
--     SELECT
--         *
--     FROM T_COMMANDE commande
--     INNER JOIN T_COMMANDE_PRODUIT link
--         ON commande.id = link.idCommande
--     INNER JOIN T_PRODUIT produit
--         ON produit.id = link.idProduit
--     WHERE commande.idRestaurant = ''