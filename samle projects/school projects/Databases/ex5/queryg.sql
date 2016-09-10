SELECT	maximum
FROM	( 
		SELECT MAX( Total.ctotal ) as maximum
		FROM ( 
			SELECT	SUM( donation ) AS ctotal
			FROM	Contributor, Organization, Donated
			WHERE	Contributor.cid = Donated.cid AND Donated.aid = Organization.aid
					AND Contributor.cname = 'Bill Gates'
			) AS Total
		) AS philantrope