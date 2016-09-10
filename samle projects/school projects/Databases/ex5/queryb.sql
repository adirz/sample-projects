SELECT 	DISTINCT cname
FROM	Contributor, Organization O1, Organization O2, Donated D1, Donated D2
WHERE	Contributor.cid = D1.cid AND Contributor.cid = D2.cid
		AND D1.aid = O1.aid AND D2.aid = O2.aid
		AND O1.oname = "Latet" AND O2.oname = "Elem"
ORDER BY cname ASC