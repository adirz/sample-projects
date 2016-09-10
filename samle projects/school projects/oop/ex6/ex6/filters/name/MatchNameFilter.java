package oop.ex6.filters.name;

/**
 * A filter that checks if the file's name matches a certain String.
 * 
 * @author Adir
 *
 */
public class MatchNameFilter extends NameFilter {

	/**
	 * Constructor. receives a string that is be the passing file name.
	 * 
	 * @param fileName - the String to match the name.
	 */
	public MatchNameFilter(String fileName) {
		super(fileName);
	}

	@Override
	public boolean filterWord(String name) {
		return name.equals(toCompare);
	}

}
