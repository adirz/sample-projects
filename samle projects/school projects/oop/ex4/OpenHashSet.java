/**
 * @author Adir
 * Creates and handles a hash table using quadric hashing
 */
public class OpenHashSet extends SimpleHashSet {
	
	private String[] table;
	private boolean[] deleted;
	private int size;
	
	/**
	 * OpenHashSet constructor
	 */
	public OpenHashSet(){
		super();
		intialize();
	}
	
	/**
	 * OpenHashSet constructor when receiving upper and lower load factors
	 */
	public OpenHashSet(float upperLoadFactor, float lowerLoadFactor){
		super(upperLoadFactor, lowerLoadFactor);
		intialize();
	}
	
	/**
	 * Constructor, builds to arrays and fills the with the given data
	 * @param data
	 */
	public OpenHashSet(String[] data){
		super();
		intialize();
		for(int i=0; i < data.length; i ++){
			add(data[i]);
		}
	}
	
	/**
	 * Creates new empty arrays to be hash table and it assistance to speed
	 * the search by knowing if the hash formula has past this point
	 */
	private void intialize(){
		size = 0;
		table = new String[capacity()];
		deleted = new boolean[capacity()];
		for(int i = 0; i < capacity(); i++){
			table[i] = "";
			deleted[i] = false;
		}		
	}
	
	/**
	 * Creates hash code for the word in the table, according to your formula
	 * @param word - What to hash
	 * @param tryTime - How much cells has been full
	 * @return
	 */
	private int hashishCode(String word, int tryTime){
		int place = (word.hashCode() + tryTime*(tryTime + 1)/2)%capacity();
		if(place < 0)
			return capacity() + place;
		return place;
	}
	
	@Override
	public boolean add(String newValue) {
		if(contains(newValue)){
			return false;
			}
		size ++;
		if((double)size/capacity() > getUpperLoadFactor())
			enlarge();
		int tryTime = 0;
		while(!table[hashishCode(newValue, tryTime)].equals("")){
			tryTime ++;
		}
		table[hashishCode(newValue, tryTime)] = newValue;
		return true;
	}

	@Override
	public boolean contains(String searchVal) {
		for(int i = 0; i < size; i++){
			if(table[hashishCode(searchVal, i)].equals(searchVal))
				return true;
			if(!deleted[hashishCode(searchVal, i)] && 
				table[hashishCode(searchVal, i)].equals(""))
				return false;
		}
		return false;
	}

	@Override
	public boolean delete(String toDelete) {
		for(int i = 0; i < size; i++){
			if(table[hashishCode(toDelete, i)].equals(toDelete)){
				deleted[hashishCode(toDelete, i)] = true;
				table[hashishCode(toDelete, i)] = "";
				size --;
				if((double)size/capacity() < getLowerLoadFactor())
					shrink();
				return true;
			}
			if(!deleted[hashishCode(toDelete, i)] && 
				table[hashishCode(toDelete, i)].equals(""))
				return false;
		}
		return false;
	}

	@Override
	public void reorganize() {
		String[] tempTable = new String[capacity()];
		deleted = new boolean[capacity()];
		for(int i = 0; i < capacity(); i++){
			tempTable[i] = "";
			deleted[i] = false;
		}
		int tryTime;
		String val;
		for(int i = 0; i< table.length; i++){
			val = table[i];
			if(!val.equals("")){
				tryTime = 0;
				while(!tempTable[hashishCode(val, tryTime)].equals("")){
					tryTime ++;
				}
				tempTable[hashishCode(val, tryTime)] = val;
			}
		}
		table = tempTable;		
	}

	@Override
	public int size() {
		return size;
	}
}
