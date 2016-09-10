
public class BracketsMathTerm extends MathTerm {
	
	private MathTerm internalTerm;
	
	public BracketsMathTerm(MathTerm internalTerm){
		this.internalTerm = internalTerm;
	}
	
	public String toLatex(){
		return "\\left( " + internalTerm.toLatex() + " \\right)";
	}
}
