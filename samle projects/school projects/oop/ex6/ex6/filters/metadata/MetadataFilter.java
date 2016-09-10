package oop.ex6.filters.metadata;

import oop.ex6.filters.Filter;
import oop.ex6.specialExceptions.TypeOneException;

/**
 * An abstract father to all meta data type filters
 * 
 * @author Adir
 *
 */
public abstract class MetadataFilter implements Filter {
	private static String YES = "YES";
	private static String NO = "NO";
	
	protected boolean yesOrNo;
	
	/**
	 * Constructor. receives 'YES' or 'NO', according to what we want the meta
	 * data to be.
	 * 
	 * @param meta - 'YES' or 'NO'. Error otherwise
	 * @throws TypeOneException if the parameter is wrong
	 */
	public MetadataFilter(String meta) throws TypeOneException{
		if(meta.equals(YES)){
			yesOrNo = true;
		}else{
			if(meta.equals(NO)){
				yesOrNo = false;
			}else{
				throw new TypeOneException();
			}
		}
	}

}
