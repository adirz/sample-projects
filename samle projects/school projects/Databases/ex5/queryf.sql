SELECT	cid
FROM	( 
		SELECT Total.cid, MAX( Total.ctotal ) 
		FROM ( 
			SELECT cid, SUM( Donated.donation ) AS ctotal
			FROM Donated
			GROUP BY Donated.cid
			) AS Total
		) AS philantrope
ORDER BY cid ASC