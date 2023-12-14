CREATE TABLE IF NOT EXISTS transfer_history(
    id SERIAL PRIMARY KEY,
    debit_transaction_id INT NOT NULL REFERENCES transaction(id),
    credit_transaction_id INT NOT NULL REFERENCES transaction(id),
    datetime TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);