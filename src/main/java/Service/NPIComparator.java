package Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import FileOperations.PropReadWrite;
import FileOperations.WriteFile;

public class NPIComparator {

	static XSSFWorkbook workbook1;
	static XSSFWorkbook workbook2;
	public static int Filesheetrow = 0;
	public static int DBsheetrow = 0;
	public static int unmatchrecordcount = 0;
	public static int matchrecordcount = 0;
	public static String FileValue;
	public static String DBValue;
	public static String Message;
	static DataFormatter Dataformat;
	static ArrayList<String> unmatch = new ArrayList<String>();
	static List<String> FileValueFields = new ArrayList<String>();

	
	public static void NPIDataComparison() throws IOException {

		try {

			FileInputStream NPIFile = new FileInputStream(PropReadWrite.ReadProp("NPI_TESTFILE"));
			FileInputStream NPIDB = new FileInputStream(PropReadWrite.ReadProp("NPI_DBFILE"));

			workbook1 = new XSSFWorkbook(NPIFile);
			workbook2 = new XSSFWorkbook(NPIDB);
		} catch (Exception e) {
			e.printStackTrace();
		}
		XSSFSheet Filesheet = workbook1.getSheetAt(0);
		XSSFSheet DBsheet = workbook2.getSheetAt(0);
		XSSFRow Filerow = Filesheet.getRow(Filesheetrow);
		XSSFRow DBrow = DBsheet.getRow(DBsheetrow);

		Dataformat = new DataFormatter();

		for (; Filesheetrow >= 0; Filesheetrow++) {
			if (Filesheet.getRow(Filesheetrow) != null) {

				for (int FilesheetCol = 0; FilesheetCol < Filerow.getLastCellNum(); FilesheetCol++) {
					XSSFCell FileCell = Filerow.getCell(FilesheetCol);
					FileValue = Dataformat.formatCellValue(FileCell).toString();
					FileValueFields.add(FileValue);
				}

				for (int DBsheetrow = 0; DBsheetrow < DBsheet.getLastRowNum(); DBsheetrow++) {
					for (int DBsheetcol = 0; DBsheetcol < DBrow.getLastCellNum(); DBsheetcol++) {
						XSSFCell DBCell = DBrow.getCell(DBsheetcol);
						DBValue = Dataformat.formatCellValue(DBCell).toString();

						if (FileValueFields.get(DBsheetcol).equalsIgnoreCase(DBValue)) {
							continue;
						} else {
							unmatch.add(Integer.toString(Filesheetrow) + 1);
							unmatchrecordcount++;
							continue;
						}
					}
				}
				FileValueFields.clear();
			}
			matchrecordcount++;
			if (Filesheetrow == Filesheet.getLastRowNum()) {
				break;
			}
		}
		workbook1.close();
		workbook2.close();
		WriteFile.WritetxtFile("NPIResult.txt",
				"-----------------------------------------------------------------------------------------------\n");
		if (unmatchrecordcount == 0) {
			Message = "Number of record found in NPI file  :  " + Integer.toString(matchrecordcount) + "\n";
			WriteFile.WritetxtFile("NPIResult.txt", Message);
			Message = "Number of unmatching record found between in NPI file and DB  :  "
					+ Integer.toString(unmatchrecordcount) + "\n";
			WriteFile.WritetxtFile("NPIResult.txt", Message);
		} else {
			Message = "Number of record found in NPI file  :  " + Integer.toString(matchrecordcount) + "\n";
			WriteFile.WritetxtFile("NPIResult.txt", Message);
			Message = "Number of unmatching record found between in NPI file and DB  :  "
					+ Integer.toString(unmatchrecordcount) + "\n";
			WriteFile.WritetxtFile("NPIResult.txt", Message, unmatch);
		}
	}

}
