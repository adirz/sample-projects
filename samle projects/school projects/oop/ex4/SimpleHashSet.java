/**
 * @author Adir
 * The basic of a needed hash table
 */
public abstract class SimpleHashSet implements SimpleSet {
	
	private final static int DEFAULT_CAPACITY = 16;
	private final static float DEFAULT_UPPER_LOAD_FACTOR = (float) 0.75;
	private final static float DEFAULT_LOWER_LOAD_FACTOR = (float) 0.25;
	
	private int cap;
	private float upperLoadFactor;
	private float lowerLoadFactor;
	
	/**
	 * SimpleHashSet constructor
	 */
	public SimpleHashSet(){
		cap = DEFAULT_CAPACITY;
		upperLoadFactor = DEFAULT_UPPER_LOAD_FACTOR;
		lowerLoadFactor = DEFAULT_LOWER_LOAD_FACTOR;
	}
	
	/**
	 * SimpleHashSet constructor when receiving upper and lower load factors
	 */
	public SimpleHashSet(float upperLoadFactor, float lowerLoadFactor){
		cap = DEFAULT_CAPACITY;
		this.upperLoadFactor = upperLoadFactor;
		this.lowerLoadFactor = lowerLoadFactor;
	}
	
	/**
	 * personally I disagree, but was told not to use protected, so getting
	 * the upper load factor is needed
	 * @return upperLoadFactor
	 */
	public float getUpperLoadFactor(){
		return upperLoadFactor;
	}
	
	/**
	 * Personally I disagree, but was told not to use protected, so getting
	 * the lower load factor is needed
	 * @return lowerLoadFactor
	 */
	public float getLowerLoadFactor(){
		return lowerLoadFactor;
	}
	
	/**
	 * Halves capacity and calls @function reorganize
	 * @return true if resized, false if it is the smallest possible
	 */
	public boolean shrink(){
		if(cap == 1)
			return false;
		cap = cap/2;
		reorganize();
		return true;
	}

	/**
	 * Doubles capacity and calls @function reorganize
	 * @return true if resized, false if it is the smallest possible
	 */
	public void enlarge(){
		cap = cap*2;
		reorganize();
	}
	
	/**
	 * @return size - the amount of elements in the hash table
	 */
	public abstract int size();

	/**
	 * @return cap - the amount of cells in the hash table
	 */
	public int capacity() {
		return cap;
	}
	
	/**
	 * Creates a new table (after its capacity has changed) and puts all the
	 * values that are in the previous table in the new one.
	 */
	public abstract void reorganize();

}
