package Controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import Service.DataComparator;

@RestController
@RequestMapping("/api/ndc")
public class NDCValidation {
	


    @PostMapping(
        value = "/NDCvalidation")
    public ResponseEntity<Resource> validateNDC(
        @RequestParam("testFile") MultipartFile NDCFile,
        @RequestParam("dbFile") MultipartFile NDCDB
    ) {
        try {
        	
        	String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        	String fileName = "NDC_SAResults"+dateTime+".txt";
        	
            System.out.println("Comparing NDC test sheet and DB records via uploaded files : "+fileName);
            DataComparator.DataComparison(NDCFile, NDCDB, fileName, "NDC");

            Path filePath = Paths.get(".\\Output\\"+fileName);
            Resource resource = new UrlResource(filePath.toUri());
          
            return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity
                .status(500)
                .header(HttpHeaders.CONTENT_TYPE, "text/plain")
                .body(null); 
        }
    }

}