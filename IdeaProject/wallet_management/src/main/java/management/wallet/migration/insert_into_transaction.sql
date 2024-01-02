INSERT INTO "transaction" (label, amount, transaction_date, transaction_type)
VALUES
('Salary', 100000, 'credit', '2023-12-01 12:15'),
('Noël gift', 50000, 'debit', '2023-12-02 2:00'),
('New shoe', 20000, 'debit', '2023-12-06 4:00'),

('Groceries', 50.00, CURRENT_TIMESTAMP, 'debit'),
('Salary', 3000.00, '2023-12-01 09:00:00', 'credit'),
('Electricity Bill', 80.00, '2023-11-20 15:30:00', 'debit');
