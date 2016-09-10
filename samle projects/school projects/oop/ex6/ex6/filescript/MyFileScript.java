package oop.ex6.filescript;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import oop.ex6.filters.Filter;
import oop.ex6.order.Order;
import oop.ex6.sections.Section;
import oop.ex6.sections.SectionsFactory;
import oop.ex6.specialExceptions.TypeOneException;
import oop.ex6.specialExceptions.TypeTwoException;
/**
 * The front gate of the program. Takes the arguments, and construct them as
 * the proper files. Than activates the manger.
 * 
 * @author Adir
 * 
 */
public class MyFileScript {
	private static final int ALLOWED_ARGUMENTS = 2;
	private static ArrayList<File> allFiles;
	private static ArrayList<String> commandFile;
	
	/**
	 * Adds all the files inside the given directory and its sub-directories
	 * to all Files.
	 * 
	 * @param folder the source directory to explore
	 */
	private static void listFilesForFolder(final File folder) {
		File[] files = folder.listFiles();
		if(files != null ){
		    for (File fileEntry : folder.listFiles()) {
		        if (fileEntry.isDirectory()) {
		            listFilesForFolder(fileEntry);
		        } else {
		        	allFiles.add(fileEntry);
		        }
		    }
		}
	}
	
	/**
	 * Converts all the lines in the file via BufferedReader into an array of
	 * strings, each string represent a line in the filter file.
	 * 
	 * @param reader - the constant to buffer the command file.
	 * 
	 * @throws IOException if the file location is wrong and can't be read.
	 * 						We were told to except it to be legitimate.
	 */
	private static void listCommands(BufferedReader
			reader) throws IOException{
		String tempLine = reader.readLine();
		while(tempLine != null){
			commandFile.add(tempLine);
			tempLine = reader.readLine();
		}
	}
	
	/**
	 * Main method of the program. Gets as arguments the locations of the
	 * command file, prints out the files that pass filters in the correct
	 * order. According to the EX6_new: "You may assume Source Directory is an
	 * existing directory and Commands File is an existing file".
	 * 
	 * @param args -first should be the location of the source directory.
	 * 				second should be the location of the command file.
	 */
	public static void main(String[] args) {
		if(args.length == ALLOWED_ARGUMENTS){
			allFiles = new ArrayList<File>();
			File source = new File(args[0]);
			if(!source.isDirectory()){
				allFiles.add(source);
			}else
				listFilesForFolder(source);
			FileReader fileReader;
			try {
				fileReader = new FileReader(args[1]);
				BufferedReader bufferedReader =
						new BufferedReader(fileReader);
				commandFile = new ArrayList<String>();
				try {
					listCommands(bufferedReader);
					printAnswer();
				} catch (IOException e) {
					// in the work assignment it says it shoudn't be a problem
					System.err.println(TypeTwoException.ERROR_MSG);
				}
			} catch (FileNotFoundException e1) {
				// in the work assignment it says it shoudn't be a problem
				System.err.println(TypeTwoException.ERROR_MSG);
			}
		}else
			System.err.println(TypeTwoException.ERROR_MSG);
	}
	
	/**
	 * After everything been built, orders and filters, it goes on the files
	 * that passed each filter and print them in the order told.
	 */
	private static void printAnswer() {
		SectionsFactory secFac = new SectionsFactory();
		try {
			secFac.buildSections(commandFile);
			ArrayList<Section> secs = secFac.getSections();
			for(Section section: secs){
				if(section.isWarning()){
					for(TypeOneException warning: section.getExceptions().get()) {
						System.err.println(warning);
					}
				}
				Filter filter = section.getFilter();
				Order order = section.getOrder();
				for(File file: allFiles){
					if(filter.isPass(file)){
						order.add(file);
					}
				}
				String[] lines = order.print();
				for(String line : lines){
					System.out.println(line);
				}
			}
		} catch (TypeTwoException e) {
			System.err.println(e.getMessage());
			// I do nothing because the exist won't work
		}
	}
}
