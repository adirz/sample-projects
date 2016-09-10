
public class FractionMathTerm extends BinaryMathTerm {

	public FractionMathTerm(MathTerm firstTerm,MathTerm secondTerm){
		super(firstTerm, secondTerm);
	}
	
	public String toLatex(){
		return "\\frac{ "+firstTerm.toLatex()+" }{ "+ secondTerm.toLatex() +" }";
	}

}
