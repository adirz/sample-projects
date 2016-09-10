CREATE FUNCTION forbesList()
RETURNS trigger AS $$
	DECLARE
		listBar real;
	BEGIN
	    SELECT  AccountBalance.Balance into listBar
	    FROM (
	        SELECT	AccountBalance.AccountNum, AccountBalance.Balance
	        FROM	AccountBalance, Customers
		    WHERE	AccountBalance.Balance > 0 AND Customers.AccountNum = AccountBalance.AccountNum
		            AND Customers.AccountStatus = 'open'
            ORDER BY Balance DESC
            limit 10;
	    )
        ORDER BY Balance ASC
        limit 1;
	    DELETE FROM Top10Customers;
	    SELECT	AccountBalance.AccountNum into Top10Customers.AccountNum,
	            AccountBalance.Balance into Top10Customers.Balance
	    FROM	AccountBalance, Customers
		WHERE	AccountBalance.Balance > 0 AND Customers.AccountNum = AccountBalance.AccountNum
		        AND Customers.AccountStatus = 'open'
	    WHERE   AccountBalance.Balance >= listBar;
    END;
$$ language plpgsql;

CREATE  TRIGGER     whoseRichest
AFTER   UPDATE ON   AccountBalance, Customers
FOR EACH STATEMENT  EXECUTE PROCEDURE   forbesList()
’ language plpgsql;