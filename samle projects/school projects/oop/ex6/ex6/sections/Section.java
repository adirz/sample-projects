package oop.ex6.sections;

import oop.ex6.filters.AllFilter;
import oop.ex6.filters.Filter;
import oop.ex6.filters.FilterFactory;
import oop.ex6.order.AbsOrder;
import oop.ex6.order.Order;
import oop.ex6.order.OrderFactory;
import oop.ex6.specialExceptions.ExceptionHolder;
import oop.ex6.specialExceptions.TypeOneException;

/**
 * A class that holds filter and order of each section in the command file.
 * 
 * @author Adir
 *
 */
public class Section {
	
	private static final int FILTER_LINE = 2;
	private static final int ORDER_LINE = 4;
	
	private Filter filter;
	private Order order;
	private ExceptionHolder myExc;
	
	/**
	 * Constructor. Sets the filter and order according to their lines.
	 * @param FilterLine - the line to create the filter
	 * @param OrderLine - the line to create the order
	 * @param secLine - the line the section is in the file
	 * @throws TypeOneException in case of bad parameters or bad name
	 */
	public Section(String FilterLine, String OrderLine, int secLine) {
		myExc = new ExceptionHolder();
		try {
			filter = FilterFactory.createFilter(FilterLine);
		} catch (TypeOneException e) {
			filter = new AllFilter();
			e.addLineNum(FILTER_LINE + secLine);
			myExc.add(e);
		}
		try {
			order = OrderFactory.createOrder(OrderLine);
		} catch (TypeOneException e) {
			order = new AbsOrder();
			e.addLineNum(ORDER_LINE + secLine);
			myExc.add(e);
		}
	}
	
	/**
	 * @return this section's filter
	 */
	public Filter getFilter() {
		return filter;
	}

	/**
	 * @return this section's order
	 */
	public Order getOrder(){
		return order;
	}
	
	/**
	 * @return the exceptions it holds
	 */
	public ExceptionHolder getExceptions(){
		return myExc;
	}
	
	/**
	 * @return if there is a warning regarding this section
	 */
	public boolean isWarning() {
		return myExc.anyExceptions();
	}
}
