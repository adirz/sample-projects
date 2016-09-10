package oop.ex6.filters.name;

import java.io.File;

/**
 * An abstract prototype that all those who matches strings extends.
 * 
 * @author Adir
 *
 */
import oop.ex6.filters.Filter;

/**
 * An abstract filter that all String matching filters extends.
 * 
 * @author Adir
 *
 */
public abstract class NameFilter implements Filter {
	protected String toCompare;

	/**
	 * Constructor. receives a String to match.
	 * 
	 * @param filterWord - the String to match.
	 */
	public NameFilter(String filterWord){
		toCompare = filterWord;
	}
	
	/**
	 * Receives a string 'name' and check if it passes the filter.
	 * @param name
	 * @return true, if this string should pass the filter. false otherwise.
	 */
	public abstract boolean filterWord(String name);
	
	@Override
	public boolean isPass(File toCheck){
		return filterWord(toCheck.getName());
	}

}
