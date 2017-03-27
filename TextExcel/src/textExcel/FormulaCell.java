//APCS1 Leonard Wang 1stperiod
package textExcel;
public class FormulaCell extends RealCell{
	private String value = "";
	public FormulaCell(String input){
		value = input;
		//stores string in parent class
		setRealCell(value);
	}
	public double getDoubleValue(){
		String[] changeValue = value.split(" ");
		double finalValue = 0.0;
		for(int i = 0; i < changeValue.length; i+=2){
			if(changeValue[i+1].equals("+")){
				finalValue += Double.parseDouble(changeValue[i]);
			} else if(changeValue[i+1].equals("*")){
				finalValue *= Double.parseDouble(changeValue[i]);
			} else if(changeValue[i+1].equals("/")){
				finalValue /= Double.parseDouble(changeValue[i]);
			} else{
				finalValue -= Double.parseDouble(changeValue[i]);
			}
		}
		return finalValue;
	}
	public String abbreviatedCellText(){
		return value;
	}
	public String fullCellText(){
		return value;
	}
}
