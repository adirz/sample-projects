

import java.util.LinkedList;

/**
 * @author Adir
 * class ChainedHashSet which represent String's hash table, where you can fit
 * any number of elements in each cell.
 * 
 * @param MyLinked[] table - the hash table that store the information
 * @param int size - the amount of elements in the hash table
 */
public class ChainedHashSet extends SimpleHashSet {

	/**
	 * @author Adir
	 * class MyLinked which wraps a LinkedList<String> because you can't
	 * compile an array of LinkedList<String>
	 * @param list - the wrapped LinkedList<String>
	 */
	private class MyLinked{
		private LinkedList<String> list;
		
		/**
		 * Constructor. Creates the list
		 */
		public MyLinked(){
			list = new LinkedList<String>();
		}
		
		/**
		 * @return the wrapped list
		 */
		public LinkedList<String> getList(){
			return list;
		}
	}
	
	private MyLinked[] table;
	private int size;
	
	/**
	 * ChainedHashSet constructor
	 */
	public ChainedHashSet(){
		super();
		size = 0;
		table = new  MyLinked[capacity()];
		for(int i = 0; i < capacity(); i++){
			table [i] = new MyLinked();
		}
	}
	
	/**
	 * ChainedHashSet constructor when receiving upper and lower load factors
	 */
	public ChainedHashSet(float upperLoadFactor, float lowerLoadFactor){
		super(upperLoadFactor, lowerLoadFactor);
		size = 0;
		table = new  MyLinked[capacity()];
		for(int i = 0; i < capacity(); i++){
			table [i] = new MyLinked();
		}
	}

	/**
	 * ChainedHashSet constructor when receiving variables
	 */
	public ChainedHashSet(String[] data){
		super();
		size = 0;
		table = new  MyLinked[capacity()];
		for(int i = 0; i < capacity(); i++){
			table [i] = new MyLinked();
		}
		for(int i = 0; i < data.length; i++){
			this.add(data[i]);
		}
	}
	
	/**
	 * @param word
	 * @return the hash code of "word"
	 */
	private int myHash(String word){
		if(word.hashCode()%capacity() >=0 )
			return word.hashCode()%capacity();
		return capacity() + word.hashCode()%capacity();
	}
	
	@Override
	public void reorganize(){
		MyLinked[] tempTable = new MyLinked[capacity()];
		for(int i = 0; i < capacity() ; i++){
			tempTable[i] = new MyLinked();
		}
		int tempHash;
		String tempVal;
		for(int i = 0; i < table.length ; i++){
			while(!table[i].getList().isEmpty()){
				tempVal = table[i].getList().getFirst();
				tempVal = table[i].getList().remove();
				tempHash = myHash(tempVal);
				tempTable[tempHash].getList().add(tempVal);
			}
		}
		table = tempTable;
	}
	
	@Override
	public boolean add(String newValue) {
		if(contains(newValue))
			return false;
		size ++;
		if((double)size/capacity() > getUpperLoadFactor())
			enlarge();
		table[myHash(newValue)].getList().add(newValue);
		return true;
	}

	@Override
	public boolean contains(String searchVal){
		return table[myHash(searchVal)].getList().contains(searchVal);
	}

	@Override
	public boolean delete(String toDelete) {
		if(table[myHash(toDelete)].getList().remove(toDelete)){
			size--;
			if((double)size/capacity() < getLowerLoadFactor()){
				shrink();
			}
			return true;
		}
		return false;
	}

	@Override
	public int size() {
		return size;
	}

}
