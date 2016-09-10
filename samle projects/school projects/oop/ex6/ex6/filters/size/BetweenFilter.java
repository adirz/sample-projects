package oop.ex6.filters.size;

import java.io.File;

import oop.ex6.filters.Filter;
import oop.ex6.specialExceptions.TypeOneException;

public class BetweenFilter implements Filter{
	private static final int KILOBYTES = 1024;// 1 kilobyte  = 1024 bytes
	private double lowerBound;
	private double higherBound;
	
	/**
	 * Constructor. gets both number we want our file size to be between.
	 * 
	 * @param mini - lower bound
	 * @param maxi - higher bound
	 * @throws TypeOneException in case of bad parameters
	 */
	public BetweenFilter(double mini, double maxi) throws TypeOneException {
		if(mini < 0 || maxi < 0 || maxi < mini)
			throw new TypeOneException();
		lowerBound = mini;
		higherBound = maxi;
	}

	@Override
	public boolean isPass(File toCheck) {
		double size = (double)toCheck.length()/KILOBYTES;
		return (size >= lowerBound && size <= higherBound);
	}

}
