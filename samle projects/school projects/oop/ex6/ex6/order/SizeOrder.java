package oop.ex6.order;

import java.io.File;

/**
 * An Order type class that sorts the files by their size.
 * 
 * @author Adir
 *
 */
public class SizeOrder extends Order {
	
	@Override
	public boolean oneIsFirst(File one, File two){
		long spaceDifference = one.length() - two.length();
		if(spaceDifference < 0){
			return false;
		}else if(spaceDifference > 0)
			return true;
		return new AbsOrder().oneIsFirst(one, two);
	}
}
