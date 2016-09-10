package oop.ex6.filters;

import java.util.Hashtable;

import oop.ex6.filters.metadata.ExecutableFilter;
import oop.ex6.filters.metadata.HiddenFilter;
import oop.ex6.filters.metadata.WritableFilter;
import oop.ex6.filters.name.ContainFilter;
import oop.ex6.filters.name.MatchNameFilter;
import oop.ex6.filters.name.PrefixFilter;
import oop.ex6.filters.name.SuffixFilter;
import oop.ex6.filters.size.BetweenFilter;
import oop.ex6.filters.size.SizeFilter;
import oop.ex6.specialExceptions.TypeOneException;

/**
 * A static class, that creates the appropriate type of filter according to
 * the filter name and variables.
 * 
 * @author Adir
 *
 */
public class FilterFactory {
	private static String NOT = "NOT";
	
	/**
	 * An interface for all the different filters builder
	 * 
	 * @author Adir
	 *
	 */
	private static interface BuildFilter{
		/**
		 * Filter builder - a easy to handle builder where we can easily add
		 * new filters to build.
		 * 
		 * @param args - the filter line, broke down to name and variables.
		 * @return a new  filter, according to the filter name
		 * @throws TypeOneException in case of bad variables.
		 */
		public Filter build(String[] args) throws TypeOneException;
	}
	
	/**
	 * Creates an array that each of its parts holds a builder to a different
	 * filter.
	 */
	private static BuildFilter[] myBuilder = new BuildFilter[]{
		new BuildFilter(){
			public Filter build(String[] args) throws TypeOneException {
				return new SizeFilter(Double.parseDouble(args[1]), true); } },
		new BuildFilter(){
			public Filter build(String[] args) throws TypeOneException {
				return new BetweenFilter(Double.parseDouble(args[1]),
						Double.parseDouble(args[2])); } },
		new BuildFilter(){
			public Filter build(String[] args) throws TypeOneException {
				return new SizeFilter(Double.parseDouble(args[1]), false);} },
		new BuildFilter(){
			public Filter build(String[] args) {
				return new MatchNameFilter(args[1]); } },
		new BuildFilter(){
			public Filter build(String[] args) {
				return new ContainFilter(args[1]); } },
		new BuildFilter(){
			public Filter build(String[] args) {
				return new PrefixFilter(args[1]); } },
		new BuildFilter(){
			public Filter build(String[] args) {
				return new SuffixFilter(args[1]); } },
		new BuildFilter(){
			public Filter build(String[] args) throws TypeOneException {
				return new WritableFilter(args[1]); } },
		new BuildFilter(){
			public Filter build(String[] args) throws TypeOneException {
				return new ExecutableFilter(args[1]); } },
		new BuildFilter(){
			public Filter build(String[] args) throws TypeOneException {
				return new HiddenFilter(args[1]); } },
		new BuildFilter(){
			public Filter build(String[] args) {
				return new AllFilter(); } }
	};
	
	private static final Hashtable<String, Integer> FILTERS = 
			filterOptions();
	
	/**
	 * 
	 * @return the static hash table that maps filters names into their place
	 * in the BuildFilter array.
	 */
	private static Hashtable<String, Integer> filterOptions(){
		/*
		 * I didn't saw a reason to put the filter names in MAGIC_NUM, because
		 * its already a magic number as part of the filterOptions hash table.
		 */
		Hashtable<String, Integer> options = new Hashtable<String, Integer>();
		options.put("greater_than", 0);
		options.put("between", 1);
		options.put("smaller_than", 2);
		options.put("file", 3);
		options.put("contains", 4);
		options.put("prefix", 5);
		options.put("suffix", 6);
		options.put("writable", 7);
		options.put("executable", 8);
		options.put("hidden", 9);
		options.put("all", 10);
		return options;
	}

	/**
	 * creates a more easy to work with array of strings rather than all
	 * combined with '#' between them
	 * @param filterString - the line of the filter
	 * @return the line as different parts.
	 */
	private static String[] breakToFilters(String filterString){
		return filterString.split("#");
	}
	
	/**
	 * Builds a filter of our likings. If error type one is found, builds 
	 * "allFilter" and throws exception
	 * 
	 * @param filterString - a string of one of those formats:
	 * "<filter name>#<val 1>" or "<filter name>#<val 1>#NOT" or
	 * "<filter name>#<val 1>#<val 2>" or "<filter name>#<val 1>#<val 2>#NOT"
	 * 
	 * @return the appropriate filter, with the correct variables.
	 * @throws TypeOneException in case of bad parameters or bad filter name
	 */
	public static Filter createFilter(String filterString)
			throws TypeOneException{
		String[] filters = breakToFilters(filterString);
		Integer filterNum = FILTERS.get(filters[0]);
		Filter filter;
		if(filterNum == null){
			throw new TypeOneException();
		}else{
			filter = myBuilder[filterNum].build(filters);
		}
		if(filters[filters.length-1].equals(NOT))
			return new NotFilter(filter);
		return filter;
	}
}
