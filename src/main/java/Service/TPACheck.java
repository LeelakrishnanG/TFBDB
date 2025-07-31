package Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

public class TPACheck {

    public static DataFormatter format = new DataFormatter();

    public static boolean Excel(MultipartFile multipartFile, String Location, String TPA, String Date) throws IOException {
        try (InputStream file = multipartFile.getInputStream();
             XSSFWorkbook newwb = new XSSFWorkbook(file)) {

            XSSFSheet newsheet = newwb.getSheetAt(0);

            for (int Locationrow = 0; Locationrow < newsheet.getLastRowNum() - 1; Locationrow++) {
                XSSFRow row = newsheet.getRow(Locationrow);
                XSSFCell DBTPAcell = row.getCell(0);
                XSSFCell DBDatecell = row.getCell(3);
                XSSFCell DBLocationcell = row.getCell(1);

                String DBTPAcellValue = format.formatCellValue(DBTPAcell).replaceAll("\\s", "");
                String DBDateValue = format.formatCellValue(DBDatecell).toString();
                String DBLocationValue = format.formatCellValue(DBLocationcell).toString();
                String LocationValue;
                if (DBTPAcellValue.equalsIgnoreCase(TPA)) {

                    if (DBDateValue.equalsIgnoreCase(Date)) {

                        LocationValue = LocationIDExtract(Location);
                        if (DBLocationValue.equalsIgnoreCase(LocationValue)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    private static String LocationIDExtract(String Location) {
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher = pattern.matcher(Location);

        while (matcher.find()) {
            return matcher.group().toString();
        }
        return "LOCATION ID NOT FOUND";
    }
}
