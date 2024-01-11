INSERT INTO transaction (label, amount, transaction_date, transaction_type, category_id, account_id)
VALUES
('Salary', 100000, '2023-12-01 12:15', 'credit', 3, 1),
('Noël gift', 50000, '2023-12-02 2:00', 'debit', 4, 2),
('New shoe', 20000, '2023-12-06 4:00', 'debit', 5, 3),

('Groceries', 50.00, CURRENT_TIMESTAMP, 'debit', 5, 3),
('Salary', 3000.00, '2023-12-01 09:00:00', 'credit', 3, 2),
('Electricity Bill', 80.00, '2023-11-20 15:30:00', 'debit', 6, 3);
