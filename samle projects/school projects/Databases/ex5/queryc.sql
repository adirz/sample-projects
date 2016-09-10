SELECT 	DISTINCT oname
FROM	Contributor, Organization, Donated
WHERE	Contributor.cid = Donated.cid
		AND Donated.aid = Organization.aid
GROUP BY Organization.aid
		HAVING COUNT( Contributor.cid ) >= 4
ORDER BY oname ASC