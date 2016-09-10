
public class SimpleBinaryOpMathTerm extends BinaryMathTerm {
	
	private String sign;
	private static char MULTIPLY = "*".charAt(0);

	public SimpleBinaryOpMathTerm(MathTerm firstTerm,
			MathTerm secondTerm,char sign){
		super(firstTerm, secondTerm);
		if(sign == MULTIPLY){
			this.sign = " \\cdot ";
		}else{
			this.sign = String.valueOf(sign);
		}
	}
	
	public String toLatex(){
		return firstTerm.toLatex() + sign + secondTerm.toLatex();
	}

}
