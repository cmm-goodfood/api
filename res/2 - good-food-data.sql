COPY T_ADRESSE FROM '/var/lib/postgresql/data/assets/T_ADRESSE.csv' 
CSV HEADER;

COPY T_UTILISATEUR FROM '/var/lib/postgresql/data/assets/T_UTILISATEUR.csv'
CSV HEADER;

COPY T_RESTAURANT FROM '/var/lib/postgresql/data/assets/T_RESTAURANT.csv'
CSV HEADER;

COPY T_CODE_PROMO FROM '/var/lib/postgresql/data/assets/T_CODE_PROMO.csv'
CSV HEADER;

COPY T_ETAT_COMMANDE FROM '/var/lib/postgresql/data/assets/T_ETAT_COMMANDE.csv'
CSV HEADER;

COPY T_TYPE_COMMANDE FROM '/var/lib/postgresql/data/assets/T_TYPE_COMMANDE.csv'
CSV HEADER;

COPY T_TVA FROM '/var/lib/postgresql/data/assets/T_TVA.csv'
CSV HEADER;

COPY T_COMMANDE FROM '/var/lib/postgresql/data/assets/T_COMMANDE.csv'
CSV HEADER;

COPY T_CATEGORIE_PRODUIT FROM '/var/lib/postgresql/data/assets/T_CATEGORIE_PRODUIT.csv'
CSV HEADER;

COPY T_PRODUIT FROM '/var/lib/postgresql/data/assets/T_PRODUIT.csv'
CSV HEADER;

-- COPY T_LIEN_COMMANDE_PRODUIT FROM '/var/lib/postgresql/data/assets/T_LIEN_COMMANDE_PRODUIT.csv'
-- CSV HEADER;
INSERT INTO T_LIEN_COMMANDE_PRODUIT (
    idCommande, idProduit, quantiteProduit
)
SELECT
    floor(random() * (1000) + 1),
    floor(random() * (2000) + 1),
    floor(random() * (10) + 1)
FROM generate_series(1, 10000) s(i)