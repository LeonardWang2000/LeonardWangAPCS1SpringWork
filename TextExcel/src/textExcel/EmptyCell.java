//APCS1 Leonard Wang 1stperiod
package textExcel;
public class EmptyCell implements Cell {
	//emptycell returns empty strings for the cells
	public String abbreviatedCellText() {
		//10 spaces according to instructions
		return "          ";
	}

	public String fullCellText() {
		return "";
	}
	public EmptyCell(){
	}
}