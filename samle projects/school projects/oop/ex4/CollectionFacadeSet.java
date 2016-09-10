import java.util.Collection;

/**
 * @author Adir
 * @param Collection<String> collection - the hash table
 * A class that wraps a collection in order to get different types in an array
 */
public class CollectionFacadeSet implements SimpleSet{
	
	private Collection<String> collection;
	
	/**
	 * Constructor that receives collection and saves it to be wrapped
	 * @param collection
	 */
	public CollectionFacadeSet(Collection<String> collection){
		this.collection = collection;
	}
	
	@Override
	public boolean add(String newValue){
		if(!this.contains(newValue)){
			collection.add(newValue);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean contains(String searchVal){
		return collection.contains(searchVal);
	}
	
	@Override
	public boolean delete(String toDelete){
		return collection.remove(toDelete);
	}

	@Override
	public int size() {
		return collection.size();
	}
	
}
