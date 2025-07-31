package FileOperations;

import java.io.FileInputStream;
import java.io.IOException;
//import java.io.FileOutputStream;
//import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class Excel {

	private static XSSFWorkbook workbook;
	private static String TPAName;
	private static String SendValue;
	static String Message;
	
	public static ArrayList<String> UniqueOPAID(MultipartFile file) throws IOException {
	    ArrayList<String> value = new ArrayList<>();
	    DataFormatter Dataformat = new DataFormatter();

	    try (XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream())) {
	        XSSFSheet sheet = workbook.getSheetAt(0);
	        for (int sheetrow = 0; sheetrow < sheet.getLastRowNum(); sheetrow++) {
	            XSSFRow row = sheet.getRow(sheetrow);
	            if (row == null) continue;

	            XSSFCell cell = row.getCell(4);
	            if (cell == null) continue;

	            String data = Dataformat.formatCellValue(cell);
	            if (!value.contains(data)) {
	                value.add(data);
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw e;
	    }

	    return value;
	}


	public static ArrayList<String> CoveredEntity(MultipartFile CoveredEntity) throws IOException {
		ArrayList<String> value = new ArrayList<String>();

		try {

			FileInputStream input = new FileInputStream(
					PropReadWrite.ReadProp("COVERED_ENTITY"));

			workbook = new XSSFWorkbook(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		XSSFSheet sheet = workbook.getSheetAt(0);
		for (int sheetrow = 0; sheetrow >= 0; sheetrow++) {
			if (sheet.getRow(sheetrow) != null) {

				XSSFRow row = sheet.getRow(sheetrow);
				XSSFCell cell = row.getCell(3);
				DataFormatter Dataformat = new DataFormatter();
				if (value.contains(Dataformat.formatCellValue(cell).toString())) {
					continue;
				} else {
					value.add(Dataformat.formatCellValue(cell).toString());
				}
			} else {
				break;
			}
		}
		workbook.close();
		return value;
	}

	public static ArrayList<String> SendOPAID(String TPAMain, String Send) throws IOException {
		ArrayList<String> value = new ArrayList<String>();

		try {

			FileInputStream input = new FileInputStream(
					PropReadWrite.ReadProp("NPI_TESTFILE"));

			workbook = new XSSFWorkbook(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		XSSFSheet sheet = workbook.getSheetAt(0);
		for (int sheetrow = 0; sheetrow >= 0; sheetrow++) {
			if (sheet.getRow(sheetrow) != null) {

				XSSFRow row = sheet.getRow(sheetrow);
				XSSFCell TPAcell = row.getCell(1);
				DataFormatter Dataformat = new DataFormatter();
				TPAName = Dataformat.formatCellValue(TPAcell).toString();
				if (TPAName.equalsIgnoreCase(TPAMain)) {
					XSSFCell Sendcell = row.getCell(5);
					SendValue = Dataformat.formatCellValue(Sendcell).toString();
					if (SendValue.equalsIgnoreCase(Send)) {
						XSSFCell OPAIDcell = row.getCell(4);
						value.add(Dataformat.formatCellValue(OPAIDcell).toString());
					}
				}
				if (sheetrow == sheet.getLastRowNum()) {
					break;
				}

			}

		}
		workbook.close();
		return value;

	}

}
