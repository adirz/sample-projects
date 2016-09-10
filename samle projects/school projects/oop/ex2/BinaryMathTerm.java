
public class BinaryMathTerm extends MathTerm {
	
	protected MathTerm firstTerm;
	protected MathTerm secondTerm;
	
	BinaryMathTerm(MathTerm firstTerm, MathTerm secondTerm){
		this.firstTerm = firstTerm;
		this.secondTerm = secondTerm;
	}
	
	public String toLatex(){
		return "";
	}
}
