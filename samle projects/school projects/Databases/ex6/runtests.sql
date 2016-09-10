

CREATE OR REPLACE FUNCTION deleteall() RETURNS VOID AS $$
BEGIN
TRUNCATE TABLE actions RESTART IDENTITY CASCADE;
TRUNCATE TABLE accountbalance RESTART IDENTITY CASCADE;
TRUNCATE TABLE customers RESTART IDENTITY CASCADE;
TRUNCATE TABLE savings RESTART IDENTITY CASCADE;
TRUNCATE TABLE top10customers RESTART IDENTITY CASCADE;
drop table testint;
create table testint(id serial primary key, result INTEGER);

END;
$$ language plpgsql;

--TEST 1
-- Legal putting in customers.
CREATE OR REPLACE FUNCTION Test1() RETURNS BOOLEAN AS $$
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;
TestNum INTEGER :=1;

BEGIN
perform deleteall();
insert into testint(result) values(newCustomer(201486222,'alon','7421',-100));
insert into testint(result) values(newCustomer(300000000,'shani','1234',-200)); 


IF (select result from testint where id=1) = 1  THEN
	insert into testResult(result) values('success 1 on test 1');
ELSE    insert into testResult(result) values('fail 1 on test 1');

END IF;

IF (select result from testint where id=2) = 2  THEN
	insert into testResult(result) values('success 2 on test 1');
	ELSE    insert into testResult(result) values('fail 2 on test 1');

END IF;

select * into tuple from customers where accountnum=1;
CustomerRow := (1,201486222,'alon','7421','open',-100);
IF tuple = CustomerRow THEN
	insert into testResult(result) values('success 3 on test 1');
	ELSE    insert into testResult(result) values('fail 3 on test 1');

END IF;

select * into tuple from customers where accountnum=2;
CustomerRow :=(2,300000000,'shani','1234','open',-200);
IF tuple = CustomerRow THEN
	insert into testResult(result) values('success 4 on test 1');
	ELSE    insert into testResult(result) values('fail 4 on test 1');

END IF;

select * into tuple from accountbalance where accountnum=1;
AccountBalanceRow :=(1,0);
IF tuple = AccountBalanceRow THEN
	insert into testResult(result) values('success 5 on test 1');
	ELSE    insert into testResult(result) values('fail 5 on test 1');

END IF;

select * into tuple from accountbalance where accountnum=2;
AccountBalanceRow :=(2,0);
IF tuple = AccountBalanceRow THEN
	insert into testResult(result) values('success 6 on test 1');
	ELSE    insert into testResult(result) values('fail 6 on test 1');

END IF;



perform deleteall();

return true;
END;

$$ language plpgsql;


--TEST 2
-- Legal putting in customers and deleting them.
CREATE OR REPLACE FUNCTION Test2() RETURNS BOOLEAN AS $$
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN

insert into testint(result) values(newCustomer(201486222,'alon','7421',-100));
insert into testint(result) values(newCustomer(300000000,'shani','1234',-200));
insert into testint(result) values(closeCustomer(201486222));
insert into testint(result) values(closeCustomer(300000000));


IF (select result from testint where id=1) = 1  THEN

	insert into testResult(result) values('success 1 on test 2');
ELSE    insert into testResult(result) values('fail 1 on test 2');

END IF;

IF (select result from testint where id=2) = 2  THEN
	insert into testResult(result) values('success 2 on test 2');
	ELSE    insert into testResult(result) values('fail 2 on test 2');

END IF;


IF (select result from testint where id=3) = 1  THEN
	insert into testResult(result) values('success 1.1 on test 2');
ELSE    insert into testResult(result) values('fail 1.1 on test 2');

END IF;

IF (select result from testint where id=4) = 2  THEN
	insert into testResult(result) values('success 2.1 on test 2');
	ELSE    insert into testResult(result) values('fail 2.1 on test 2');

END IF;

select * into tuple from customers where accountnum=1;
CustomerRow := (1,201486222,'alon','7421','close',-100);
IF tuple = CustomerRow THEN
	insert into testResult(result) values('success 3 on test 2');
	ELSE    insert into testResult(result) values('fail 3 on test 2');

END IF;

select * into tuple from customers where accountnum=2;
CustomerRow :=(2,300000000,'shani','1234','close',-200);
IF tuple = CustomerRow THEN
	insert into testResult(result) values('success 4 on test 2');
	ELSE    insert into testResult(result) values('fail 4 on test 2');

END IF;


IF  (select COUNT(*) from accountbalance) = 0
	THEN
	insert into testResult(result) values('success 5 on test 2');
	ELSE    insert into testResult(result) values('fail 4 on test 2');

END IF;



perform deleteall();
return true;
END;

$$ language plpgsql;


--TEST 3
--Inserting a new customer to an open account
CREATE OR REPLACE FUNCTION Test3() RETURNS BOOLEAN AS $$
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN

insert into testint(result) values(newCustomer(201486222,'alon','7421',-100));
insert into testint(result) values(newCustomer(300000000,'shani','1234',-200));
insert into testint(result) values(newCustomer(201486222,'alon','7421',-100));


IF (select result from testint where id=1) = 1  THEN
	insert into testResult(result) values('success 1 on test 3');
ELSE    insert into testResult(result) values('fail 1 on test 3');

END IF;

IF (select result from testint where id=2) = 2  THEN
	insert into testResult(result) values('success 2 on test 3');
	ELSE    insert into testResult(result) values('fail 2 on test 3');

END IF;


IF (select result from testint where id=3) = -1  THEN
	insert into testResult(result) values('success 2.1 on test 3');
ELSE    insert into testResult(result) values('fail 2.1 on test 3');

END IF;



select * into tuple from customers where accountnum=1;
CustomerRow := (1,201486222,'alon','7421','open',-100);
IF tuple = CustomerRow THEN
	insert into testResult(result) values('success 3 on test 3');
	ELSE    insert into testResult(result) values('fail 3 on test 3');

END IF;

select * into tuple from customers where accountnum=2;
CustomerRow :=(2,300000000,'shani','1234','open',-200);
IF tuple = CustomerRow THEN
	insert into testResult(result) values('success 4 on test 3');
	ELSE    insert into testResult(result) values('fail 4 on test 3');

END IF;

select * into tuple from accountbalance where accountnum=1;
AccountBalanceRow :=(1,0);
IF tuple = AccountBalanceRow THEN
	insert into testResult(result) values('success 5 on test 3');
	ELSE    insert into testResult(result) values('fail 5 on test 3');

END IF;

select * into tuple from accountbalance where accountnum=2;
AccountBalanceRow :=(2,0);
IF tuple = AccountBalanceRow THEN
	insert into testResult(result) values('success 6 on test 3');
	ELSE    insert into testResult(result) values('fail 6 on test 3');

END IF;

perform deleteall();
return true;
END;

$$ language plpgsql;

--TEST 4
--Inserting a new customer to a closed account
CREATE OR REPLACE FUNCTION Test4() RETURNS BOOLEAN AS $$
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN

insert into testint(result) values(newCustomer(201486222,'alon','7421',-100));
insert into testint(result) values(newCustomer(300000000,'shani','1234',-200));
insert into testint(result) values(closeCustomer(201486222));
insert into testint(result) values(newCustomer(201486222,'aviv','9876',-300));


IF (select result from testint where id=1) = 1  THEN
	insert into testResult(result) values('success 1 on test 4');
ELSE    insert into testResult(result) values('fail 1 on test 4');

END IF;

IF (select result from testint where id=2) = 2  THEN
	insert into testResult(result) values('success 2 on test 4');
	ELSE    insert into testResult(result) values('fail 2 on test 4');

END IF;


IF (select result from testint where id=3) = 1  THEN
	insert into testResult(result) values('success 3 on test 4');
ELSE    insert into testResult(result) values('fail 3 on test 4');

END IF;


IF (select result from testint where id=4) = 1  THEN
	insert into testResult(result) values('success 3.1 on test 4');
ELSE    insert into testResult(result) values('fail 3.1 on test 4');

END IF;

select * into tuple from customers where accountnum=1;
CustomerRow :=(1,201486222,'aviv','9876','open',-300);
IF tuple =CustomerRow THEN
	insert into testResult(result) values('success 4 on test 4');
	ELSE    insert into testResult(result) values('fail 4 on test 4');

END IF;

select * into tuple from customers where accountnum=2;
CustomerRow :=(2,300000000,'shani','1234','open',-200);
IF tuple = CustomerRow THEN
	insert into testResult(result) values('success 5 on test 4');
	ELSE    insert into testResult(result) values('fail 5 on test 4');

END IF;

select * into tuple from accountbalance where accountnum=1;
AccountBalanceRow :=(1,0);
IF tuple = AccountBalanceRow THEN
	insert into testResult(result) values('success 6 on test 4');
	ELSE    insert into testResult(result) values('fail 6 on test 4');

END IF;

select * into tuple from accountbalance where accountnum=2;
AccountBalanceRow :=(2,0);
IF tuple = AccountBalanceRow THEN
	insert into testResult(result) values('success 7 on test 4');
	ELSE    insert into testResult(result) values('fail 7 on test 4');

END IF;


perform deleteall();
return true;
END;

$$ language plpgsql;

--TEST 5
--Deleting a non existant account
CREATE OR REPLACE FUNCTION Test5() RETURNS BOOLEAN AS $$
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN

insert into testint(result) values(newCustomer(201486222,'alon','7421',-100));
insert into testint(result) values(newCustomer(300000000,'shani','1234',-200));
insert into testint(result) values(closeCustomer(1));


IF (select result from testint where id=1) = 1  THEN
	insert into testResult(result) values('success 1 on test 5');
ELSE    insert into testResult(result) values('fail 1 on test 5');

END IF;

IF (select result from testint where id=2) = 2  THEN
	insert into testResult(result) values('success 2 on test 5');
	ELSE    insert into testResult(result) values('fail 2 on test 5');

END IF;


IF (select result from testint where id=3) = -1  THEN
	insert into testResult(result) values('success 3 on test 5');
ELSE    insert into testResult(result) values('fail 3 on test 5');

END IF;


select * into tuple from customers where accountnum=1;
CustomerRow :=(1,'201486222','alon','7421','open',-100);
IF tuple =CustomerRow THEN
	insert into testResult(result) values('success 4 on test 5');
	ELSE    insert into testResult(result) values('fail 4 on test 5');

END IF;

select * into tuple from customers where accountnum=2;
CustomerRow :=(2,300000000,'shani','1234','open',-200);
IF tuple = CustomerRow THEN
	insert into testResult(result) values('success 5 on test 5');
	ELSE    insert into testResult(result) values('fail 5 on test 5');

END IF;

select * into tuple from accountbalance where accountnum=1;
AccountBalanceRow :=(1,0);
IF tuple = AccountBalanceRow THEN
	insert into testResult(result) values('success 6 on test 5');
	ELSE    insert into testResult(result) values('fail 6 on test 5');

END IF;

select * into tuple from accountbalance where accountnum=2;
AccountBalanceRow :=(2,0);
IF tuple = AccountBalanceRow THEN
	insert into testResult(result) values('success 7 on test 5');
	ELSE    insert into testResult(result) values('fail 7 on test 5');

END IF;


perform deleteall();
return true;
END;

$$ language plpgsql;



--TEST 6
--Adding money to an open account
CREATE OR REPLACE FUNCTION Test6() RETURNS BOOLEAN AS $$
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN

insert into testint(result) values(newCustomer(201486222,'alon','7421',-100));
insert into testint(result) values(newCustomer(300000000,'shani','1234',-200));
insert into testint(result) values(doAction(201486222,'receive',current_date,200));


IF (select result from testint where id=1) = 1  THEN
	insert into testResult(result) values('success 1 on test 6');
ELSE    insert into testResult(result) values('fail 1 on test 6');

END IF;

IF (select result from testint where id=2) = 2  THEN
	insert into testResult(result) values('success 2 on test 6');
	ELSE    insert into testResult(result) values('fail 2 on test 6');

END IF;


IF (select result from testint where id=3) = 1  THEN
	insert into testResult(result) values('success 3 on test 6');
ELSE    insert into testResult(result) values('fail 3 on test 6');

END IF;


select * into tuple from customers where accountnum=1;
CustomerRow :=(1,'201486222','alon','7421','open',-100);
IF tuple =CustomerRow THEN
	insert into testResult(result) values('success 4 on test 6');
	ELSE    insert into testResult(result) values('fail 4 on test 6');

END IF;

select * into tuple from customers where accountnum=2;
CustomerRow :=(2,300000000,'shani','1234','open',-200);
IF tuple = CustomerRow THEN
	insert into testResult(result) values('success 5 on test 6');
	ELSE    insert into testResult(result) values('fail 5 on test 6');

END IF;

select * into tuple from accountbalance where accountnum=1;
AccountBalanceRow :=(1,200);
IF tuple = AccountBalanceRow THEN
	insert into testResult(result) values('success 6 on test 6');
	ELSE    insert into testResult(result) values('fail 6 on test 6');

END IF;

select * into tuple from accountbalance where accountnum=2;
AccountBalanceRow :=(2,0);
IF tuple = AccountBalanceRow THEN
	insert into testResult(result) values('success 7 on test 6');
	ELSE    insert into testResult(result) values('fail 7 on test 6');

END IF;


select * into tuple from actions where actionnum=1;
ActionsRow :=(1,1,'receive',current_date,200);
IF tuple = ActionsRow THEN
	insert into testResult(result) values('success 7 on test 6');
	ELSE    insert into testResult(result) values('fail 7 on test 6');

END IF;


perform deleteall();
return true;
END;

$$ language plpgsql;



--TEST 7
--Adding money to a non existant account
CREATE OR REPLACE FUNCTION Test7() RETURNS BOOLEAN AS $$
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN
 
insert into testint(result) values(newCustomer(201486222,'alon','7421',-100));
insert into testint(result) values(newCustomer(300000000,'shani','1234',-200));
insert into testint(result) values(doAction(1,'receive',current_date,200));


IF (select result from testint where id=1) = 1  THEN
	insert into testResult(result) values('success 1 on test 7');
ELSE    insert into testResult(result) values('fail 1 on test 7');

END IF;

IF (select result from testint where id=2) = 2  THEN
	insert into testResult(result) values('success 2 on test 7');
	ELSE    insert into testResult(result) values('fail 2 on test 7');

END IF;


IF (select result from testint where id=3) = -1  THEN
	insert into testResult(result) values('success 3 on test 7');
ELSE    insert into testResult(result) values('fail 3 on test 7');

END IF;


select * into tuple from customers where accountnum=1;
CustomerRow :=(1,'201486222','alon','7421','open',-100);
IF tuple =CustomerRow THEN
	insert into testResult(result) values('success 4 on test 7');
	ELSE    insert into testResult(result) values('fail 4 on test 7');

END IF;

select * into tuple from customers where accountnum=2;
CustomerRow :=(2,300000000,'shani','1234','open',-200);
IF tuple = CustomerRow THEN
	insert into testResult(result) values('success 5 on test 7');
	ELSE    insert into testResult(result) values('fail 5 on test 7');

END IF;

IF (Select COUNT(*) from actions) = 0 THEN
	insert into testResult(result) values('success 6 on test 7');
	ELSE    insert into testResult(result) values('fail 6 on test 7');

END IF;


select * into tuple from accountbalance where accountnum=1;
AccountBalanceRow :=(1,0);
IF tuple = AccountBalanceRow THEN
	insert into testResult(result) values('success 7 on test 7');
	ELSE    insert into testResult(result) values('fail 7 on test 7');

END IF;

select * into tuple from accountbalance where accountnum=2;
AccountBalanceRow :=(2,0);
IF tuple = AccountBalanceRow THEN
	insert into testResult(result) values('success 8 on test 7');
	ELSE    insert into testResult(result) values('fail 8 on test 7');

END IF;

perform deleteall();
return true;
END;

$$ language plpgsql;




--TEST 8
--Remove money from an existing account
CREATE OR REPLACE FUNCTION Test8() RETURNS BOOLEAN AS $$
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN

insert into testint(result) values(newCustomer(201486222,'alon','7421',-100));
insert into testint(result) values(newCustomer(300000000,'shani','1234',-200));
insert into testint(result) values(doAction(201486222,'receive',current_date,200));
insert into testint(result) values(doAction(201486222,'payment',current_date,-100));


IF (select result from testint where id=1) = 1  THEN
	insert into testResult(result) values('success 1 on test 8');
ELSE    insert into testResult(result) values('fail 1 on test 8');

END IF;

IF (select result from testint where id=2) = 2  THEN
	insert into testResult(result) values('success 2 on test 8');
	ELSE    insert into testResult(result) values('fail 2 on test 8');

END IF;


IF (select result from testint where id=3) = 1  THEN
	insert into testResult(result) values('success 3 on test 8');
ELSE    insert into testResult(result) values('fail 3 on test 8');

END IF;

IF (select result from testint where id=4) = 2  THEN
	insert into testResult(result) values('success 3.1 on test 8');
ELSE    insert into testResult(result) values('fail 3.1 on test 8');

END IF;


select * into tuple from customers where accountnum=1;
CustomerRow :=(1,'201486222','alon','7421','open',-100);
IF tuple =CustomerRow THEN
	insert into testResult(result) values('success 4 on test 8');
	ELSE    insert into testResult(result) values('fail 4 on test 8');

END IF;

select * into tuple from customers where accountnum=2;
CustomerRow :=(2,300000000,'shani','1234','open',-200);
IF tuple = CustomerRow THEN
	insert into testResult(result) values('success 5 on test 8');
	ELSE    insert into testResult(result) values('fail 5 on test 8');

END IF;


select * into tuple from accountbalance where accountnum=1;
AccountBalanceRow :=(1,100);
IF tuple = AccountBalanceRow THEN
	insert into testResult(result) values('success 7 on test 8');
	ELSE    insert into testResult(result) values('fail 7 on test 8');

END IF;

select * into tuple from accountbalance where accountnum=2;
AccountBalanceRow :=(2,0);
IF tuple = AccountBalanceRow THEN
	insert into testResult(result) values('success 8 on test 8');
	ELSE    insert into testResult(result) values('fail 8 on test 8');

END IF;

select * into tuple from actions where actionnum=1;
Actionsrow :=(1,1,'receive',current_date,200);
IF tuple = ActionsRow THEN
	insert into testResult(result) values('success 8 on test 8');
	ELSE    insert into testResult(result) values('fail 8 on test 8');
END IF;


select * into tuple from actions where actionnum=2;
Actionsrow :=(2,1,'payment',current_date,-100);
IF tuple = ActionsRow THEN
	insert into testResult(result) values('success 8 on test 8');
	ELSE    insert into testResult(result) values('fail 8 on test 8');

END IF;

perform deleteall();
return true;
END;

$$ language plpgsql;


--TEST 9
--Removing money from a closed account
CREATE OR REPLACE FUNCTION Test9() RETURNS BOOLEAN AS $$
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN

insert into testint(result) values(newCustomer(201486222,'alon','7421',-100));
insert into testint(result) values(newCustomer(300000000,'shani','1234',-200));
insert into testint(result) values(closeCustomer(201486222));
insert into testint(result) values(doAction(1,'payment',current_date,200));


IF (select result from testint where id=1) = 1  THEN
	insert into testResult(result) values('success 1 on test 9');
ELSE    insert into testResult(result) values('fail 1 on test 9');

END IF;

IF (select result from testint where id=2) = 2  THEN
	insert into testResult(result) values('success 2 on test 9');
	ELSE    insert into testResult(result) values('fail 2 on test 9');

END IF;


IF (select result from testint where id=3) = 1  THEN
	insert into testResult(result) values('success 2.1 on test 9');
	ELSE    insert into testResult(result) values('fail 2.1 on test 9');

END IF;


IF (select result from testint where id=4) = -1  THEN
	insert into testResult(result) values('success 3 on test 9');
ELSE    insert into testResult(result) values('fail 3 on test 9');

END IF;


select * into tuple from customers where accountnum=1;
CustomerRow :=(1,'201486222','alon','7421','close',-100);
IF tuple =CustomerRow THEN
	insert into testResult(result) values('success 4 on test 9');
	ELSE    insert into testResult(result) values('fail 4 on test 9');

END IF;

select * into tuple from customers where accountnum=2;
CustomerRow :=(2,300000000,'shani','1234','open',-200);
IF tuple = CustomerRow THEN
	insert into testResult(result) values('success 5 on test 9');
	ELSE    insert into testResult(result) values('fail 5 on test 9');

END IF;

IF (Select COUNT(*) from actions) = 0 THEN
	insert into testResult(result) values('success 6 on test 9');
	ELSE    insert into testResult(result) values('fail 6 on test 9');

END IF;

IF (Select COUNT(*) from accountbalance) = 1 THEN
	insert into testResult(result) values('success 6 on test 9');
	ELSE    insert into testResult(result) values('fail 6 on test 9');

END IF;




perform deleteall();
return true;
END;

$$ language plpgsql;





--TEST 10
--Opening saving account for existing account
CREATE OR REPLACE FUNCTION Test10() RETURNS BOOLEAN AS $$
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN

insert into testint(result) values(newCustomer(201486222,'alon','7421',-100));
insert into testint(result) values(newCustomer(300000000,'shani','1234',-200));
insert into testint(result) values(doAction(201486222,'receive',current_date,200));
insert into testint(result) values(newSaving(201486222,100,current_date,5,0.5));
insert into testint(result) values(newSaving(201486222,50,current_date,6,0.6));


IF (select result from testint where id=1) = 1  THEN
	insert into testResult(result) values('success 1 on test 10');
ELSE    insert into testResult(result) values('fail 1 on test 10');

END IF;

IF (select result from testint where id=2) = 2  THEN
	insert into testResult(result) values('success 2 on test 10');
	ELSE    insert into testResult(result) values('fail 2 on test 10');

END IF;


IF (select result from testint where id=3) = 1  THEN
	insert into testResult(result) values('success 3 on test 10');
ELSE    insert into testResult(result) values('fail 3 on test 10');

END IF;

IF (select result from testint where id=4) = 1  THEN
	insert into testResult(result) values('success 3.1 on test 10');
ELSE    insert into testResult(result) values('fail 3.1 on test 10');

END IF;


IF (select result from testint where id=5) = 2  THEN
	insert into testResult(result) values('success 3.2 on test 10');
ELSE    insert into testResult(result) values('fail 3.2 on test 10');

END IF;


select * into tuple from customers where accountnum=1;
CustomerRow :=(1,'201486222','alon','7421','open',-100);
IF tuple =CustomerRow THEN
	insert into testResult(result) values('success 4 on test 10');
	ELSE    insert into testResult(result) values('fail 4 on test 10');

END IF;

select * into tuple from customers where accountnum=2;
CustomerRow :=(2,300000000,'shani','1234','open',-200);
IF tuple = CustomerRow THEN
	insert into testResult(result) values('success 5 on test 10');
	ELSE    insert into testResult(result) values('fail 5 on test 10');

END IF;


select * into tuple from accountbalance where accountnum=1;
AccountBalanceRow :=(1,50);
IF tuple = AccountBalanceRow THEN
	insert into testResult(result) values('success 7 on test 10');
	ELSE    insert into testResult(result) values('fail 7 on test 10');

END IF;

select * into tuple from accountbalance where accountnum=2;
AccountBalanceRow :=(2,0);
IF tuple = AccountBalanceRow THEN
	insert into testResult(result) values('success 8 on test 10');
	ELSE    insert into testResult(result) values('fail 8 on test 10');

END IF;

select * into tuple from actions where actionnum=1;
Actionsrow :=(1,1,'receive',current_date,200);
IF tuple = ActionsRow THEN
	insert into testResult(result) values('success 9 on test 10');
	ELSE    insert into testResult(result) values('fail 9 on test 10');
END IF;


select * into tuple from actions where actionnum=2;
Actionsrow :=(2,1,'saving',current_date,-100);
IF tuple = ActionsRow THEN
	insert into testResult(result) values('success 10 on test 10');
	ELSE    insert into testResult(result) values('fail 10 on test 10');

END IF;

select * into tuple from actions where actionnum=3;
Actionsrow :=(3,1,'saving',current_date,-50);
IF tuple = ActionsRow THEN
	insert into testResult(result) values('success 10.1 on test 10');
	ELSE    insert into testResult(result) values('fail 10.1 on test 10');

END IF;

select * into tuple from savings where savingnum=1;
Savingsrow :=(1,1,100,current_date,5,0.5);
IF tuple = SavingsRow THEN
	insert into testResult(result) values('success 11 on test 10');
	ELSE    insert into testResult(result) values('fail 11 on test 10');

END IF;

select * into tuple from savings where savingnum=2;
Savingsrow :=(2,1,50,current_date,6,0.6);
IF tuple = SavingsRow THEN
	insert into testResult(result) values('success 11.1 on test 10');
	ELSE    insert into testResult(result) values('fail 11.1 on test 10');

END IF;

perform deleteall();
return true;
END;

$$ language plpgsql;



--TEST 11
--Opening saving account non-existing account
CREATE OR REPLACE FUNCTION Test11() RETURNS BOOLEAN AS $$
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN

insert into testint(result) values(newCustomer(201486222,'alon','7421',-100));
insert into testint(result) values(newCustomer(300000000,'shani','1234',-200));
insert into testint(result) values(newSaving(5,100,current_date,5,0.5));




IF (select result from testint where id=1) = 1  THEN
	insert into testResult(result) values('success 1 on test 11');
ELSE    insert into testResult(result) values('fail 1 on test 11');

END IF;

IF (select result from testint where id=2) = 2  THEN
	insert into testResult(result) values('success 2 on test 11');
	ELSE    insert into testResult(result) values('fail 2 on test 11');

END IF;


IF (select result from testint where id=3) = -1  THEN
	insert into testResult(result) values('success 2.1 on test 11');
	ELSE    insert into testResult(result) values('fail 2.1 on test 11');

END IF;


select * into tuple from customers where accountnum=1;
CustomerRow :=(1,'201486222','alon','7421','open',-100);
IF tuple =CustomerRow THEN
	insert into testResult(result) values('success 4 on test 11');
	ELSE    insert into testResult(result) values('fail 4 on test 11');

END IF;

select * into tuple from customers where accountnum=2;
CustomerRow :=(2,300000000,'shani','1234','open',-200);
IF tuple = CustomerRow THEN
	insert into testResult(result) values('success 5 on test 11');
	ELSE    insert into testResult(result) values('fail 5 on test 11');

END IF;

select * into tuple from accountBalance where accountnum=1;
accountbalancerow :=(1,0);
IF tuple = accountbalanceRow THEN
	insert into testResult(result) values('success 6 on test 11');
	ELSE    insert into testResult(result) values('fail 6 on test 11');

END IF;

select * into tuple from accountBalance where accountnum=2;
accountbalancerow :=(2,0);
IF tuple = accountbalancerow THEN
	insert into testResult(result) values('success 7 on test 11');
	ELSE    insert into testResult(result) values('fail 7 on test 11');

END IF;


IF (Select COUNT(*) from actions) = 0 THEN
	insert into testResult(result) values('success 8 on test 11');
	ELSE    insert into testResult(result) values('fail 8 on test 11');

END IF;


IF (Select COUNT(*) from savings) = 0 THEN
	insert into testResult(result) values('success 9 on test 11');
	ELSE    insert into testResult(result) values('fail 9 on test 11');

END IF;

perform deleteall();
return true;
END;

$$ language plpgsql;




--TEST 12
--close existing customer with a savings account
CREATE OR REPLACE FUNCTION Test12() RETURNS BOOLEAN AS $$
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN

insert into testint(result) values(newCustomer(201486222,'alon','7421',-100));
insert into testint(result) values(newCustomer(300000000,'shani','1234',-200));
insert into testint(result) values(doAction(201486222,'receive',current_date,200));
insert into testint(result) values(newSaving(201486222,50,current_date,5,0.5));
insert into testint(result) values(closeCustomer(201486222));


IF (select result from testint where id=1) = 1  THEN
	insert into testResult(result) values('success 1 on test 12');
ELSE    insert into testResult(result) values('fail 1 on test 12');

END IF;

IF (select result from testint where id=2) = 2  THEN
	insert into testResult(result) values('success 2 on test 12');
	ELSE    insert into testResult(result) values('fail 2 on test 12');

END IF;


IF (select result from testint where id=3) = 1  THEN
	insert into testResult(result) values('success 3 on test 12');
ELSE    insert into testResult(result) values('fail 3 on test 12');

END IF;

IF (select result from testint where id=4) = 1  THEN
	insert into testResult(result) values('success 3.1 on test 12');
ELSE    insert into testResult(result) values('fail 3.1 on test 12');

END IF;

IF (select result from testint where id=5) = 1  THEN
	insert into testResult(result) values('success 3.1 on test 12');
ELSE    insert into testResult(result) values('fail 3.1 on test 12');

END IF;


select * into tuple from customers where accountnum=1;
CustomerRow :=(1,'201486222','alon','7421','close',-100);
IF tuple =CustomerRow THEN
	insert into testResult(result) values('success 4 on test 12');
	ELSE    insert into testResult(result) values('fail 4 on test 12');

END IF;

select * into tuple from customers where accountnum=2;
CustomerRow :=(2,300000000,'shani','1234','open',-200);
IF tuple = CustomerRow THEN
	insert into testResult(result) values('success 5 on test 12');
	ELSE    insert into testResult(result) values('fail 5 on test 12');

END IF;


select * into tuple from accountbalance where accountnum=2;
AccountBalanceRow :=(2,0);
IF tuple = AccountBalanceRow THEN
	insert into testResult(result) values('success 8 on test 12');
	ELSE    insert into testResult(result) values('fail 8 on test 12');

END IF;

select * into tuple from actions where actionnum=1;
Actionsrow :=(1,1,'receive',current_date,200);
IF tuple = ActionsRow THEN
	insert into testResult(result) values('success 8.1 on test 12');
	ELSE    insert into testResult(result) values('fail 8.1 on test 12');
END IF;


select * into tuple from actions where actionnum=2;
Actionsrow :=(2,1,'saving',current_date,-50);
IF tuple = ActionsRow THEN
	insert into testResult(result) values('success 9 on test 12');
	ELSE    insert into testResult(result) values('fail 9 on test 12');

END IF;

IF (Select COUNT(*) from savings) = 0 THEN
	insert into testResult(result) values('success 10 on test 12');
	ELSE    insert into testResult(result) values('fail 10 on test 12');

END IF;

IF (Select COUNT(*) from accountbalance) = 1 THEN
	insert into testResult(result) values('success 10.1 on test 12');
	ELSE    insert into testResult(result) values('fail 10.1 on test 12');

END IF;

perform deleteall();
return true;
END;

$$ language plpgsql;

--TEST 13
--Close account with positive balance
CREATE OR REPLACE FUNCTION Test13() RETURNS BOOLEAN AS $$
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN

insert into testint(result) values(newCustomer(201486222,'alon','7421',-100));
insert into testint(result) values(newCustomer(300000000,'shani','1234',-200));
insert into testint(result) values(doAction(201486222,'receive',current_date,200));
insert into testint(result) values(closeCustomer(201486222));


IF (select result from testint where id=1) = 1  THEN
	insert into testResult(result) values('success 1 on test 13');
ELSE    insert into testResult(result) values('fail 1 on test 13');

END IF;

IF (select result from testint where id=2) = 2  THEN
	insert into testResult(result) values('success 2 on test 13');
	ELSE    insert into testResult(result) values('fail 2 on test 13');

END IF;


IF (select result from testint where id=3) = 1  THEN
	insert into testResult(result) values('success 3 on test 13');
ELSE    insert into testResult(result) values('fail 3 on test 13');

END IF;

IF (select result from testint where id=4) = 1  THEN
	insert into testResult(result) values('success 3.1 on test 13');
ELSE    insert into testResult(result) values('fail 3.1 on test 13');

END IF;



select * into tuple from customers where accountnum=1;
CustomerRow :=(1,'201486222','alon','7421','close',-100);
IF tuple =CustomerRow THEN
	insert into testResult(result) values('success 4 on test 13');
	ELSE    insert into testResult(result) values('fail 4 on test 13');

END IF;

select * into tuple from customers where accountnum=2;
CustomerRow :=(2,300000000,'shani','1234','open',-200);
IF tuple = CustomerRow THEN
	insert into testResult(result) values('success 5 on test 13');
	ELSE    insert into testResult(result) values('fail 5 on test 13');

END IF;


select * into tuple from accountbalance where accountnum=2;
AccountBalanceRow :=(2,0);
IF tuple = AccountBalanceRow THEN
	insert into testResult(result) values('success 8 on test 13');
	ELSE    insert into testResult(result) values('fail 8 on test 13');

END IF;

select * into tuple from actions where actionnum=1;
Actionsrow :=(1,1,'receive',current_date,200);
IF tuple = ActionsRow THEN
	insert into testResult(result) values('success 8.1 on test 13');
	ELSE    insert into testResult(result) values('fail 8.1 on test 13');
END IF;


select * into tuple from actions where actionnum=2;
Actionsrow :=(2,1,'close',current_date,-200);
IF tuple = ActionsRow THEN
	insert into testResult(result) values('success 9 on test 13');
	ELSE    insert into testResult(result) values('fail 9 on test 13');

END IF;

IF (Select COUNT(*) from accountbalance) = 1 THEN
	insert into testResult(result) values('success 10.1 on test 13');
	ELSE    insert into testResult(result) values('fail 10.1 on test 13');

END IF;

perform deleteall();
return true;
END;

$$ language plpgsql;
--TEST 14
--Close account with 0 balance
CREATE OR REPLACE FUNCTION Test14() RETURNS BOOLEAN AS $$
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN

insert into testint(result) values(newCustomer(201486222,'alon','7421',-100));
insert into testint(result) values(newCustomer(300000000,'shani','1234',-200));
insert into testint(result) values(closeCustomer(201486222));


IF (select result from testint where id=1) = 1  THEN
	insert into testResult(result) values('success 1 on test 14');
ELSE    insert into testResult(result) values('fail 1 on test 14');

END IF;

IF (select result from testint where id=2) = 2  THEN
	insert into testResult(result) values('success 2 on test 14');
	ELSE    insert into testResult(result) values('fail 2 on test 14');

END IF;


IF (select result from testint where id=3) = 1  THEN
	insert into testResult(result) values('success 3 on test 14');
ELSE    insert into testResult(result) values('fail 3 on test 14');

END IF;


select * into tuple from customers where accountnum=1;
CustomerRow :=(1,'201486222','alon','7421','close',-100);
IF tuple =CustomerRow THEN
	insert into testResult(result) values('success 4 on test 14');
	ELSE    insert into testResult(result) values('fail 4 on test 14');

END IF;

select * into tuple from customers where accountnum=2;
CustomerRow :=(2,300000000,'shani','1234','open',-200);
IF tuple = CustomerRow THEN
	insert into testResult(result) values('success 5 on test 14');
	ELSE    insert into testResult(result) values('fail 5 on test 14');

END IF;


select * into tuple from accountbalance where accountnum=2;
AccountBalanceRow :=(2,0);
IF tuple = AccountBalanceRow THEN
	insert into testResult(result) values('success 8 on test 14');
	ELSE    insert into testResult(result) values('fail 8 on test 14');

END IF;



IF (Select COUNT(*) from accountbalance) = 1 THEN
	insert into testResult(result) values('success 10.1 on test 14');
	ELSE    insert into testResult(result) values('fail 10.1 on test 14');

END IF;

IF (Select COUNT(*) from actions) = 0 THEN
	insert into testResult(result) values('success 11 on test 14');
	ELSE    insert into testResult(result) values('fail 11 on test 14');

END IF;

perform deleteall();
return true;
END;

$$ language plpgsql;

--TEST 15
--Close account with a negative balance
CREATE OR REPLACE FUNCTION Test15() RETURNS BOOLEAN AS $$
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN


insert into testint(result) values(newCustomer(201486222,'alon','7421',-100));
insert into testint(result) values(newCustomer(300000000,'shani','1234',-200));
insert into testint(result) values(doAction(201486222,'payment',current_date,-50));
insert into testint(result) values(closeCustomer(201486222));

insert into testResult(result) values('fail 1 on test 15');

perform deleteall();
return true;

EXCEPTION
WHEN others THEN
	insert into testResult(result) values('success 1 on test 15');
	perform deleteall();
	return true;
END;

$$ language plpgsql;

--TEST 16
-- Payment under overdraft
CREATE OR REPLACE FUNCTION Test16() RETURNS BOOLEAN AS $$
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN

insert into testint(result) values(newCustomer(201486222,'alon','7421',-100));
insert into testint(result) values(newCustomer(300000000,'shani','1234',-200));
insert into testint(result) values(doAction(201486222,'payment',current_date,-101));

insert into testResult(result) values('fail 1 on test 16');

perform deleteall();
return true;

EXCEPTION
WHEN others THEN
	insert into testResult(result) values('success 1 on test 16');
	perform deleteall();
	return true;
END;

$$ language plpgsql;



--TEST 17
-- Payment just in overdraft
CREATE OR REPLACE FUNCTION Test17() RETURNS BOOLEAN AS $$
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN

insert into testint(result) values(newCustomer(201486222,'alon','7421',-100));
insert into testint(result) values(newCustomer(300000000,'shani','1234',-200));
insert into testint(result) values(doAction(201486222,'payment',current_date,-100));

insert into testResult(result) values('success 1 on test 17');

perform deleteall();
return true;

EXCEPTION
WHEN others THEN
	insert into testResult(result) values('fail 1 on test 17');
	perform deleteall();
	return true;
END;

$$ language plpgsql;


--TEST 18
-- Savings under overdraft
CREATE OR REPLACE FUNCTION Test18() RETURNS BOOLEAN AS $$
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN


insert into testint(result) values(newCustomer(201486222,'alon','7421',-100));
insert into testint(result) values(newCustomer(300000000,'shani','1234',-200));
insert into testint(result) values(newSaving(201486222,101,current_date,5,0.5));

insert into testResult(result) values('fail 1 on test 18');

perform deleteall();
return true;

EXCEPTION
WHEN others THEN
	insert into testResult(result) values('success 1 on test 18');
	perform deleteall();
	return true;
END;

$$ language plpgsql;


--TEST 19
-- Receive action with wrong date
CREATE OR REPLACE FUNCTION Test19() RETURNS BOOLEAN AS $$
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN


insert into testint(result) values(newCustomer(201486222,'alon','7421',-100));
insert into testint(result) values(newCustomer(300000000,'shani','1234',-200));
insert into testint(result) values(doAction(201486222,'receive','1988-10-30',200));
insert into testint(result) values(closeCustomer(201486222));


IF (select result from testint where id=1) = 1  THEN
	insert into testResult(result) values('success 1 on test 19');
ELSE    insert into testResult(result) values('fail 1 on test 19');

END IF;

IF (select result from testint where id=2) = 2  THEN
	insert into testResult(result) values('success 2 on test 19');
	ELSE    insert into testResult(result) values('fail 2 on test 19');

END IF;


IF (select result from testint where id=3) = 1  THEN
	insert into testResult(result) values('success 3 on test 19');
ELSE    insert into testResult(result) values('fail 3 on test 19');

END IF;

IF (select result from testint where id=4) = 1  THEN
	insert into testResult(result) values('success 3.1 on test 19');
ELSE    insert into testResult(result) values('fail 3.1 on test 19');

END IF;



select * into tuple from customers where accountnum=1;
CustomerRow :=(1,'201486222','alon','7421','close',-100);
IF tuple =CustomerRow THEN
	insert into testResult(result) values('success 4 on test 19');
	ELSE    insert into testResult(result) values('fail 4 on test 19');

END IF;

select * into tuple from customers where accountnum=2;
CustomerRow :=(2,300000000,'shani','1234','open',-200);
IF tuple = CustomerRow THEN
	insert into testResult(result) values('success 5 on test 19');
	ELSE    insert into testResult(result) values('fail 5 on test 19');

END IF;


select * into tuple from accountbalance where accountnum=2;
AccountBalanceRow :=(2,0);
IF tuple = AccountBalanceRow THEN
	insert into testResult(result) values('success 8 on test 19');
	ELSE    insert into testResult(result) values('fail 8 on test 19');

END IF;

select * into tuple from actions where actionnum=1;
Actionsrow :=(1,1,'receive',current_date,200);
IF tuple = ActionsRow THEN
	insert into testResult(result) values('success 8.1 on test 19');
	ELSE    insert into testResult(result) values('fail 8.1 on test 19');
END IF;


select * into tuple from actions where actionnum=2;
Actionsrow :=(2,1,'close',current_date,-200);
IF tuple = ActionsRow THEN
	insert into testResult(result) values('success 9 on test 19');
	ELSE    insert into testResult(result) values('fail 9 on test 19');

END IF;

IF (Select COUNT(*) from accountbalance) = 1 THEN
	insert into testResult(result) values('success 10.1 on test 19');
	ELSE    insert into testResult(result) values('fail 10.1 on test 19');

END IF;

perform deleteall();
return true;
END;

$$ language plpgsql;



--TEST 20
--Opening saving account with wrong date
CREATE OR REPLACE FUNCTION Test20() RETURNS BOOLEAN AS $$
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN


insert into testint(result) values(newCustomer(201486222,'alon','7421',-100));
insert into testint(result) values(newCustomer(300000000,'shani','1234',-200));
insert into testint(result) values(doAction(201486222,'receive',current_date,200));
insert into testint(result) values(newSaving(201486222,100,'1988-10-30',5,0.5));


IF (select result from testint where id=1) = 1  THEN
	insert into testResult(result) values('success 1 on test 20');
ELSE    insert into testResult(result) values('fail 1 on test 20');

END IF;

IF (select result from testint where id=2) = 2  THEN
	insert into testResult(result) values('success 2 on test 20');
	ELSE    insert into testResult(result) values('fail 2 on test 20');

END IF;


IF (select result from testint where id=3) = 1  THEN
	insert into testResult(result) values('success 3 on test 20');
ELSE    insert into testResult(result) values('fail 3 on test 20');

END IF;

IF (select result from testint where id=4) = 1  THEN
	insert into testResult(result) values('success 3.1 on test 20');
ELSE    insert into testResult(result) values('fail 3.1 on test 20');

END IF;


select * into tuple from customers where accountnum=1;
CustomerRow :=(1,'201486222','alon','7421','open',-100);
IF tuple =CustomerRow THEN
	insert into testResult(result) values('success 4 on test 20');
	ELSE    insert into testResult(result) values('fail 4 on test 20');

END IF;

select * into tuple from customers where accountnum=2;
CustomerRow :=(2,300000000,'shani','1234','open',-200);
IF tuple = CustomerRow THEN
	insert into testResult(result) values('success 5 on test 20');
	ELSE    insert into testResult(result) values('fail 5 on test 20');

END IF;


select * into tuple from accountbalance where accountnum=1;
AccountBalanceRow :=(1,100);
IF tuple = AccountBalanceRow THEN
	insert into testResult(result) values('success 7 on test 20');
	ELSE    insert into testResult(result) values('fail 7 on test 20');

END IF;

select * into tuple from accountbalance where accountnum=2;
AccountBalanceRow :=(2,0);
IF tuple = AccountBalanceRow THEN
	insert into testResult(result) values('success 8 on test 20');
	ELSE    insert into testResult(result) values('fail 8 on test 20');

END IF;

select * into tuple from actions where actionnum=1;
Actionsrow :=(1,1,'receive',current_date,200);
IF tuple = ActionsRow THEN
	insert into testResult(result) values('success 9 on test 20');
	ELSE    insert into testResult(result) values('fail 9 on test 20');
END IF;


select * into tuple from actions where actionnum=2;
Actionsrow :=(2,1,'saving',current_date,-100);
IF tuple = ActionsRow THEN
	insert into testResult(result) values('success 10 on test 20');
	ELSE    insert into testResult(result) values('fail 10 on test 20');

END IF;

select * into tuple from savings where savingnum=1;
Savingsrow :=(1,1,100,current_date,5,0.5);
IF tuple = SavingsRow THEN
	insert into testResult(result) values('success 11 on test 20');
	ELSE    insert into testResult(result) values('fail 11 on test 20');

END IF;

perform deleteall();
return true;
END;

$$ language plpgsql;





--TEST 21
--Opening saving account and payment with wrong date
CREATE OR REPLACE FUNCTION Test21() RETURNS BOOLEAN AS $$
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN


insert into testint(result) values(newCustomer(201486222,'alon','7421',-200));
insert into testint(result) values(newCustomer(300000000,'shani','1234',-200));
insert into testint(result) values(doAction(201486222,'payment','1988-10-30',-50));
insert into testint(result) values(newSaving(201486222,100,'1988-10-30',5,0.5));


IF (select result from testint where id=1) = 1  THEN
	insert into testResult(result) values('success 1 on test 21');
ELSE    insert into testResult(result) values('fail 1 on test 21');

END IF;

IF (select result from testint where id=2) = 2  THEN
	insert into testResult(result) values('success 2 on test 21');
	ELSE    insert into testResult(result) values('fail 2 on test 21');

END IF;


IF (select result from testint where id=3) = 1  THEN
	insert into testResult(result) values('success 3 on test 21');
ELSE    insert into testResult(result) values('fail 3 on test 21');

END IF;

IF (select result from testint where id=4) = 1  THEN
	insert into testResult(result) values('success 3.1 on test 21');
ELSE    insert into testResult(result) values('fail 3.1 on test 21');

END IF;


select * into tuple from customers where accountnum=1;
CustomerRow :=(1,'201486222','alon','7421','open',-200);
IF tuple =CustomerRow THEN
	insert into testResult(result) values('success 4 on test 21');
	ELSE    insert into testResult(result) values('fail 4 on test 21');

END IF;

select * into tuple from customers where accountnum=2;
CustomerRow :=(2,300000000,'shani','1234','open',-200);
IF tuple = CustomerRow THEN
	insert into testResult(result) values('success 5 on test 21');
	ELSE    insert into testResult(result) values('fail 5 on test 21');

END IF;


select * into tuple from accountbalance where accountnum=1;
AccountBalanceRow :=(1,-150);
IF tuple = AccountBalanceRow THEN
	insert into testResult(result) values('success 7 on test 21');
	ELSE    insert into testResult(result) values('fail 7 on test 21');

END IF;

select * into tuple from accountbalance where accountnum=2;
AccountBalanceRow :=(2,0);
IF tuple = AccountBalanceRow THEN
	insert into testResult(result) values('success 8 on test 21');
	ELSE    insert into testResult(result) values('fail 8 on test 21');

END IF;

select * into tuple from actions where actionnum=1;
Actionsrow :=(1,1,'payment',current_date,-50);
IF tuple = ActionsRow THEN
	insert into testResult(result) values('success 9 on test 21');
	ELSE    insert into testResult(result) values('fail 9 on test 21');
END IF;


select * into tuple from actions where actionnum=2;
Actionsrow :=(2,1,'saving',current_date,-100);
IF tuple = ActionsRow THEN
	insert into testResult(result) values('success 10 on test 21');
	ELSE    insert into testResult(result) values('fail 10 on test 21');

END IF;

select * into tuple from savings where savingnum=1;
Savingsrow :=(1,1,100,current_date,5,0.5);
IF tuple = SavingsRow THEN
	insert into testResult(result) values('success 11 on test 21');
	ELSE    insert into testResult(result) values('fail 11 on test 21');

END IF;

perform deleteall();
return true;
END;

$$ language plpgsql;






--TEST 22
--Adding money to a few clients and checking top 10.
CREATE OR REPLACE FUNCTION Test22() RETURNS BOOLEAN AS $$
 
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN


insert into testint(result) values(newCustomer(201486222,'alon','7421',-200));
insert into testint(result) values(newCustomer(300000000,'shani','1234',-200));
insert into testint(result) values(newCustomer(439872098,'dov','1543',-200));
insert into testint(result) values(doAction(201486222,'receive',current_date,50));

select * into tuple from Top10Customers where accountNum=1;
Top10CustomersRow :=(1,50);
IF tuple = Top10CustomersRow THEN
	insert into testResult(result) values('success 1 on test 22');
	ELSE    insert into testResult(result) values('fail 1 on test 22');

END IF;

IF (Select COUNT(*) From Top10Customers) = 1 THEN
	insert into testResult(result) values('success 2 on test 22');
	ELSE    insert into testResult(result) values('fail 2 on test 22');

END IF;


insert into testint(result) values(doAction(300000000,'receive',current_date,100));

select * into tuple from Top10Customers where accountNum=1;
Top10CustomersRow :=(1,50);
IF tuple = Top10CustomersRow THEN
	insert into testResult(result) values('success 1.1 on test 22');
	ELSE    insert into testResult(result) values('fail 1.1 on test 22');

END IF;


select * into tuple from Top10Customers where accountNum=2;
Top10CustomersRow :=(2,100);
IF tuple = Top10CustomersRow THEN
	insert into testResult(result) values('success 2.1 on test 22');
	ELSE    insert into testResult(result) values('fail 2.1 on test 22');

END IF;

IF (Select COUNT(*) From Top10Customers) = 2 THEN
	insert into testResult(result) values('success 3.1 on test 22');
	ELSE    insert into testResult(result) values('fail 3.1 on test 22');

END IF;


insert into testint(result) values(doAction(439872098,'receive',current_date,200));


select * into tuple from Top10Customers where accountNum=1;
Top10CustomersRow :=(1,50);
IF tuple = Top10CustomersRow THEN
	insert into testResult(result) values('success 1.2 on test 22');
	ELSE    insert into testResult(result) values('fail 1.2 on test 22');

END IF;


select * into tuple from Top10Customers where accountNum=2;
Top10CustomersRow :=(2,100);
IF tuple = Top10CustomersRow THEN
	insert into testResult(result) values('success 2.2 on test 22');
	ELSE    insert into testResult(result) values('fail 2.2 on test 22');

END IF;

select * into tuple from Top10Customers where accountNum=3;
Top10CustomersRow :=(3,200);
IF tuple = Top10CustomersRow THEN
	insert into testResult(result) values('success 3.2 on test 22');
	ELSE    insert into testResult(result) values('fail 3.2 on test 22');

END IF;

IF (Select COUNT(*) From Top10Customers) = 3 THEN
	insert into testResult(result) values('success 4.2 on test 22');
	ELSE    insert into testResult(result) values('fail 4.2 on test 22');

END IF;


perform deleteall();
return true;
END;

$$ language plpgsql;


--TEST 23
--Adding money to more than 10 clients with different balances
CREATE OR REPLACE FUNCTION Test23() RETURNS BOOLEAN AS $$
 
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN

insert into testint(result) values(newCustomer(1,'alon','7421',-200));
insert into testint(result) values(newCustomer(2,'alon','7421',-200));
insert into testint(result) values(newCustomer(3,'alon','7421',-200));
insert into testint(result) values(newCustomer(4,'alon','7421',-200));
insert into testint(result) values(newCustomer(5,'alon','7421',-200));
insert into testint(result) values(newCustomer(6,'alon','7421',-200));
insert into testint(result) values(newCustomer(7,'alon','7421',-200));
insert into testint(result) values(newCustomer(8,'alon','7421',-200));
insert into testint(result) values(newCustomer(9,'alon','7421',-200));
insert into testint(result) values(newCustomer(10,'alon','7421',-200));
insert into testint(result) values(newCustomer(11,'alon','7421',-200));

insert into testint(result) values(doAction(1,'receive',current_date,1));
insert into testint(result) values(doAction(2,'receive',current_date,2));
insert into testint(result) values(doAction(3,'receive',current_date,3));
insert into testint(result) values(doAction(4,'receive',current_date,4));
insert into testint(result) values(doAction(5,'receive',current_date,5));
insert into testint(result) values(doAction(6,'receive',current_date,6));
insert into testint(result) values(doAction(7,'receive',current_date,7));
insert into testint(result) values(doAction(8,'receive',current_date,8));
insert into testint(result) values(doAction(9,'receive',current_date,9));
insert into testint(result) values(doAction(10,'receive',current_date,10));
insert into testint(result) values(doAction(11,'receive',current_date,11));


IF (Select COUNT(*) From Top10Customers) = 10 THEN
	insert into testResult(result) values('success 1 on test 23');
	ELSE    insert into testResult(result) values('fail 1 on test 23');

END IF;

select * into tuple from Top10Customers where accountNum=4;
Top10CustomersRow :=(4,4);
IF tuple = Top10CustomersRow THEN
	insert into testResult(result) values('success 2 on test 23');
	ELSE    insert into testResult(result) values('fail 2 on test 23');

END IF;

select * into tuple from Top10Customers where accountNum=11;
Top10CustomersRow :=(11,11);
IF tuple = Top10CustomersRow THEN
	insert into testResult(result) values('success 3 on test 23');
	ELSE    insert into testResult(result) values('fail 3 on test 23');

END IF;

IF NOT EXISTS(Select * from Top10Customers where accountNum=1) THEN
	insert into testResult(result) values('success 4 on test 23');
	ELSE    insert into testResult(result) values('fail 4 on test 23');

END IF;


perform deleteall();
return true;
END;

$$ language plpgsql;




--TEST 24
--Adding money to more than 10 clients with some same balances
CREATE OR REPLACE FUNCTION Test24() RETURNS BOOLEAN AS $$
 
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN

insert into testint(result) values(newCustomer(1,'alon','7421',-200));
insert into testint(result) values(newCustomer(2,'alon','7421',-200));
insert into testint(result) values(newCustomer(3,'alon','7421',-200));
insert into testint(result) values(newCustomer(4,'alon','7421',-200));
insert into testint(result) values(newCustomer(5,'alon','7421',-200));
insert into testint(result) values(newCustomer(6,'alon','7421',-200));
insert into testint(result) values(newCustomer(7,'alon','7421',-200));
insert into testint(result) values(newCustomer(8,'alon','7421',-200));
insert into testint(result) values(newCustomer(9,'alon','7421',-200));
insert into testint(result) values(newCustomer(10,'alon','7421',-200));
insert into testint(result) values(newCustomer(11,'alon','7421',-200));
insert into testint(result) values(newCustomer(12,'alon','7421',-200));
insert into testint(result) values(newCustomer(13,'alon','7421',-200));
insert into testint(result) values(newCustomer(14,'alon','7421',-200));
insert into testint(result) values(newCustomer(15,'alon','7421',-200));
insert into testint(result) values(newCustomer(16,'alon','7421',-200));


insert into testint(result) values(doAction(1,'receive',current_date,1));
insert into testint(result) values(doAction(2,'receive',current_date,2));
insert into testint(result) values(doAction(3,'receive',current_date,3));
insert into testint(result) values(doAction(4,'receive',current_date,4));
insert into testint(result) values(doAction(5,'receive',current_date,5));
insert into testint(result) values(doAction(6,'receive',current_date,6));
insert into testint(result) values(doAction(7,'receive',current_date,7));
insert into testint(result) values(doAction(8,'receive',current_date,8));
insert into testint(result) values(doAction(9,'receive',current_date,9));
insert into testint(result) values(doAction(10,'receive',current_date,10));
insert into testint(result) values(doAction(11,'receive',current_date,15));
insert into testint(result) values(doAction(12,'receive',current_date,15));
insert into testint(result) values(doAction(13,'receive',current_date,15));
insert into testint(result) values(doAction(14,'receive',current_date,16));
insert into testint(result) values(doAction(15,'receive',current_date,16));
insert into testint(result) values(doAction(16,'receive',current_date,16));



IF (Select COUNT(*) From Top10Customers) = 10 THEN
	insert into testResult(result) values('success 1 on test 24');
	ELSE    insert into testResult(result) values('fail 1 on test 24');

END IF;


select * into tuple from Top10Customers where accountNum=16;
Top10CustomersRow :=(16,16);
IF tuple = Top10CustomersRow THEN
	insert into testResult(result) values('success 2 on test 24');
	ELSE    insert into testResult(result) values('fail 2 on test 24');

END IF;

select * into tuple from Top10Customers where accountNum=14;
Top10CustomersRow :=(14,16);
IF tuple = Top10CustomersRow THEN
	insert into testResult(result) values('success 3 on test 24');
	ELSE    insert into testResult(result) values('fail 3 on test 24');

END IF;

select * into tuple from Top10Customers where accountNum=7;
Top10CustomersRow :=(7,7);
IF tuple = Top10CustomersRow THEN
	insert into testResult(result) values('success 4 on test 24');
	ELSE    insert into testResult(result) values('fail 4 on test 24');

END IF;

IF NOT EXISTS(Select * from Top10Customers where accountNum=6) THEN
	insert into testResult(result) values('success 5 on test 24');
	ELSE    insert into testResult(result) values('fail 5 on test 24');

END IF;

perform deleteall();
return true;
END;

$$ language plpgsql;



--TEST 24
--Adding money to more than 10 clients with some same balances
CREATE OR REPLACE FUNCTION Test24() RETURNS BOOLEAN AS $$
 
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN


insert into testint(result) values(newCustomer(1,'alon','7421',-200));
insert into testint(result) values(newCustomer(2,'alon','7421',-200));
insert into testint(result) values(newCustomer(3,'alon','7421',-200));
insert into testint(result) values(newCustomer(4,'alon','7421',-200));
insert into testint(result) values(newCustomer(5,'alon','7421',-200));
insert into testint(result) values(newCustomer(6,'alon','7421',-200));
insert into testint(result) values(newCustomer(7,'alon','7421',-200));
insert into testint(result) values(newCustomer(8,'alon','7421',-200));
insert into testint(result) values(newCustomer(9,'alon','7421',-200));
insert into testint(result) values(newCustomer(10,'alon','7421',-200));
insert into testint(result) values(newCustomer(11,'alon','7421',-200));
insert into testint(result) values(newCustomer(12,'alon','7421',-200));
insert into testint(result) values(newCustomer(13,'alon','7421',-200));
insert into testint(result) values(newCustomer(14,'alon','7421',-200));
insert into testint(result) values(newCustomer(15,'alon','7421',-200));
insert into testint(result) values(newCustomer(16,'alon','7421',-200));


insert into testint(result) values(doAction(1,'receive',current_date,1));
insert into testint(result) values(doAction(2,'receive',current_date,2));
insert into testint(result) values(doAction(3,'receive',current_date,3));
insert into testint(result) values(doAction(4,'receive',current_date,4));
insert into testint(result) values(doAction(5,'receive',current_date,5));
insert into testint(result) values(doAction(6,'receive',current_date,6));
insert into testint(result) values(doAction(7,'receive',current_date,7));
insert into testint(result) values(doAction(8,'receive',current_date,8));
insert into testint(result) values(doAction(9,'receive',current_date,9));
insert into testint(result) values(doAction(10,'receive',current_date,10));
insert into testint(result) values(doAction(11,'receive',current_date,15));
insert into testint(result) values(doAction(12,'receive',current_date,15));
insert into testint(result) values(doAction(13,'receive',current_date,15));
insert into testint(result) values(doAction(14,'receive',current_date,16));
insert into testint(result) values(doAction(15,'receive',current_date,16));
insert into testint(result) values(doAction(16,'receive',current_date,16));



IF (Select COUNT(*) From Top10Customers) = 10 THEN
	insert into testResult(result) values('success 1 on test 24');
	ELSE    insert into testResult(result) values('fail 1 on test 24');

END IF;


select * into tuple from Top10Customers where accountNum=16;
Top10CustomersRow :=(16,16);
IF tuple = Top10CustomersRow THEN
	insert into testResult(result) values('success 2 on test 24');
	ELSE    insert into testResult(result) values('fail 2 on test 24');

END IF;

select * into tuple from Top10Customers where accountNum=14;
Top10CustomersRow :=(14,16);
IF tuple = Top10CustomersRow THEN
	insert into testResult(result) values('success 3 on test 24');
	ELSE    insert into testResult(result) values('fail 3 on test 24');

END IF;

select * into tuple from Top10Customers where accountNum=7;
Top10CustomersRow :=(7,7);
IF tuple = Top10CustomersRow THEN
	insert into testResult(result) values('success 4 on test 24');
	ELSE    insert into testResult(result) values('fail 4 on test 24');

END IF;

IF NOT EXISTS(Select * from Top10Customers where accountNum=6) THEN
	insert into testResult(result) values('success 5 on test 24');
	ELSE    insert into testResult(result) values('fail 5 on test 24');

END IF;

perform deleteall();
return true;
END;

$$ language plpgsql;



--TEST 24
--Adding money to more than 10 clients with some same balances
CREATE OR REPLACE FUNCTION Test24() RETURNS BOOLEAN AS $$
 
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN


insert into testint(result) values(newCustomer(1,'alon','7421',-200));
insert into testint(result) values(newCustomer(2,'alon','7421',-200));
insert into testint(result) values(newCustomer(3,'alon','7421',-200));
insert into testint(result) values(newCustomer(4,'alon','7421',-200));
insert into testint(result) values(newCustomer(5,'alon','7421',-200));
insert into testint(result) values(newCustomer(6,'alon','7421',-200));
insert into testint(result) values(newCustomer(7,'alon','7421',-200));
insert into testint(result) values(newCustomer(8,'alon','7421',-200));
insert into testint(result) values(newCustomer(9,'alon','7421',-200));
insert into testint(result) values(newCustomer(10,'alon','7421',-200));
insert into testint(result) values(newCustomer(11,'alon','7421',-200));
insert into testint(result) values(newCustomer(12,'alon','7421',-200));
insert into testint(result) values(newCustomer(13,'alon','7421',-200));
insert into testint(result) values(newCustomer(14,'alon','7421',-200));
insert into testint(result) values(newCustomer(15,'alon','7421',-200));
insert into testint(result) values(newCustomer(16,'alon','7421',-200));


insert into testint(result) values(doAction(1,'receive',current_date,1));
insert into testint(result) values(doAction(2,'receive',current_date,2));
insert into testint(result) values(doAction(3,'receive',current_date,3));
insert into testint(result) values(doAction(4,'receive',current_date,4));
insert into testint(result) values(doAction(5,'receive',current_date,5));
insert into testint(result) values(doAction(6,'receive',current_date,6));
insert into testint(result) values(doAction(7,'receive',current_date,7));
insert into testint(result) values(doAction(8,'receive',current_date,8));
insert into testint(result) values(doAction(9,'receive',current_date,9));
insert into testint(result) values(doAction(10,'receive',current_date,10));
insert into testint(result) values(doAction(11,'receive',current_date,15));
insert into testint(result) values(doAction(12,'receive',current_date,15));
insert into testint(result) values(doAction(13,'receive',current_date,15));
insert into testint(result) values(doAction(14,'receive',current_date,16));
insert into testint(result) values(doAction(15,'receive',current_date,16));
insert into testint(result) values(doAction(16,'receive',current_date,16));



IF (Select COUNT(*) From Top10Customers) = 10 THEN
	insert into testResult(result) values('success 1 on test 24');
	ELSE    insert into testResult(result) values('fail 1 on test 24');

END IF;


select * into tuple from Top10Customers where accountNum=16;
Top10CustomersRow :=(16,16);
IF tuple = Top10CustomersRow THEN
	insert into testResult(result) values('success 2 on test 24');
	ELSE    insert into testResult(result) values('fail 2 on test 24');

END IF;

select * into tuple from Top10Customers where accountNum=14;
Top10CustomersRow :=(14,16);
IF tuple = Top10CustomersRow THEN
	insert into testResult(result) values('success 3 on test 24');
	ELSE    insert into testResult(result) values('fail 3 on test 24');

END IF;

select * into tuple from Top10Customers where accountNum=7;
Top10CustomersRow :=(7,7);
IF tuple = Top10CustomersRow THEN
	insert into testResult(result) values('success 4 on test 24');
	ELSE    insert into testResult(result) values('fail 4 on test 24');

END IF;

IF NOT EXISTS(Select * from Top10Customers where accountNum=6) THEN
	insert into testResult(result) values('success 5 on test 24');
	ELSE    insert into testResult(result) values('fail 5 on test 24');

END IF;

perform deleteall();
return true;
END;

$$ language plpgsql;



--TEST 25
--Adding money to more than 10 clients with some same balances, and then removing money from the top ones and giving money to bottom ones.
CREATE OR REPLACE FUNCTION Test25() RETURNS BOOLEAN AS $$
 
DECLARE 
tuple record;
CustomerRow Customers%rowtype;
AccountBalanceRow AccountBalance%rowtype;
ActionsRow Actions%rowtype;
SavingsRow Savings%rowtype;
Top10CustomersRow Top10Customers%rowtype;

BEGIN

insert into testint(result) values(newCustomer(1,'alon','7421',-200));
insert into testint(result) values(newCustomer(2,'alon','7421',-200));
insert into testint(result) values(newCustomer(3,'alon','7421',-200));
insert into testint(result) values(newCustomer(4,'alon','7421',-200));
insert into testint(result) values(newCustomer(5,'alon','7421',-200));
insert into testint(result) values(newCustomer(6,'alon','7421',-200));
insert into testint(result) values(newCustomer(7,'alon','7421',-200));
insert into testint(result) values(newCustomer(8,'alon','7421',-200));
insert into testint(result) values(newCustomer(9,'alon','7421',-200));
insert into testint(result) values(newCustomer(10,'alon','7421',-200));
insert into testint(result) values(newCustomer(11,'alon','7421',-200));
insert into testint(result) values(newCustomer(12,'alon','7421',-200));
insert into testint(result) values(newCustomer(13,'alon','7421',-200));
insert into testint(result) values(newCustomer(14,'alon','7421',-200));
insert into testint(result) values(newCustomer(15,'alon','7421',-200));
insert into testint(result) values(newCustomer(16,'alon','7421',-200));


insert into testint(result) values(doAction(1,'receive',current_date,1));
insert into testint(result) values(doAction(2,'receive',current_date,2));
insert into testint(result) values(doAction(3,'receive',current_date,3));
insert into testint(result) values(doAction(4,'receive',current_date,4));
insert into testint(result) values(doAction(5,'receive',current_date,5));
insert into testint(result) values(doAction(6,'receive',current_date,6));
insert into testint(result) values(doAction(7,'receive',current_date,7));
insert into testint(result) values(doAction(8,'receive',current_date,8));
insert into testint(result) values(doAction(9,'receive',current_date,9));
insert into testint(result) values(doAction(10,'receive',current_date,10));
insert into testint(result) values(doAction(11,'receive',current_date,15));
insert into testint(result) values(doAction(12,'receive',current_date,15));
insert into testint(result) values(doAction(13,'receive',current_date,15));
insert into testint(result) values(doAction(14,'receive',current_date,16));
insert into testint(result) values(doAction(15,'receive',current_date,16));
insert into testint(result) values(doAction(16,'receive',current_date,16));

insert into testint(result) values(doAction(12,'payment',current_date,-12));
insert into testint(result) values(closeCustomer(15));
insert into testint(result) values(doAction(1,'receive',current_date,16));






IF (Select COUNT(*) From Top10Customers) = 10 THEN
	insert into testResult(result) values('success 1 on test 25');
	ELSE    insert into testResult(result) values('fail 1 on test 25');

END IF;


select * into tuple from Top10Customers where accountNum=6;
Top10CustomersRow :=(6,6);
IF tuple = Top10CustomersRow THEN
	insert into testResult(result) values('success 2 on test 25');
	ELSE    insert into testResult(result) values('fail 2 on test 25');

END IF;

select * into tuple from Top10Customers where accountNum=14;
Top10CustomersRow :=(14,16);
IF tuple = Top10CustomersRow THEN
	insert into testResult(result) values('success 3 on test 25');
	ELSE    insert into testResult(result) values('fail 3 on test 25');

END IF;

IF NOT EXISTS(Select * from Top10Customers where accountNum=5) THEN
	insert into testResult(result) values('success 4 on test 25');
	ELSE    insert into testResult(result) values('fail 4 on test 25');

END IF;

IF NOT EXISTS(Select * from Top10Customers where accountNum=15) THEN
	insert into testResult(result) values('success 5 on test 25');
	ELSE    insert into testResult(result) values('fail 5 on test 25');

END IF;

IF NOT EXISTS(Select * from Top10Customers where accountNum=12) THEN
	insert into testResult(result) values('success 6 on test 25');
	ELSE    insert into testResult(result) values('fail 6 on test 25');

END IF;


perform deleteall();
return true;
END;

$$ language plpgsql;

--END OF TESTS


CREATE OR REPLACE FUNCTION FinalTesting() RETURNS BOOLEAN AS $$
BEGIN
	IF NOT EXISTS(Select * from testResult where NOT result LIKE 'success%')
		THEN insert into testResult(result) values('All Tests Passed!!');	
	ELSE insert  into testResult(result) values('ERROR! One or more tests have failed');
	END IF;
	return true;
	
END;
$$ language plpgsql;

drop table useless;

create table useless(stam boolean);
drop table testResult;
create table testResult(id serial primary key, result TEXT);

insert into useless values(test1());
insert into useless values(test2());
insert into useless values(test3());
insert into useless values(test4());
insert into useless values(test5());
insert into useless values(test6());
insert into useless values(test7());
insert into useless values(test8());
insert into useless values(test9());
insert into useless values(test10());
insert into useless values(test11());
insert into useless values(test12());
insert into useless values(test13());
insert into useless values(test14());
insert into useless values(test15());
insert into useless values(test16());
insert into useless values(test17());
insert into useless values(test18());
insert into useless values(test19());
insert into useless values(test20());
insert into useless values(test21());
insert into useless values(test22());
insert into useless values(test23());
insert into useless values(test24());
insert into useless values(test25());




insert into useless values(FinalTesting());
select * from testResult;

	














