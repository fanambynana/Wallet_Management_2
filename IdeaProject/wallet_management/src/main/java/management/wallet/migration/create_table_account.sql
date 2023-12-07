CREATE TYPE "name" AS ENUM('current_account', 'saving_account');
CREATE TYPE "type" AS ENUM('bank', 'cash', 'mobile money');

CREATE TABLE IF NOT EXISTS account(
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    currency VARCHAR(10) NOT NULL ,
    balance DOUBLE PRECISION DEFAULT 0
);