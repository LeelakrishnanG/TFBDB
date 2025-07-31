package FileOperations;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;


public class PropReadWrite {

	public static String PropValue;
	public static FileWriter file;
	public static File FileF;

	public static String ReadProp(String PropKey) throws IOException {

		Properties inputfile = new Properties();
		FileInputStream file = null;
		try {
			file = new FileInputStream(
					".\\src\\test\\resources\\config.properties");
			inputfile.load(file);
			PropValue = inputfile.getProperty(PropKey);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			file.close();
		}
		
		return PropValue;
	}

}
