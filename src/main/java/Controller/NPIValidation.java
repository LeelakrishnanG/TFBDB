package Controller;

import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import FileOperations.Excel;
import FileOperations.WriteFile;
import Service.DataComparator;
import Service.YXNValidation;

@RestController
@RequestMapping("/api/npi")
public class NPIValidation {

    String Message;
    int count;
    static ArrayList<String> UniqueID = new ArrayList<>();

    @PostMapping("/NPIvalidation")
    public ResponseEntity<String> compareExcel(@RequestParam("testFile") MultipartFile NPIDB,
                                               @RequestParam("dbFile") MultipartFile NPITEST, 
                                               @RequestParam("coveredEntityFile") MultipartFile COVEREDENTITY) {

        try {
            System.out.println("Starting comparison logic at: " + LocalTime.now());
            //DataComparator.DataComparison(NPIDB, NPITEST, "NPIResult.txt", "NPI");

            System.out.println("Validating OPAID between NPI and Covered Entity sheet: " + LocalTime.now());
            for (String Position : Excel.UniqueOPAID(NPITEST)) {
                if (Excel.CoveredEntity(COVEREDENTITY).contains(Position)) {
                    UniqueID.add(Position);
                    count++;
                }
            }

            Message = "Number of unique OPAID found in NPI sheet and Covered Entity: " + count + "\n";
            WriteFile.WritetxtFile("NPIResult.txt", Message);
            WriteFile.WritetxtFile("NPIResult.txt", Message, UniqueID);

            System.out.println("Validating X,Y,N values of TPAs: " + LocalTime.now());
            YXNValidation.SendOPAIDValidate();

            System.out.println("Comparing NPI test sheet and DB values: " + LocalTime.now());
            DataComparator.DataComparison(NPIDB, NPITEST, "NPIResults.txt", "NPI");

            return ResponseEntity.ok("Validation and comparison completed successfully.");

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error during processing: " + e.getMessage());
        }
    }
}
