CREATE TYPE ACCOUNT_NAME AS ENUM('current_account', 'savings_account');
CREATE TYPE ACCOUNT_TYPE AS ENUM('bank', 'cash', 'mobile money');

CREATE TABLE IF NOT EXISTS "account"(
    id SERIAL PRIMARY KEY,
    account_name ACCOUNT_NAME NOT NULL,
    balance_amount DECIMAL NOT NULL,
    balance_update_date_time TIMESTAMP NOT NULL,
    currency_id INT NOT NULL REFERENCES currency(id),
    account_type ACCOUNT_TYPE NOT NULL
);