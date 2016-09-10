package oop.ex6.filters.metadata;

import java.io.File;

import oop.ex6.specialExceptions.TypeOneException;

/**
 * A filter that checks if the file can be executed.
 * 
 * @author Adir
 *
 */
public class ExecutableFilter extends MetadataFilter {

	/**
	 * Constructor. receives 'YES' or 'NO', according to if we want the file
	 * to be executable.
	 * 
	 * @param meta - 'YES' or 'NO'. Causes error otherwise.
	 * @throws TypeOneException if the parameter is wrong.
	 */
	public ExecutableFilter(String isExecutable) throws TypeOneException {
		super(isExecutable);
	}

	@Override
	public boolean isPass(File toCheck) {
		return (toCheck.canExecute() == yesOrNo);
	}

}
