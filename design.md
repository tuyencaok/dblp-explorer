# dblp-explorer
Design
1. Read each line of text file and store objects in a list
	1a. Parse each line in as an Json Object.
	1b. Store all Json Objects into a List<JsonObject> allPapers.
2. Find papers containing keywords and stored them in a list: List<JsonObject> tier0
	2a. get stream of list of allPapers, and filter papers containing keyword, then collect them into a list.
	2b. print the list of paper containing keywords, tier0.
3. Find papers of the next n tiers
	3a. build findReferences function to find all immediate papers of current list:
		parameters: list of all papers, and list of upperTier papers - which cited the papers of its lower tier.
		operation: - get all referenced ids from upperTier list using get("references") function for each paper (json Object),
			   - get stream of list of allPapers, and filter papers which id on the referenced list (above), then collect them into a list.
		return: List<JsonObject> referencedPapers
	3b. start with tier0, passing tier0 as parameter in findReferences function, to find all papers of tier1
	3c. continue passing tier1 as parameter in findReferences function, to find all paper of tier2.
	3d. while (k<n), recursively passing list of papers, tier-k, as parameter in findReferences function , to find papers in tier k+1.
	3d. break when tier-k list is empty or k=n-1.
	3e. print out list of papers in tier-k.
