package oop.ex6.sections;

import java.util.ArrayList;

import oop.ex6.specialExceptions.ExceptionHolder;
import oop.ex6.specialExceptions.TypeTwoException;

/**
 * A class that converts a the given command file (as ArrayList<String> lines)
 * to section array.
 * 
 * @author Adir
 *
 */
public class SectionsFactory {
	private static final String FILTER = "FILTER";
	private static final String ORDER = "ORDER";
	private static final int STEP_1 = 1;
	private static final int STEP_2 = 2;
	private static final int STEP_3 = 3;
	private static final int STEP_4 = 4;
	
	private ArrayList<Section> sections;
	
	public SectionsFactory(){
		sections = new ArrayList<Section>();
	}
	
	/**
	 * 
	 * @param lines - all the lines the command file is made of.
	 * @return an ArrayList of the matching sections
	 * @throws TypeTwoException in case of missing title or bad ones
	 * @throws ExceptionHolder in case of bad parameters or bad names
	 */
	public void buildSections(ArrayList<String> lines) throws TypeTwoException {
		String tempFilterLine = "";
		String tempOrderLine = "";
		int step = STEP_1;
		int lineNum = 0;
		/*
		 * Steps:
		 * 1) FILTER
		 * 2) filter command
		 * 3) ORDER
		 * 4) optional: order command
		 */
		for(String line : lines){
			lineNum ++;
			if(step == STEP_1){
				if(line.equals(FILTER)){
					tempFilterLine = "";
					tempOrderLine = "";
					step ++;
				}else
					throw new TypeTwoException();
//					TypeTwoException.TheresAnError();
			}else if(step == STEP_2){
				tempFilterLine = line;
				step ++;
			}else if(step == STEP_3){
				if(!line.equals(ORDER)){
					throw new TypeTwoException();
//					TypeTwoException.TheresAnError();
				}else
					step ++;
			}else if(step == STEP_4){
				if(line.equals(FILTER)){
					sections.add(new Section(tempFilterLine, tempOrderLine,
							lineNum - step));
					tempFilterLine = "";
					tempOrderLine = "";
					step = STEP_2;
				}else{
					tempOrderLine = line;
					sections.add(new Section(tempFilterLine, tempOrderLine,
							lineNum - step));
					step = STEP_1;
				}
			}
		}
		lineNum ++;
		if(step == STEP_4){
			sections.add(new Section(tempFilterLine, tempOrderLine, lineNum 
					- step));
		}else if(step == STEP_2 || step == STEP_3){
			throw new TypeTwoException();
//			TypeTwoException.TheresAnError();
		}
	}
	
	/**
	 * @return sections - All the Section variables created.
	 */
	public ArrayList<Section> getSections(){
		return sections;
	}
}
