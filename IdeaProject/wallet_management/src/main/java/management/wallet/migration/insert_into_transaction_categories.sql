DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM transaction_categories WHERE category_name = 'Prêt') THEN
            INSERT INTO transaction_categories (category_name, category_categorie) VALUES ('Prêt', 'other');
        END IF;

        IF NOT EXISTS (SELECT 1 FROM transaction_categories WHERE category_name = 'Téléphone et Multimédia') THEN
            INSERT INTO transaction_categories (category_name, category_categorie) VALUES ('Téléphone et Multimédia', 'expense');
        END IF;

        IF NOT EXISTS(SELECT 1 FROM transaction_categories WHERE category_name = 'salaire') THEN
        INSERT INTO transaction_categories (category_name, category_categorie )VALUES ('salaire', 'income');
        END IF;

    END $$;
