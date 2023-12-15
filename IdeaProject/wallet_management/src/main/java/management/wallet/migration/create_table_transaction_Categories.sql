CREATE TYPE SPECIFIC_CATEGORY AS ENUM ('expense', 'income', 'other');
CREATE TABLE IF NOT EXISTS transaction_categories (
    Id SERIAL PRIMARY KEY,
    Category_name VARCHAR(50) NOT NULL,
    Category_categorie SPECIFIC_CATEGORY NOT NULL
);