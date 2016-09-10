
public class MathTerm {
	
	protected MathTerm exponent;
	protected boolean isBarred;
	protected boolean isNegated;
	
	public MathTerm(){
	}
	
	public void setExponentTerm(MathTerm exponentTerm){
		exponent = exponentTerm;
	}
	
	public MathTerm getExponentTerm(){
		return exponent;
	}
	
	public void setIsBarred(boolean isBarred){
		this.isBarred = isBarred;
	}
	
	public boolean getIsBarred(){
		return isBarred;
	}
	
	public void setIsNegated(boolean isNegated){
		this.isNegated = isNegated;
	}
	
	public boolean getIsNegated(){
		return isNegated;
	}
	
	public String toLatex(){
		return "";
	}
}
