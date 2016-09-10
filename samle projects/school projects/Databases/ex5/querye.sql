SELECT 	DISTINCT cname
FROM	Contributor
WHERE	Contributor.cid IN (
			SELECT	Donated.cid
			FROM	Donated
			WHERE	Donated.donation >= 50000
		)
		AND Contributor.cid NOT IN (
			SELECT	Donated.cid
			FROM	Donated
			WHERE	Donated.donation < 50000
		)
ORDER BY cname ASC