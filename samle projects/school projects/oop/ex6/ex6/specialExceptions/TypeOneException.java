package oop.ex6.specialExceptions;

/**
 * all of type one errors, as asked in the exercise.
 * 
 * @author Adir
 *
 */
public class TypeOneException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4085348309558517957L;
	
	private int lineOfWarning;
	
	/**
	 * Constructor. creates this new exception.
	 */
	public TypeOneException() {
		lineOfWarning = 0;
	}
	
	@Override
	public String toString(){
		return "Warning in line " + lineOfWarning;
	}

	/**
	 * as the exception travel upwards, it reveals it true line.
	 * @param line
	 */
	public void addLineNum(int line) {
		lineOfWarning += line;
	}
	
}
