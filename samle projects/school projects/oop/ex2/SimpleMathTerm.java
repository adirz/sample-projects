
public class SimpleMathTerm extends MathTerm {
	
	private boolean numeric;
	private double num;
	private int precisionDigits;
	private boolean setPrecision;
	private String term;
	
	public SimpleMathTerm(String termName){
		term = termName;
		setPrecision = false;
		numeric = true;
		try  
		{
			num = Double.parseDouble(termName);  
		}  
		catch(NumberFormatException nfe)  
		{  
			numeric = false;  
		} 
	}
	
	public boolean isNumeric(){
		return numeric;
	}
	
	public void setPrecisionDigits(int precisionDigits){
		this.precisionDigits = precisionDigits;
		setPrecision = true;
	}
	
	public String toLatex(){
		String expon = "", latex ="";
		if(exponent != null){
			expon ="^{ "+exponent.toLatex()+" }";
		}
		if(numeric && setPrecision){
			int floatPoint = term.indexOf(".");
			int cutNum = floatPoint;
			if(precisionDigits > 0){
				cutNum +=  precisionDigits + 1;
			}
			if(cutNum > term.length()){
				cutNum = term.length();
			}
			latex = term.substring(0, cutNum)+expon;
		}else{
			latex = term+expon;
		}
		if(isBarred){
			latex = "\\overline{ " + latex +" }";
		}
		if(isNegated){
			latex = "\\neg{ "+ latex + " }";
		}
		return latex;
	}
}
