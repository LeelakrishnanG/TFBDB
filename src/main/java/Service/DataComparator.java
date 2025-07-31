package Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import FileOperations.WriteFile;

@Service
public class DataComparator {

    private static XSSFWorkbook workbook1, workbook2;
    public static int Filesheetrow = 0, unmatchrecordcount = 0, matchrecordcount = 0;
    public static String FileValue, DBValue, Message;
    static DataFormatter Dataformat;
    static List<String> FileValueFields = new ArrayList<>();

    static ArrayList<String> unmatch = new ArrayList<>();

    public static void DataComparison(MultipartFile file1, MultipartFile file2, String Filename, String File) throws IOException {
        try {
            workbook1 = new XSSFWorkbook(file1.getInputStream());
            workbook2 = new XSSFWorkbook(file2.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        XSSFSheet fileSheet = workbook1.getSheetAt(0);
        XSSFSheet dbSheet = workbook2.getSheetAt(0);
        Dataformat = new DataFormatter();

        // Step 1: Collect all DB row keys
        Set<String> dbRowKeys = new HashSet<>();
        for (int r = 0; r <= dbSheet.getLastRowNum(); r++) {
            XSSFRow row = dbSheet.getRow(r);
            if (row != null) {
                List<String> values = new ArrayList<>();
                for (int c = 0; c < row.getLastCellNum(); c++) {
                    values.add(Dataformat.formatCellValue(row.getCell(c)));
                }
                dbRowKeys.add(String.join("-", values));
            }
        }

        // Step 2: Compare each row in File against DB
        for (Filesheetrow = 0; Filesheetrow <= fileSheet.getLastRowNum(); Filesheetrow++) {
            XSSFRow row = fileSheet.getRow(Filesheetrow);
            if (row != null) {
                List<String> values = new ArrayList<>();
                for (int c = 0; c < row.getLastCellNum(); c++) {
                    values.add(Dataformat.formatCellValue(row.getCell(c)));
                }
                String rowKey = String.join("-", values);
                if (!dbRowKeys.contains(rowKey)) {
                    unmatch.add(String.valueOf(Filesheetrow + 1));
                    unmatchrecordcount++;
                } else {
                    matchrecordcount++;
                }
            }
        }

        workbook1.close();
        workbook2.close();

        // Step 3: Write output
        WriteFile.WritetxtFile(Filename, "-----------------------------------------------------------------------------------------------\n");
        WriteFile.WritetxtFile(Filename, "Number of records found in " + File + " file  :  " + (Filesheetrow + 1) + "\n");
        Message = "Number of unmatching records found between in " + File + " file and DB  :  " + unmatchrecordcount + "\n";

        if (unmatchrecordcount == 0) {
            WriteFile.WritetxtFile(Filename, Message);
        } else {
            WriteFile.WritetxtFile(Filename, Message, unmatch);
        }
    }

}
