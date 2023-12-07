CREATE TYPE accountType  AS ENUM ('current_account', 'savings_account');
CREATE TABLE IF NOT EXISTS account(
    id SERIAL PRIMARY KEY,
    accountName accountType NOT NULL,
    transactionDate DATE NOT NULL,
    balance BIGDECIMAL NOT NULL,
    currencyId INT REFERENCES currency(Id),
    Type VARCHAR(20)
);