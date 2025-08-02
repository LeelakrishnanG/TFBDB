package Service;

import java.io.IOException;
import java.util.*;
import java.util.regex.*;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import FileOperations.WriteFile;

@Service
public class TPALocationDateComparator {

    private DataFormatter DataFormat = new DataFormatter();
    private String value1, value2, value3;
    private ArrayList<String> unmatchrecords = new ArrayList<>();

    public void compareTPARecords(MultipartFile coveredEntityFile, MultipartFile tpaDbFile, String fileName) throws IOException {
    	Map<String, Map<String, List<String>>> tpaMap = new HashMap<>();

    	// Build the map from Covered Entity File
    	try (XSSFWorkbook wb = new XSSFWorkbook(coveredEntityFile.getInputStream())) {
    	    XSSFSheet sheet = wb.getSheetAt(0);

    	    for (int rowIndex = 1; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
    	        XSSFRow row = sheet.getRow(rowIndex);
    	        if (row == null) continue;

    	        String tpa = validateSelfAdmin(DataFormat.formatCellValue(row.getCell(0)).replaceAll("\\s", ""));
    	        String location = LocationIDExtract(DataFormat.formatCellValue(row.getCell(6)));
    	        String date = DataFormat.formatCellValue(row.getCell(5));

    	        tpaMap.computeIfAbsent(tpa, k -> new HashMap<>())
    	              .computeIfAbsent(location, k -> new ArrayList<>())
    	              .add(date);
    	    }
    	}

    	// Compare each TPA's location/dates against DB
    	for (String tpa : tpaMap.keySet()) {
    	    Map<String, List<String>> locDateMap = tpaMap.get(tpa);
    	    for (String location : locDateMap.keySet()) {
    	        List<String> dates = locDateMap.get(location);
    	        boolean matchFound = false;

    	        for (String date : dates) {
    	            if (TPAComparison(tpa, location, date, tpaDbFile)) {
    	                matchFound = true;
    	                break;
    	            }
    	        }

    	        if (!matchFound) {
    	            for (String unmatchedDate : dates) {
    	                unmatchrecords.add(tpa + "|" + location + "|" + unmatchedDate);
    	            }
    	        }
    	    }
    	}

        WriteFile.WritetxtFile(fileName, "Mismatching Records between Covered Entity file and TPALOCATION table data:\n", unmatchrecords);
    }

    private boolean TPAComparison(String TPA, String LocationID, String Date, MultipartFile tpaDbFile) throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook(tpaDbFile.getInputStream())) {
            XSSFSheet sheet = wb.getSheetAt(0);
            for (int Location = 0; Location <= sheet.getLastRowNum(); Location++) {
                XSSFRow row = sheet.getRow(Location);
                if (row == null) continue;

                value1 = DataFormat.formatCellValue(row.getCell(0)).replaceAll("\\s", "");
                value2 = DataFormat.formatCellValue(row.getCell(1));
                value3 = DataFormat.formatCellValue(row.getCell(3));

                if (value1.equalsIgnoreCase(TPA) &&
                    value3.equalsIgnoreCase(Date) &&
                    value2.equalsIgnoreCase(LocationID)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static String validateSelfAdmin(String value1) {
        if (value1.equalsIgnoreCase("Self-Admin") || value1.equalsIgnoreCase("Self-Admin_CC")
                || value1.equalsIgnoreCase("Self-Admin_MGDMCD")) {
            value1 = new StringBuilder(value1).deleteCharAt(4).toString();
            if (value1.equalsIgnoreCase("SelfAdmin_MGDMCD")) {
                value1 = value1.replaceAll("_MGDMCD", "_MMCD");
            }
        }
        return value1;
    }

    private static String LocationIDExtract(String Location) {
        Matcher matcher = Pattern.compile("\\d+").matcher(Location);
        return matcher.find() ? matcher.group() : "LOCATION ID NOT FOUND";
    }
}
