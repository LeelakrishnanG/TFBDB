package FileOperations;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class WriteFile {
	
	private static File FileF;
	private static FileWriter file;
	
	public static void WritetxtFile(String filename, String Message, ArrayList<String> List) {
		try {
			
			FileF = new File(".\\Output\\"+ filename);
			if (FileF.exists()) {
				try (BufferedWriter writer = new BufferedWriter( new FileWriter(
					".\\Output\\"
							+ filename,true))) {
					writer.append(Message);
					for (String Values : List) {
						writer.append(Values+"\n");
					}
				}

			} else {
				
				file = new FileWriter(
						".\\Output\\"
								+ filename);
				file.write("");
				file.close();
			}

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
	public static void WritetxtFile(String filename, String Message) {
		try {
			
			FileF = new File(
					".\\Output\\"
							+ filename);
			if (FileF.exists()) {
				try (BufferedWriter writer = new BufferedWriter( new FileWriter(
					".\\Output\\"
							+ filename,true))) {
					writer.append(Message);
				}

			} else {
				
				file = new FileWriter(
						".\\Output\\"
								+ filename);
				file.write("");
				file.close();
			}

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


}