package oop.ex6.filters.metadata;

import java.io.File;

import oop.ex6.specialExceptions.TypeOneException;

/**
 * A filter that checks if the file is writable. 
 * 
 * @author Adir
 *
 */
public class WritableFilter extends MetadataFilter {

	/**
	 * Constructor. receives 'YES' or 'NO', according to what we want the meta
	 * data to be.
	 * 
	 * @param meta - 'YES' or 'NO'. Error otherwise
	 * @throws TypeOneException  if the parameter is wrong
	 */
	public WritableFilter(String isWritable) throws TypeOneException {
		super(isWritable);
	}

	@Override
	public boolean isPass(File toCheck) {
		return (toCheck.canWrite() == yesOrNo);
	}

}
