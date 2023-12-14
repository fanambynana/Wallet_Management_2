CREATE TABLE IF NOT EXISTS currency_value(
    id SERIAL PRIMARY KEY,
    exchanged_currency_id INT NOT NULL REFERENCES currency(id),
    exchange_currency_id INT NOT NULL REFERENCES currency(id),
    exchange_value DECIMAL NOT NULL,
    exchange_date DATE
);