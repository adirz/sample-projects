package oop.ex6.specialExceptions;

import java.util.ArrayList;

/**
 * ExceptionHolder. My way of throwing multiple exceptions. Even tough we use
 * up to two exception it is made for the general case, as we are asked to
 * make our classes as independent as possible.
 * 
 * @author Adir
 *
 */
public class ExceptionHolder {
	
	private boolean areThereAny;
	private ArrayList<TypeOneException> exceptions;
	
	/**
	 * Constructor. creates a new exception ArrayList, without any in it.
	 */
	public ExceptionHolder(){
		areThereAny = false;
		exceptions = new ArrayList<TypeOneException>();
	}
	
	/**
	 * Adds exc to the exception array
	 * @param exc - a new exception to be added
	 */
	public void add(TypeOneException exc){
		areThereAny = true;
		exceptions.add(exc);
	}
	
	/**
	 * adds all the exceptions in exc to the end of our list.
	 * @param exc - a holder with exceptions to be added.
	 */
	public void add(ExceptionHolder exc){
		areThereAny = true;
		exceptions.addAll(exc.get());
	}
	
	/**
	 * @return exceptions that we have been holding
	 */
	public ArrayList<TypeOneException> get(){
		return exceptions;
	}
	
	/**
	 * @return if we hold any exceptions
	 */
	public boolean anyExceptions(){
		return areThereAny;
	}
}
