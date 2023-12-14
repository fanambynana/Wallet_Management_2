CREAT TABLE IF NOT EXIST transactionCategories (
    Id SERIAL PRIMARY KEY,
    TransactionId int REFERENCES transaction(Id),

);