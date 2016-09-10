package oop.ex6.order;

import java.io.File;
import java.util.LinkedList;

/**
 * An abstract class that stores the order of files as we wish it.
 * 
 * @author Adir
 *
 */
public abstract class Order {

	protected LinkedList<File> files;
	
	/**
	 * Constructor. Starts the linked list, which is linked by the desired
	 * order.
	 */
	public Order(){
		files = new LinkedList<File>();
	}

	/**
	 * @param one - the file already in the list.
	 * @param two - the file to check if we insert him here.
	 * @return if by this order one comes before two
	 */
	public abstract boolean oneIsFirst(File one, File two);
	
	/**
	 * Add a file to its correct place in the order at 'files' linked list.
	 * 
	 * @param file - the file to add.
	 */
	public void add(File file){
		/*
		 *  I search for the file location horizontally because it is a linked
		 *  list, so doing a binary search would take more time- it would need
		 *  to run till the point we check each time.
		 */
		if(files.isEmpty()){
			files.add(file);
		}else{
			int index = 0;
			for(File linked : files){
				if(oneIsFirst(linked, file)){
					break;
				}else
					index ++;
			}
			files.add(index, file);
		}
	}

	/**
	 * Converts the linked list to an easy to work with array of names of
	 * files, in the same order, to be returned.
	 * @return an array of files names.
	 */
	public String[] print() {
		String[] fileNames = new String[files.size()];
		int index = 0;
		for(File file:files){
			fileNames[index] = file.getName();
			index ++;
		}
		return fileNames;
	}
}
