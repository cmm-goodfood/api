-- 4. Optional

DROP TABLE IF EXISTS A_LIEN_COMMANDE_PRODUIT;
CREATE TABLE A_LIEN_COMMANDE_PRODUIT
(
    id                  SERIAL PRIMARY KEY,
    idCommande          INT,
    idProduit           INT,
    quantiteProduit     INT
);
DROP TABLE IF EXISTS A_COMMANDE;
CREATE TABLE A_COMMANDE
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
DROP TABLE IF EXISTS TMP_ANCIEN_LIEN_COMMANDE_PRODUIT;
CREATE TABLE TMP_ANCIEN_LIEN_COMMANDE_PRODUIT -- postgres doesnt support table variable in procedure
(
	id                  serial,
	idCommande          INT,
	idProduit           INT,
	quantiteProduit     INT
);

CREATE OR REPLACE PROCEDURE T_PROC_PARTITION_COMMANDES()
LANGUAGE SQL
AS $$

    -- 1. Init temp table
	DELETE FROM TMP_ANCIEN_LIEN_COMMANDE_PRODUIT;
    INSERT INTO TMP_ANCIEN_LIEN_COMMANDE_PRODUIT
    SELECT
        link.*
    FROM A_LIEN_COMMANDE_PRODUIT link
    INNER JOIN T_COMMANDE commande
        ON link.idCommande = commande.id
    WHERE commande.dateCreation > current_date - 1
    AND commande.dateFin is not null;

    -- 2. Move data to archive
    INSERT INTO A_LIEN_COMMANDE_PRODUIT
    SELECT * FROM TMP_ANCIEN_LIEN_COMMANDE_PRODUIT;

    INSERT INTO A_COMMANDE
    SELECT
        commande.*
    FROM TMP_ANCIEN_LIEN_COMMANDE_PRODUIT links
    INNER JOIN T_COMMANDE commande
        ON links.idCommande = commande.id;

    -- 3. Remove data from normal tables
    DELETE FROM T_LIEN_COMMANDE_PRODUIT t
        USING TMP_ANCIEN_LIEN_COMMANDE_PRODUIT tmp
        WHERE t.id = tmp.id;

    DELETE FROM T_COMMANDE t
        USING TMP_ANCIEN_LIEN_COMMANDE_PRODUIT tmp
        WHERE t.id = tmp.idCommande;

    -- 4. Cleanup tmp table
    DROP TABLE TMP_ANCIEN_LIEN_COMMANDE_PRODUIT;

$$;

-- CALL T_PROC_PARTITION_COMMANDES();