package oop.ex6.filters;

import java.io.File;

/**
 * A type of filter that doesn't filter- lets anything pass through.
 * 
 * @author Adir
 *
 */
public class AllFilter implements Filter {

	@Override
	public boolean isPass(File toCheck) {
		return true;
	}
	
}
