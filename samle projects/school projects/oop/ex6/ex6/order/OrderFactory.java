package oop.ex6.order;

import java.util.Hashtable;

import oop.ex6.specialExceptions.TypeOneException;

/**
 * A static class, that creates the appropriate type of order according to the
 * order name and variables.
 * 
 * @author Adir
 *
 */
public class OrderFactory {
	private static final Hashtable<String, Integer> ORDERS = orderOptions();
	private static final String REVERSE = "REVERSE";
	
	/**
	 * 
	 * @return the static hash table that maps order names into their place
	 * in the BuildOrder array.
	 */
	private static Hashtable<String, Integer> orderOptions(){
		Hashtable<String, Integer> options = new Hashtable<String, Integer>();
		options.put("abs", 0);
		options.put("type", 1);
		options.put("size", 2);
		return options;
	}

	/**
	 * Creates a more easy to work with array of strings rather than all
	 * combined with '#' between them
	 * @param orderString - the line of the order
	 * @return the line as different parts.
	 */
	private static String[] breakToOrders(String orderString){
		return orderString.split("#");
	}

	/**
	 * An interface for all the different orders builder
	 * 
	 * @author Adir
	 *
	 */
	private static interface BuildOrder{
		public Order build();
	}

	/**
	 * Creates an array that each of its parts holds a builder to a different
	 * order.
	 */
	private static BuildOrder[] myBuilder = new BuildOrder[]{
		new BuildOrder(){
			@Override
			public Order build() {
				return new AbsOrder();}},
		new BuildOrder(){
			@Override
			public Order build() {
				return new TypeOrder();}},
		new BuildOrder(){
			@Override
			public Order build() {
				return new SizeOrder();}}
	};
	/**
	 * Creates an order variable of one of the order classes, by interpreting
	 * the given parameter "orderLine". If it is blank than gets the abs order
	 * @param orderLine - The line in the file referring to the order.
	 * @return an order sub-type matching what needed
	 * @throws TypeOneException in case of bad parameters or bad order name
	 */
	public static Order createOrder(String orderLine) throws TypeOneException{
		if(orderLine == "")
			return new AbsOrder();
		String[] orders = breakToOrders(orderLine);
		Integer orderNum = ORDERS.get(orders[0]);
		Order order;
		if(orderNum == null){
			throw new TypeOneException();
		}else{
			order = myBuilder[orderNum].build();
		}
		if(orders.length == 2){
			if(orders[1].equals(REVERSE))
				return new ReverseOrder(order);
			else{
				throw new TypeOneException();
			}
		}
		return order;
	}

}
