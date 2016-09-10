CREATE TABLE Guest (
	id			INTEGER NOT NULL PRIMARY KEY CHECK( id >= 0),
	name    	VARCHAR(20) NOT NULL,
	birthYear	INTEGER NOT NULL CHECK( birthYear > 1850 AND birthYear < 2050)
);

CREATE TABLE Room (
	rnum	INTEGER NOT NULL PRIMARY KEY CHECK( rnum >= 1 AND rnum <= 30 ),
	floor	INTEGER NOT NULL CHECK( floor >= 1 AND floor <= 4),
	price	INTEGER CHECK( price >= 0),
	area	INTEGER NOT NULL DEFAULT(95) CHECK ( area >= 0 AND (area < 150 OR price >= 1000))
);
 
CREATE TABLE Stayed (
	id       INTEGER   
	rnum     INTEGER
	
	FOREIGN KEY (id) REFERENCES Guest(id)
 
 	FOREIGN KEY (rnum) REFERENCES Room(rnum)
);
