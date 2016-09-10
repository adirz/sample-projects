package oop.ex6.filters;

import java.io.File;

/**
 * An anti filter. Passes anything that 'deco' blocks, blocks anything that
 * 'deco'passes.
 * 
 * @author Adir
 *
 */
public class NotFilter implements Filter {
	private Filter deco;
	
	/**
	 * Constructor. 
	 * @param filter - A filter that's the opposite of what we want to pass.
	 */
	public NotFilter(Filter filter){
		deco = filter;
	}

	@Override
	public boolean isPass(File toCheck) {
		return !deco.isPass(toCheck);
	}
}
