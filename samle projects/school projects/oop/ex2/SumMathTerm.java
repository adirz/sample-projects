
public class SumMathTerm extends MathTerm {
	
	private MathTerm subTerm, superTerm, sumTerm;
	
	public SumMathTerm(MathTerm subTerm,MathTerm superTerm, MathTerm sumTerm){
		this.subTerm = subTerm;
		this.superTerm = superTerm;
		this.sumTerm = sumTerm;
	}
	
	public String toLatex(){
		return "\\sum_{ "+subTerm.toLatex()+" }^{ "+superTerm.toLatex()+" }{ "
		+ sumTerm.toLatex() + " }";
	}
}
