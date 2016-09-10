
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * This helper class will allow you to create Latex document to show your MathTerm.
 * 
 * @author oop
 *
 */
public class LatexDocument {
	private String title;
	private String author;
	
	private final static String DOCUMENT_CLASS = "\\documentclass[12pt]{article}";
	
	
	private ArrayList<String> mathTerms;
	
	/**
	 * Constructs a new document.
	 * 
	 * @param documentTitle The title of the document.
	 * @param documentAuthor The author of the document.
	 */
	public LatexDocument(String documentTitle, String documentAuthor) {
		this.title = documentTitle;
		this.author = documentAuthor;
		
		mathTerms = new ArrayList<String>();
	}
	
	/**
	 * Appends a new MathTerm to the document.
	 * 
	 * @param newMathTerm The MathTerm to be appended.
	 */
	public void appendMathTerm(MathTerm newMathTerm) {
		mathTerms.add(newMathTerm.toLatex());
	}
	
	/**
	 * Creates a new Tex file.
	 * 
	 * @param filename The filename of the file to be created.
	 */
	public void createLatexdDocument(String filename) {
		
		String sysNewLine = System.getProperty("line.separator");
		
		try {
			BufferedWriter latexWriter = new BufferedWriter(new FileWriter(filename));
			
			latexWriter.write(DOCUMENT_CLASS + sysNewLine);
			latexWriter.write("\\begin{document}" + sysNewLine);
			latexWriter.write("\\title{" + title + "}" + sysNewLine);
			latexWriter.write("\\author{" + author + "}" + sysNewLine);
			latexWriter.write("\\maketitle" + sysNewLine);
			
			for (String curTerm : mathTerms) {
				latexWriter.write("\\begin{equation}" + sysNewLine);
				latexWriter.write(curTerm  + sysNewLine);
				latexWriter.write("\\end{equation}" + sysNewLine);
			}
			
			latexWriter.write("\\end{document}" + sysNewLine);
			latexWriter.close();
			
		} catch (IOException e) {
			System.out.println("ERROR: Error creating Latex file.");
		}
		
		
		
	}
	
	

}
