package oop.ex6.filters.name;

/**
 * A filter that checks if the file have the desired prefix.
 * 
 * @author Adir
 *
 */
public class PrefixFilter extends NameFilter {

	/**
	 * Constructor. receives a String to match to the prefix of the file.
	 * 
	 * @param desiredPrefix - the desired prefix.
	 */
	public PrefixFilter(String desiredPrefix){
		super(desiredPrefix);
	}
	
	@Override
	public boolean filterWord(String name) {
		return name.startsWith(toCompare);
	}

}
