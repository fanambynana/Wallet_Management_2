CREATE TYPE ACCOUNT_NAME AS ENUM('current_account', 'savings_account');
CREATE TYPE ACCOUNT_TYPE AS ENUM('bank', 'cash', 'mobile_money');

CREATE TABLE IF NOT EXISTS "account"(
    id SERIAL PRIMARY KEY,
    account_name ACCOUNT_NAME NOT NULL,
    balance_id INT UNIQUE NOT NULL REFERENCES balance(id),
    currency_id INT NOT NULL REFERENCES currency(id),
    account_type ACCOUNT_TYPE NOT NULL
);