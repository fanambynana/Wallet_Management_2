DO $$
    BEGIN
        IF NOT EXISTS (SELECT 1 FROM transaction_categories WHERE category_name = 'Loan') THEN
            INSERT INTO transaction_categories (category_name, category_group) VALUES ('Loan', 'either');
        END IF;

        IF NOT EXISTS (SELECT 1 FROM transaction_categories WHERE category_name = 'Communication, PC') THEN
            INSERT INTO transaction_categories (category_name, category_group) VALUES ('Communication, PC', 'expense');
        END IF;

        IF NOT EXISTS(SELECT 1 FROM transaction_categories WHERE category_name = 'Salary') THEN
        INSERT INTO transaction_categories (category_name, category_group )VALUES ('Salary', 'income');
        END IF;

    END $$;
