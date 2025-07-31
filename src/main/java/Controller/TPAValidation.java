package Controller;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import FileOperations.WriteFile;
import Service.TPACheck;
import Service.TPALocationDateComparator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/tpa")
@RequiredArgsConstructor
@Slf4j
public class TPAValidation {

    private String addValue;
    private String removeValue;
    private String Message;
    private ArrayList<String> addList = new ArrayList<>();
    private ArrayList<String> removeList = new ArrayList<>(); 
    
    @Autowired
    private TPALocationDateComparator TPALocationCompare;

    @PostMapping("/TPAvalidation")
    public void testingFile(@RequestParam("testFile") MultipartFile file1,
                            @RequestParam("dbFile") MultipartFile file2) throws IOException
    {
        System.out.println("Validating added and removed TPAs ");
        XSSFWorkbook wb = new XSSFWorkbook(file1.getInputStream());
        XSSFSheet sheet = wb.getSheetAt(0);
        int add = 0, remove = 0;

        for (int Location = 1; Location <= sheet.getLastRowNum(); Location++) {
            XSSFRow row = sheet.getRow(Location);
            if (row == null) continue;

            XSSFCell TDLocationcell = row.getCell(6);
            XSSFCell TDDatecell = row.getCell(5);
            XSSFCell TDChangescell = row.getCell(2);
            XSSFCell TDTPAcell = row.getCell(0);

            DataFormatter format = new DataFormatter();
            String TDTPA = format.formatCellValue(TDTPAcell).replaceAll("\\s", "");
            String TDDatevalue = format.formatCellValue(TDDatecell);
            String TDChangesvalue = format.formatCellValue(TDChangescell);
            String TDLocationValue = format.formatCellValue(TDLocationcell);

            if (TDChangesvalue.equalsIgnoreCase("Please Add")) {
                add++;
                if (TPACheck.Excel(file1, TDLocationValue, TDTPA, TDDatevalue)) {
                    addValue = TDTPA + "|" + TDLocationValue + "|" + TDDatevalue;
                    addList.add(addValue);
                }
            }

            if (TDChangesvalue.equalsIgnoreCase("Please Remove")) {
                remove++;
                if (!TPACheck.Excel(file1, TDLocationValue, TDTPA, TDDatevalue)) {
                    removeValue = TDTPA + "|" + TDLocationValue + "|" + TDDatevalue;
                    removeList.add(removeValue);
                }
            }
        }

        WriteFile.WritetxtFile("TPAResult.txt", "-----------------------------------------------------------------------------------------------\n");
        Message = "Number of record found in Covered Entity sheet with \"Please Add\"  :  " + add + "\n";
        WriteFile.WritetxtFile("TPAResult.txt", Message, addList);
        WriteFile.WritetxtFile("TPAResult.txt", "-----------------------------------------------------------------------------------------------\n");
        Message = "Number of record found in Covered Entity sheet with \"Please Remove\"  :  " + remove + "\n";
        WriteFile.WritetxtFile("TPAResult.txt", Message, removeList);
        WriteFile.WritetxtFile("TPAResult.txt", "-----------------------------------------------------------------------------------------------\n");

        wb.close();

        TPALocationCompare.compareTPARecords(file1, file2);
    }
}