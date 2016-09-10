CREATE FUNCTION checkUpdate()
RETURNS trigger AS $$
	DECLARE
		maxDebt integer;
	BEGIN
	    SELECT	Customers.Overdraft into maxDebt
	    FROM	Customers
		WHERE	Customers.AccountNum = NEW.AccountNum;
        if NEW.Balance < maxDebt then
            RAISE EXCEPTION 'too much debt';
        end if;
    END;
$$ language plpgsql;

CREATE  TRIGGER     bigDebt
BEFORE  UPDATE ON   AccountBalance
FOR EACH STATEMENT  EXECUTE PROCEDURE   checkUpdate()
’ language plpgsql;