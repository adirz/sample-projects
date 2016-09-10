package oop.ex6.order;

import java.io.File;

/**
 * An Order type class that sorts the files by their type.
 * 
 * @author Adir
 *
 */
public class TypeOrder extends Order {

	/**
	 * A function that from the name of the file, extracts it extension.
	 * @param name - the name of the file.
	 * @return the file's type.
	 */
	private String typeByName(String name){
		String[] s = name.split("\\.");
		return s[s.length-1];
	}
	
	@Override
	public boolean oneIsFirst(File one, File two) {
		int first = typeByName(one.getName()).compareTo(
					typeByName(two.getName()));
		if(first > 0)
			return true;
		else if(first < 0)
			return false;
		return new AbsOrder().oneIsFirst(one, two);
	}

}
