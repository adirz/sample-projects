package oop.ex6.filters;

import java.io.File;

/**
 * The filter interface. All that check if a file meets the requirements
 * implements this.
 * 
 * @author Adir
 *
 */
public interface Filter {
	
	/**
	 * @param toCheck - The file we need to pass through the filter.
	 * @return True if the conditions in the filter are met. False otherwise.
	 */
	public boolean isPass(File toCheck);
}
