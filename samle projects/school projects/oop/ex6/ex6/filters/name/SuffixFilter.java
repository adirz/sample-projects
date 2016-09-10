package oop.ex6.filters.name;

/**
 * A filter that checks if the file have the desired suffix.
 * 
 * @author Adir
 *
 */
public class SuffixFilter extends NameFilter {
	
	/**
	 * Constructor. receives a String to match to the suffix of the file.
	 * 
	 * @param desiredSuffix - the desired suffix.
	 */
	public SuffixFilter(String desiredSuffix){
		super(desiredSuffix);
	}
	
	@Override
	public boolean filterWord(String name) {
		return name.endsWith(toCompare);
	}

}
