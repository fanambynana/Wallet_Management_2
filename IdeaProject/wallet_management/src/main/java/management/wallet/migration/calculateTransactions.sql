CREATE OR REPLACE  FUNCTION Calculate_total_transactions (
       account_id int,
       start_date TIMESTAMP,
       end_date TIMESTAMP
);
    RETURNS TABLE (total_income NUMERIC, total_expense NUMERIC) AS $$
    BEGIN
        SELECT
            COALESCE(sum(CASE WHEN amount > 0 THEN amount ELSE 0 END ), 0 )
            AS total_income,
            COALESCE(SUM(CASE WHEN amount < 0 THEN -amount ELSE 0 END ), 0)
            AS total_expense,
        FROM transaction

        WHERE
          account_id = calculateTotalTransactions.account_id
          AND transaction_date >= calculateTotalTransactions.start_date
          AND transaction_date <= calculateTotalTransactions.end_date;
   END;
   $$ LANGUAGE PLPGSQL;