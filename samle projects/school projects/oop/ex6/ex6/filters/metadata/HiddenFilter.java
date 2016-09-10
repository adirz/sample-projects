package oop.ex6.filters.metadata;

import java.io.File;

import oop.ex6.specialExceptions.TypeOneException;

/**
 * A filter that checks if the file is hidden.
 * 
 * @author Adir
 *
 */
public class HiddenFilter extends MetadataFilter {

	/**
	 * Constructor. receives 'YES' or 'NO', according to what we want the meta
	 * data to be.
	 * 
	 * @param meta - 'YES' or 'NO'. Error otherwise
	 * @throws TypeOneException  if the parameter is wrong
	 */
	public HiddenFilter(String isHidden) throws TypeOneException {
		super(isHidden);
	}

	@Override
	public boolean isPass(File toCheck) {
		return (toCheck.isHidden() == yesOrNo);
	}

}
