CREATE TABLE IF NOT EXISTS transaction(
    id SERIAL PRIMARY KEY ,
    description TEXT,
    amount DOUBLE PRECISION NOT NULL ,
    type VARCHAR(255) NOT NULL CHECK ( type = 'revenu'OR type = 'dépense' OR type = 'transfert'),
    corespondent VARCHAR(255)
);