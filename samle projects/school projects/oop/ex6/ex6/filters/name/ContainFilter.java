package oop.ex6.filters.name;

/**
 * A filter that checks if the file's name contain a certain String.
 * 
 * @author Adir
 *
 */
public class ContainFilter extends NameFilter {

	/**
	 * Constructor. receives a string that should be in the passing files'
	 * names.
	 * 
	 * @param filterWord - the String to check in the name.
	 */
	public ContainFilter(String filterWord) {
		super(filterWord);
	}

	@Override
	public boolean filterWord(String name) {
		return name.contains(toCompare);
	}

}
