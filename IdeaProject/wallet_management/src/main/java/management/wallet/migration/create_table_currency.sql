CREATE TABLE if not exists currency (
    id SERIAL PRIMARY KEY,
    name VARCHAR(20),
    code VARCHAR(4)
);