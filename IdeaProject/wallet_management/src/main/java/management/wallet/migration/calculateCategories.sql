CREATE OR REPLACE FUNCTION calculateCategoryTotals(
    account_id INT,
    start_date TIMESTAMP,
    end_date TIMESTAMP
)
RETURNS TABLE(restaurant NUMERIC, salaire NUMERIC) AS $$
BEGIN
RETURN QUERY
SELECT
    COALESCE(SUM(CASE WHEN t.category_name = 'Restaurant' THEN t.amount ELSE 0 END), 0) AS restaurant,
    COALESCE(SUM(CASE WHEN t.category_name = 'Salaire' THEN t.amount ELSE 0 END), 0) AS salaire
FROM (
         SELECT tc.category_name, COALESCE(tr.amount, 0) AS amount
         FROM transaction_categories tc
                  LEFT JOIN transactions tr
                            ON tc.id = tr.category_id
                                AND tr.transaction_date >= start_date
                                AND tr.transaction_date <= end_date
                                AND tr.account_id = account_id
     ) t;
END;
$$ LANGUAGE PLPGSQL;
