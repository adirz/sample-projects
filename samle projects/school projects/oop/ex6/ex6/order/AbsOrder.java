package oop.ex6.order;

import java.io.File;

/**
 * An order type class, sort the by their absolute path
 * 
 * @author Adir
 *
 */
public class AbsOrder extends Order {

	@Override
	public boolean oneIsFirst(File one, File two) {
		return one.getAbsolutePath().compareTo(two.getAbsolutePath()) > 0;
	}
}
