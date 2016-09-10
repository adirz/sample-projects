
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeSet;

/**
 * @author Adir
 * A way to see, by an example the speed it takes to input and build a hash
 * table, and search it, in many different types of hashing
 */
public class SimpleSetPerformanceAnalyzer {
	private final static int NUM_OF_TYPES = 5;
	private final static int NUM_OF_FILES = 2;
	private final static int NUM_OF_SEARCH_WORDS = 3;
	private final static String[] FILE_NAMES = fileNames();
	private final static String[] SEARCH_WORDS = searchWords();

	/**
	 * @return words - an array of word we were asked to search
	 */
	private static String[] searchWords(){
		String[] words = new String[NUM_OF_SEARCH_WORDS];
		words[0] = "hi";
		words[1] = "-13170890158";
		words[2] = "23";
		return words;
	}
	
	/**
	 * @return words - an array of the files names
	 */
	private static String[] fileNames(){
		String[] words = new String[NUM_OF_FILES];
		words[0] = "C:/Users/Adir/workspace/ex4/src/data1.txt";
		words[1] = "C:/Users/Adir/workspace/ex4/src/data2.txt";
		return words;
	}
	
	/**
	 * the main method running the analyzing
	 */
	public static void main(String[] args) {
		//Creating a list of words to be inputed to the hash tables
		String[][] files = new String[NUM_OF_FILES][];
		for(int i = 0; i < NUM_OF_FILES; i++){
			files[i] = Ex4Utils.file2array(FILE_NAMES[i]);
		}
		
		//Variables I'll use to find out how long anything took
		long timeBefore;
		long timeAfter;
		
		SimpleSet[] hashers = new SimpleSet[NUM_OF_TYPES];
		HashInfo[] infos= new HashInfo[NUM_OF_TYPES];
		for(int i = 0; i < NUM_OF_TYPES; i++){
			infos[i] = new HashInfo(NUM_OF_FILES, NUM_OF_SEARCH_WORDS);
		}
		
		//In order to do everything in a loop and not write it twice or more
		//I created this two organize helpers
		int helper1 = 1, helper2 = 1;
		
		//getting info on every file
		for(int h=0; h < NUM_OF_FILES; h++){
			hashers[0] = new ChainedHashSet();		
			hashers[1] = new OpenHashSet();
			hashers[2] = new CollectionFacadeSet(new TreeSet<String>());
			hashers[3] = new CollectionFacadeSet(new LinkedList<String>());
			hashers[4] = new CollectionFacadeSet(new HashSet<String>());
			//getting info on every type
			for(int i =0; i < NUM_OF_TYPES; i++){
				//building
				timeBefore = (new Date()).getTime();
				for(int j= 0; j < files[h].length; j++){
					hashers[i].add(files[h][j]);
				}
				timeAfter = (new Date()).getTime();
				infos[i].fileBuildTime(h, timeAfter-timeBefore);
				
				//searching
				for(int j= 0; j <NUM_OF_SEARCH_WORDS - helper2; j += helper1){
					System.out.println("finding " +SEARCH_WORDS[j] +" in file "+h + 
							" in type " + i);
					timeBefore = (new Date()).getTime();
					System.out.println("found? " + hashers[i].contains(SEARCH_WORDS[j]));
					timeAfter = (new Date()).getTime();
					System.out.println("time: " + (timeAfter-timeBefore));
					System.out.println();
					infos[i].fileSearchTime(h, j, timeAfter-timeBefore);
				}
			}
			helper1 = 2;
			helper2 = 0;
		}
		
		//printing the info as required
		String output = "";
		
		long[] times = new long[NUM_OF_TYPES];
		for(int i=0; i < NUM_OF_TYPES; i++){
			times[i] = infos[i].getBuildTime(0);
		}
		int fastest = HashInfo.fastest(times);
		for(int i=0; i < NUM_OF_TYPES; i++){
			if(fastest == i){
				output += "!" + times[i] + "!|";
			}else{
				output +=times[i] + "|";
			}
		}
		System.out.println("insertion of data1");
		System.out.println("ChainedHashSet   OpenHashSet   TreeSet   LinkedList   HashSet");
		System.out.println(output);
		System.out.println();
		
		for(int i=0; i < NUM_OF_TYPES; i++){
			times[i] = infos[i].getBuildTime(1);
		}
		fastest = HashInfo.fastest(times);
		output = "";
		for(int i=0; i < NUM_OF_TYPES; i++){
			if(fastest == i){
				output += "!" + times[i] + "!|";
			}else{
				output +=times[i] + "|";
			}
		}

		System.out.println("insertion of data2");
		System.out.println(output);
		System.out.println();
		
	}
}
