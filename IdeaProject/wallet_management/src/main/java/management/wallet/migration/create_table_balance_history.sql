CREATE TABLE IF NOT EXISTS balance_history(
    id SERIAL PRIMARY KEY,
    balance_id INT NOT NULL REFERENCES balance(id),
    account_id INT NOT NULL REFERENCES account(id),
    datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);