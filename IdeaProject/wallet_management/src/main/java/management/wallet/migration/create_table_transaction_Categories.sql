CREATE TYPE SPECIFIC_CATEGORY AS ENUM ('expense', 'income', 'other');
CREATE TABLE IF NOT EXISTS transaction_categories (
    id SERIAL PRIMARY KEY,
    category_name VARCHAR(50) UNIQUE NOT NULL,
    category_categorie SPECIFIC_CATEGORY NOT NULL
);