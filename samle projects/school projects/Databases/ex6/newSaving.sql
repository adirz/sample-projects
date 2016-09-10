CREATE FUNCTION
	newSaving( id integer ,amount real, deDate date, years integer, rate real)
	returns integer as $$

	DECLARE
		sNum integer;
		accNum integer;
		stat integer;
	BEGIN
	    SELECT	Customers.AccountNum into accNum, Customers.AccountStatus into stat
	    FROM	Customers
		WHERE	Customers.CustomerID = id;
		if accNum = null OR stat = 'close'	then
		    return -1;
		else
			insert into Savings values(accNum, amount, deDate, years, rate) returning Savings.SavingNum into sNum;
			doAction(id, accNum, 'payment', deDate, -amount)
            EXCEPTION
                return -1;
            return sNum;
        end if;
	END;
	$$ language plpgsql;