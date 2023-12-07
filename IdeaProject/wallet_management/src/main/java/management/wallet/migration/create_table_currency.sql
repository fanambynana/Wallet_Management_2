CREATE TABLE if not exists currency (
    Id SERIAL PRIMARY KEY,
    name VARCHAR(20),
    code VARCHAR(4)
);