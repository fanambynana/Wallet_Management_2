CREATE TYPE ACCOUNT_NAME AS ENUM('current_account', 'savings_account');
CREATE TYPE ACCOUNT_TYPE AS ENUM('bank', 'cash', 'mobile money');

CREATE TABLE IF NOT EXISTS "account"(
    id SERIAL PRIMARY KEY,
    accountName ACCOUNT_NAME NOT NULL,
    balanceAmount DECIMAL NOT NULL,
    balanceUpdateDateTime TIMESTAMP NOT NULL,
    currency_id INT NOT NULL REFERENCES currency(id),
    accountType ACCOUNT_TYPE NOT NULL
);