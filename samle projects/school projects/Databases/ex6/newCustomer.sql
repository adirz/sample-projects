CREATE FUNCTION
	newCustomer( id integer ,name varchar, pword varchar, debtable real)
	returns integer as $$
	
	DECLARE
		acNum integer;
	BEGIN
		if NOT EXISTS (
			SELECT	*
			FROM	Customers
			WHERE	Customers.CustomerID = id;
		)	then
			insert into Customers values(id, name, pword, 'open', debtable) returning Customers.AccountNum into acNum;
			return acNum;
		else
			if EXISTS (
			    SELECT	*
			    FROM	Customers
			    WHERE	Customers.CustomerID = id AND Customers.AccountStatus = 'close';
			)   then
			    UPDATE  Customers
			    SET     Customers.AccountStatus = 'open', acNum = Customers.AccountNum
			    WHERE   Customers.CustomerID = id AND Customers.CustomerPassword = pword AND Overdraft = debtable;
			    insert into AccountBalance values(acNum, 0);
			    return acNum;
			else
			    return -1;
		    end if;
        end if;
	END;
	$$ language plpgsql;