CREATE TABLE IF NOT EXISTS currency_value(
    id SERIAL PRIMARY KEY,
    exchange_source_id INT NOT NULL REFERENCES currency(id),
    exchange_destination_id INT NOT NULL REFERENCES currency(id),
    exchange_value DECIMAL NOT NULL,
    exchange_date DATE DEFAULT CURRENT_DATE
);