package oop.ex6.specialExceptions;

/**
 * all of type two errors, as asked in the exercise.
 * 
 * @author Adir
 *
 */
public class TypeTwoException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8625058337569635762L;
	
	public static final String ERROR_MSG = "ERROR";

	/**
	 * Constructor. creates this new exception.
	 */
	public TypeTwoException(){
		super(ERROR_MSG);
	}

	/*
	 * UNUSED. create error with the testers, but is a part of the project.
	 *
	public static void TheresAnError(){
		System.err.println(ERROR_MSG);
		System.exit(-1);
	}
	*/
}
