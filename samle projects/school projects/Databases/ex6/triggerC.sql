CREATE FUNCTION checkDate()
RETURNS trigger AS $$
	BEGIN
	    NEW.ActionDate = CURRENT_DATE
    END;
$$ language plpgsql;

CREATE FUNCTION checkDate()
RETURNS trigger AS $$
	BEGIN
	    NEW.DepositDate = CURRENT_DATE
    END;
$$ language plpgsql;

CREATE  TRIGGER     dateIsNow1
BEFORE  INSERT ON   Actions
FOR EACH STATEMENT  EXECUTE PROCEDURE   checkDate1()

CREATE  TRIGGER     dateIsNow2
BEFORE  INSERT ON   Savings
FOR EACH STATEMENT  EXECUTE PROCEDURE   checkDate2()
’ language plpgsql;