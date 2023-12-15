CREATE TYPE TRANSACTION_TYPE AS ENUM('debit', 'credit');

CREATE TABLE IF NOT EXISTS "transaction"(
    id SERIAL PRIMARY KEY ,
    label VARCHAR(50) NOT NULL,
    amount DECIMAL NOT NULL ,
    transaction_date TIMESTAMP NOT NULL DEFAUL CURRENT_TIMESTAMP,
    transaction_type TRANSACTION_TYPE NOT NULL,
    category_id INT REFERENCES transaction_categories(id),   --   this column specify the transaction category
    account_id INT NOT NULL REFERENCES account(id)
);