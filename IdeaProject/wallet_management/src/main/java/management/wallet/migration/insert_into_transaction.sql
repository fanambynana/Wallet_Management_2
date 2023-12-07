INSERT INTO transaction (label, amount, transactionDate, transactionsType)
VALUES
    ('Groceries', 50.00, CURRENT_TIMESTAMP, 'debit'),
    ('Salary', 3000.00, '2023-12-01 09:00:00', 'credit'),
    ('Electricity Bill', 80.00, '2023-11-20 15:30:00', 'debit');