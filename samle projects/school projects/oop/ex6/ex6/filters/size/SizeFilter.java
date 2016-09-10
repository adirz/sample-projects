package oop.ex6.filters.size;

import java.io.File;

import oop.ex6.filters.Filter;
import oop.ex6.specialExceptions.TypeOneException;

/**
 * A filter that checks if the file is bigger/smaller than chosen size.
 * 
 * @author Adir
 *
 */
public class SizeFilter implements Filter {
	private static final int KILOBYTES = 1024;// 1 kilobyte  = 1024 bytes
	private boolean larger;
	private double pivotNum; // one-side bound
	
	/**
	 * Constructor. If we want it to be larger than "pivotNum" "largerThan"
	 * should be true. if we want it to be smaller than "largerThan" it should
	 * be false.
	 * 
	 * @param pivotNum - one side bound.
	 * @param largerThan - file size is larger than pivotNum.
	 * @throws TypeOneException in case of bad parameter
	 */
	public SizeFilter(double pivot, boolean largerThan)
			throws TypeOneException {
		if(pivotNum < 0)
			throw new TypeOneException();
		larger = largerThan;
		pivotNum = pivot;
		
	}
	
	/**
	 * Checks that 'size' is between 'smallerThan' and 'biggerThan'
	 * 
	 * @param size
	 * @return true if size is in bounds. false otherwise.
	 */
	public boolean inRange(double size){
		return ( (larger && (size > pivotNum)) ||
				(!larger && (size < pivotNum)) );
	}

	@Override
	public boolean isPass(File toCheck) {
		return inRange((double)toCheck.length()/KILOBYTES);
	}


}
