CREATE FUNCTION
	doAction( id integer ,acName varchar, acDate date, amount real)
	returns integer as $$

	DECLARE
		actNum integer;
		accNum integer;
		stat integer;
	BEGIN
	    SELECT	Customers.AccountNum into accNum, Customers.AccountStatus into stat
	    FROM	Customers
		WHERE	Customers.CustomerID = id;
		if accNum = null OR stat = 'close'	then
		    return -1;
		else
			if  acName = 'close'
			    closeCustomer(id);
			else if acName = 'saving'
                newSaving(id, amount, acDate, 0, 0);
			end if;
            UPDATE  AccountBalance
            SET     AccountBalance.Balance = AccountBalance.Balance + amount
            WHERE   AccountBalance.AccountNum = accNum;
            EXCEPTION
                return -1;
			insert into Actions values(accNum, acName, acDate, amount) returning Actions.ActionNum into actNum;
            EXCEPTION
                return -1;
            return actNum;
        end if;
	END;
	$$ language plpgsql;