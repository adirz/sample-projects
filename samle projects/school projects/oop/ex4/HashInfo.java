/**
 * @author Adir
 * In order to nicely organize the data asked on hashing time, to each type of
 * hashing
 * @param buildTimes[i] -The times it takes to enter the 'i' file to the table
 * @param searchTime[i][j] -The times it takes to find the 'j' word in the 'i'
 * file
 */
public class HashInfo {
	
	private long[] buildTimes;
	private long[][] searchTime;
	
	/**
	 * Constructor, creates buildTimes and searchTime, depending on:
	 * @param numOfFiles - number of files
	 * @param numOfSearches - number of search words
	 */
	public HashInfo(int numOfFiles, int numOfSearches){
		buildTimes = new long[numOfFiles];
		searchTime = new long[numOfFiles][];
		for(int i=0; i < numOfFiles; i++){
			searchTime[i] = new long[numOfSearches];
		}
	}
	
	/**
	 * In order to find out which is fastest, I did this function
	 * @param times - how long each one took
	 * @return the place of the fastest time
	 */
	public static int fastest(long[] times){
		int place = 0;
		for(int i = 1; i<times.length; i++){
			if(times[i] < times[i-1])
				place = i;
		}
		return place;
	}

	/**
	 * Adding a new build time
	 * @param file - file build
	 * @param time - time it took
	 */
	public void fileBuildTime(int file, long time) {
		buildTimes[file] = time;
	}
	
	/**
	 * Adding a new search time
	 * @param file - file searched
	 * @param search - word searched
	 * @param time - time it took
	 */
	public void fileSearchTime(int file, int search, long time) {
		searchTime[file][search] = time;
	}

	/**
	 * @param file - the file we want to know how long it took to input
	 * @return the build time of the received file
	 */
	public long getBuildTime(int file){
		return buildTimes[file];
	}

	/**
	 * @param file - the file we want to know how long it took to search
	 * @param search - the number of word that had been searched
	 * @return the search time of the received file and word number
	 */
	public long getSearchTime(int file, int search){
		return searchTime[file][search];
	}
}
