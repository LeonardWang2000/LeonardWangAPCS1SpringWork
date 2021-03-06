//APCS1 Leonard Wang 1stperiod
package textExcel;
import java.io.*;
import java.util.Scanner;
public class Spreadsheet implements Grid
{
	public Spreadsheet(){
		//fills spreadsheet with emptycells
		 for(int i = 0; i < rows; i++){
			 for(int j = 0; j < cols; j++){
				 arrayOfStuff[i][j] = new EmptyCell();
			 }
		 }
	}
	private int rows = 20;
	private int cols = 12;
	private Cell [][] arrayOfStuff = new Cell[rows][cols];
	//2d array for rows and columns
	public String processCommand(String command)
	{
		
		String[] splitCommand = command.split(" ");
		if(command.contains("open")){
			return (readFromFile(splitCommand[1]));
		}
		if(command.contains("save")){
			return (writeToFile(splitCommand[1]));
		}
		//in case of lower case
		splitCommand[0] = splitCommand[0].toUpperCase();
		if(command.length()==0){
			return "";
		} else if(splitCommand.length >= 3){
			String userInput = splitCommand[2] + "";
			int counter = 3;
			while(counter < splitCommand.length){
				//adds the space that was taken out and the next part of the value
				userInput += " " + splitCommand[counter];
				counter++;
			}
			String cell = splitCommand[0];
			cellAssignment(userInput, cell);
			return getGridText();
			//if its less than 3, it has to be cell inspection
		}else if(command.length() <= 3){
			return cellInspection(splitCommand[0]);
			//check if the user input has clear, has been changed to uppercase
		} else if (splitCommand[0].contains("CLEAR")){
			//if theres no spaces, then must be just clear so clear entire cell
			if(splitCommand.length == 1){
				 clearEntireCell();
				 return getGridText();
				 //if there is space, then must be clearing just one spot
			} else{
				//in case the cell isnt uppercased
				clearOneCell(splitCommand[1].toUpperCase());
				return getGridText();
			}
		}
		return "";
	}
	
	public int getRows()
	{
		return this.rows;
	}


	public int getCols()
	{
		return this.cols;
	}

	public Cell getCell(Location loc)
	{
		return arrayOfStuff[loc.getRow()][loc.getCol()];
	}
	
	public String getGridText()
	{
		String grid = "   |";
		//fills in the top row with the letters
		for(int i = 0; i < cols; i++){
			//cast to character type for letters, add the 10 spaces between this and the next one
			grid += (char) ('A' + i) + "         |";
		}
		
		//fills in the grid
		for(int i = 1; i <= rows; i++){
			//makes new line at end of row and adds the number
			grid += "\n" + i;
			//fixes the spacing when the numbers hit double digits
			if(i >= 10){
				grid += " |";
			}else{
				grid += "  |";
			}
			
			//sets all the values of each part of the array
			for(int k = 0; k < cols; k++){
				//includes the dashed lines at the end, only lets first 10 characters show
				//Puts the cell with its values in the grid
				//i-1 because i started at 1 for numbering but arrays are zero based
				grid += arrayOfStuff[i-1][k].abbreviatedCellText() + "|";
			}
		}
		// skips to next line after finishing creating the grid
		grid += "\n";
		return grid;
	}
	public String cellInspection(String cell){
		//makes new spreadsheetlocation object to get the rows and col
		SpreadsheetLocation a = new SpreadsheetLocation(cell);
		String result = arrayOfStuff[a.getRow()][a.getCol()].fullCellText();
		return result;
	}
	//assigns cell using Textcell constructor
	public void cellAssignment(String input, String cell){
		SpreadsheetLocation b = new SpreadsheetLocation(cell);
		//assigns cell accordingly to distinct characteristics
		if(input.contains("%")){
			arrayOfStuff[b.getRow()][b.getCol()] = new PercentCell(input);
		} else if(input.contains("\"")){
			arrayOfStuff[b.getRow()][b.getCol()] = new TextCell(input);
		} else if(input.contains("(")){
			arrayOfStuff[b.getRow()][b.getCol()] = new FormulaCell(input, arrayOfStuff);
		} else{
			arrayOfStuff[b.getRow()][b.getCol()] = new ValueCell(input);
		}
	}
	
	//sets everything to emptycell to clear
	public void clearEntireCell(){
		for(int i = 0; i < rows; i++){
			for(int j = 0; j < cols; j++){
				arrayOfStuff[i][j] = new EmptyCell();
			}
		}
	}
	public void clearOneCell(String cell){
		//clears one cell by making it an emptycell
		SpreadsheetLocation userInput = new SpreadsheetLocation(cell);
		arrayOfStuff[userInput.getRow()][userInput.getCol()] = new EmptyCell();
	}
	//YO NO COMPRENDO how to file process
	private String readFromFile(String filename){ 
Scanner outputFile;
		
		try {
			outputFile = new Scanner(new File(filename));
		}
		
		catch (FileNotFoundException e) {
			return "File not found: " + filename;
		}
		//keep running while theres a next line
		while(outputFile.hasNextLine()){
			//split at commas
			String[] splitAtCommas = outputFile.nextLine().split(",");
			//if percentcell, put the percent back in and multiple by 100
			if(splitAtCommas[1].equals("PercentCell")){
				double holder = Double.parseDouble(splitAtCommas[2]);
				holder = holder * 100;
				splitAtCommas[2] = Double.toString(holder);
				splitAtCommas[2] += "%";
			}
			//call cell assignment to put into array
			cellAssignment(splitAtCommas[2], splitAtCommas[0]);
		}
		
		outputFile.close();
		return getGridText();

	}
	private String writeToFile(String filename){ 
		 String fileData = "";
	     PrintStream inputFile;
	     try {
	            inputFile = new PrintStream(new File(filename));
	     }
	     catch (FileNotFoundException e) {
	            return "File not found: " + filename;
	     }
	     //nested for loop going through array
	     for(int i = 0; i < 20; i++){
	    	 for(int j = 0; j < 12; j++){
	    		 //test to determine which cell
	    		 if (!(arrayOfStuff[i][j] instanceof EmptyCell)){
					fileData += (char)(j + 'A') + "" + (i+1) + ",";
					if(arrayOfStuff[i][j] instanceof TextCell){
						fileData += "TextCell" + ",";
					} else if(arrayOfStuff[i][j] instanceof ValueCell){
						fileData += "ValueCell" + ",";
					} else if(arrayOfStuff[i][j] instanceof PercentCell){
						fileData += "PercentCell" + ",";
					} else{
						fileData += "FormulaCell" + ",";
					}
					fileData += arrayOfStuff[i][j].fullCellText();
					inputFile.println(fileData);
				}
	    		fileData = "";
	    	 }
	     }
	inputFile.close();
	return ""; 
	}
}