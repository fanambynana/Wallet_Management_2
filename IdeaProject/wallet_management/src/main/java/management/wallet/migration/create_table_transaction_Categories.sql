CREATE TYPE CATEGORY_GROUP AS ENUM ('expense', 'income', 'both' );
CREATE TABLE IF NOT EXISTS transaction_categories (
    id SERIAL PRIMARY KEY,
    category_name VARCHAR(50) UNIQUE NOT NULL,
    category_group CATEGORY_GROUP NOT NULL
);