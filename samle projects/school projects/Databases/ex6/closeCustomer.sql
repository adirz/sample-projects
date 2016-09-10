CREATE FUNCTION
    closeCustomer(id integer)
    returns integer as $$

	DECLARE
		acNum integer;
		stat integer;
	BEGIN
	    SELECT	AccountNum into acNum , AccountStatus into stat
	    FROM	Customers
		WHERE	CustomerID = id;
		if acNum = null OR stat = 'close'	then
		    return -1;
		else
            DELETE FROM AccountBalance
            WHERE       CustomerID = id;
            DELETE FROM Savings
            WHERE       CustomerID = id;
            DELETE FROM Top10Customers
            WHERE       CustomerID = id;
            UPDATE  Customers
            SET     AccountStatus = 'close'
            WHERE   CustomerID = id;
            EXCEPTION
                return -1;
            return acNum;
	END;
	$$ language plpgsql;