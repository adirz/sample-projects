SELECT	Distinct cname
FROM	Contributor C1
WHERE	NOT EXISTS (
			SELECT *
			FROM	Contributor C2, Donated D1
			WHERE	C2.cid = D1.cid AND
					D1.aid  NOT IN (
						SELECT	aid
						FROM	Contributor C3, Donated D2
						WHERE	C3.cid = D2.cid AND	C3.cname = 'Rothschild'
					)
		)
ORDER BY cname ASC