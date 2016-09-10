package oop.ex6.order;

import java.io.File;

/**
 * An order that is a reverse of another one.
 * 
 * @author Adir
 *
 */
public class ReverseOrder extends Order{
	private Order deco;
	
	/**
	 * Constructor. Holds 'reverse' as the order to return the opposite of.
	 * 
	 * @param reverse an order to return the opposite of.
	 */
	public ReverseOrder(Order reverse){
		deco = reverse;
	}
	
	@Override
	public boolean oneIsFirst(File one, File two) {
		return !deco.oneIsFirst(one, two);
	}
	
}
