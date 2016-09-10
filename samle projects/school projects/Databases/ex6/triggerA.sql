CREATE FUNCTION checkDelete()
RETURNS trigger AS $$
	DECLARE
		maxDebt real;
    BEGIN
        if OLD.Balance > 0 then
			insert into Actions values(OLD.AccountNum, 'close', CURRENT_DATE, acDate, OLD.Balance);
        else if OLD.Balance < 0 then
            RAISE EXCEPTION 'cant close debt';
        end if;
    END;
$$ language plpgsql;

CREATE  TRIGGER     moneyDelete
BEFORE  DELETE ON   AccountBalance
FOR EACH STATEMENT  EXECUTE PROCEDURE   checkDelete()
’ language plpgsql;