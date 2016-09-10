SELECT 	DISTINCT cname
FROM	Contributor, Organization, Donated
WHERE	Contributor.cid = Donated.cid AND Donated.aid = Organization.aid AND Organization.estYear < 1980
ORDER BY cname ASC