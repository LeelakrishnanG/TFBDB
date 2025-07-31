package Service;

import java.io.IOException;
import java.util.ArrayList;

import FileOperations.Excel;
import FileOperations.WriteFile;

public class YXNValidation {

	static ArrayList<String> MYList = new ArrayList<String>();
	static ArrayList<String> MNList = new ArrayList<String>();
	static ArrayList<String> MXList = new ArrayList<String>();

	static ArrayList<String> CCYList = new ArrayList<String>();
	static ArrayList<String> CCNList = new ArrayList<String>();
	static ArrayList<String> CCXList = new ArrayList<String>();

	static ArrayList<String> MMCDYList = new ArrayList<String>();
	static ArrayList<String> MMCDNList = new ArrayList<String>();
	static ArrayList<String> MMCDXList = new ArrayList<String>();

	static ArrayList<String> BDYList = new ArrayList<String>();
	static ArrayList<String> BDNList = new ArrayList<String>();
	static ArrayList<String> BDXList = new ArrayList<String>();

	static String Message;

	private static String TPAList[] = { "Macrohelix", "Macrohelix_CC", "Macrohelix_BD", "MacroHelix_MMCD" };
	private static String SendOPAID[] = { "Y", "N", "X" };

	public static void SendOPAIDValidate() throws IOException {

		for (String TPAName : TPAList) {

			// Macrohelix
			if (TPAName.equalsIgnoreCase("Macrohelix")) {
				for (String SendValue : SendOPAID) {
					if (SendValue.equalsIgnoreCase("Y")) {
						MYList.addAll(Excel.SendOPAID(TPAName, SendValue));
					} else if (SendValue.equalsIgnoreCase("N")) {
						MNList.addAll(Excel.SendOPAID(TPAName, SendValue));
					} else {
						MXList.addAll(Excel.SendOPAID(TPAName, SendValue));
					}

				}
			}
			// Macrohelix_cc
			if (TPAName.equalsIgnoreCase("Macrohelix_CC")) {
				for (String SendValue : SendOPAID) {
					if (SendValue.equalsIgnoreCase("Y")) {
						CCYList.addAll(Excel.SendOPAID(TPAName, SendValue));
					} else if (SendValue.equalsIgnoreCase("N")) {
						CCNList.addAll(Excel.SendOPAID(TPAName, SendValue));
					} else {
						CCXList.addAll(Excel.SendOPAID(TPAName, SendValue));
					}

				}
			}
			// Macrohelix_MMCD
			if (TPAName.equalsIgnoreCase("Macrohelix_MMCD")) {
				for (String SendValue : SendOPAID) {
					if (SendValue.equalsIgnoreCase("Y")) {
						MMCDYList.addAll(Excel.SendOPAID(TPAName, SendValue));
					} else if (SendValue.equalsIgnoreCase("N")) {
						MMCDNList.addAll(Excel.SendOPAID(TPAName, SendValue));
					} else {
						MMCDXList.addAll(Excel.SendOPAID(TPAName, SendValue));
					}

				}
			}
			// Macrohelix_BD
			if (TPAName.equalsIgnoreCase("Macrohelix_BD")) {
				for (String SendValue : SendOPAID) {
					if (SendValue.equalsIgnoreCase("Y")) {
						BDYList.addAll(Excel.SendOPAID(TPAName, SendValue));
					} else if (SendValue.equalsIgnoreCase("N")) {
						BDNList.addAll(Excel.SendOPAID(TPAName, SendValue));
					} else {
						BDXList.addAll(Excel.SendOPAID(TPAName, SendValue));
					}
				}
			}

		}

		MYList = UniqueValuesValidator(MYList);
		CCYList = UniqueValuesValidator(CCYList);
		MMCDYList = UniqueValuesValidator(MMCDYList);
		BDYList = UniqueValuesValidator(BDYList);

		MNList = UniqueValuesValidator(MNList);
		CCNList = UniqueValuesValidator(CCNList);
		MMCDNList = UniqueValuesValidator(MMCDNList);
		BDNList = UniqueValuesValidator(BDNList);

		MXList = UniqueValuesValidator(MXList);
		CCXList = UniqueValuesValidator(CCXList);
		MMCDXList = UniqueValuesValidator(MMCDXList);
		BDXList = UniqueValuesValidator(BDXList);

		ArrayList<String> tempXList = new ArrayList<String>();
		ArrayList<String> tempNList = new ArrayList<String>();

		// Compare X values with Y for Macrohelix
		for (String XMValue : MXList) {
			if (MYList.contains(XMValue)) {
				if (!tempXList.contains(XMValue)) {
					tempXList.add(XMValue);
				}
			}
		}

		// Compare N values with Y for Macrohelix
		for (String MNValue : MNList) {
			if (MYList.contains(MNValue)) {
				if (!tempNList.contains(MNValue)) {
					tempNList.add(MNValue);
				}
			}
		}

		Message = "\nMacroHelix" + "\n";
		WriteFile.WritetxtFile("NPIResult.txt", Message);
		Message = "Number of unique OPAID found with \"Y\"  :  " + Integer.toString(MYList.size()) + "\n";
		WriteFile.WritetxtFile("NPIResult.txt", Message, MYList);

		Message = "Number of unique OPAID found with \"X\"  :  " + Integer.toString(MXList.size()) + "\n";
		WriteFile.WritetxtFile("NPIResult.txt", Message, MXList);

		Message = "Number of unique OPAID found with \"N\"  :  " + Integer.toString(MNList.size()) + "\n";
		WriteFile.WritetxtFile("NPIResult.txt", Message, MNList);

		tempXList.clear();
		tempNList.clear();

		for (String XCCValue : CCXList) {
			if (CCYList.contains(XCCValue)) {
				if (!tempXList.contains(XCCValue)) {
					tempXList.add(XCCValue);
				}
			}
		}

		// Compare N values with Y for Macrohelix_cc
		for (String CCNValue : CCNList) {
			if (CCYList.contains(CCNValue)) {
				if (!tempNList.contains(CCNValue)) {
					tempNList.add(CCNValue);
				}
			}
		}

		Message = "\nMacroHelix_CC" + "\n";
		WriteFile.WritetxtFile("NPIResult.txt", Message);
		Message = "Number of unique OPAID found with \"Y\"  :  " + Integer.toString(CCYList.size()) + "\n";
		WriteFile.WritetxtFile("NPIResult.txt", Message, CCYList);

		Message = "Number of unique OPAID found with \"X\"  :  " + Integer.toString(CCXList.size()) + "\n";
		WriteFile.WritetxtFile("NPIResult.txt", Message, CCXList);

		Message = "Number of unique OPAID found with \"N\"  :  " + Integer.toString(CCNList.size()) + "\n";
		WriteFile.WritetxtFile("NPIResult.txt", Message, CCNList);

		tempXList.clear();
		tempNList.clear();

		for (String MMCDXValue : MMCDXList) {
			if (MMCDYList.contains(MMCDXValue)) {
				if (!tempXList.contains(MMCDXValue)) {
					tempXList.add(MMCDXValue);
				}
			}
		}

		for (String MMCDNValue :MMCDNList) {
			if (MMCDYList.contains(MMCDNValue)) {
				if (!tempNList.contains(MMCDNValue)) {
					tempNList.add(MMCDNValue);
				}
			}
		}

		Message = "\nMacroHelix_BD" + "\n";
		WriteFile.WritetxtFile("NPIResult.txt", Message);
		Message = "Number of unique OPAID found with \"Y\"  :  " + Integer.toString(BDYList.size()) + "\n";
		WriteFile.WritetxtFile("NPIResult.txt", Message, BDYList);
		
		Message = "Number of unique OPAID found with \"X\"  :  " + Integer.toString(BDXList.size()) + "\n";
		WriteFile.WritetxtFile("NPIResult.txt", Message, BDXList);

		Message = "Number of unique OPAID found with \"N\"  :  " + Integer.toString(BDNList.size()) + "\n";
		WriteFile.WritetxtFile("NPIResult.txt", Message, BDNList);

		tempXList.clear();
		tempNList.clear();

		for (String BDXValue : BDXList) {
			if (BDYList.contains(BDXValue)) {
				if (!tempXList.contains(BDXValue)) {
					tempXList.add(BDXValue);
				}
			}
		}

		for (String BDNValue : BDNList) {
			if (BDYList.contains(BDNValue)) {
				if (!tempNList.contains(BDNValue)) {
					tempNList.add(BDNValue);
				}
			}
		}

		Message = "\nMacroHelix_MMCD" + "\n";
		WriteFile.WritetxtFile("NPIResult.txt", Message);
		Message = "Number of unique OPAID found with \"Y\"  :  " + Integer.toString(MMCDYList.size()) + "\n";
		WriteFile.WritetxtFile("NPIResult.txt", Message, MMCDYList);

		Message = "Number of unique OPAID found with \"X\"  :  " + Integer.toString(MMCDXList.size()) + "\n";
		WriteFile.WritetxtFile("NPIResult.txt", Message, MMCDXList);

		Message = "Number of unique OPAID found with \"N\"  :  " + Integer.toString(MMCDNList.size()) + "\n";
		WriteFile.WritetxtFile("NPIResult.txt", Message, MMCDNList);

		tempXList.clear();
		tempNList.clear();
	}
	// Method to get unique values from X,N

	public static ArrayList<String> UniqueValuesValidator(ArrayList<String> Values) {
		ArrayList<String> temp = new ArrayList<String>();
		for (String temp1 : Values) {
			if (!temp.contains(temp1)) {
				temp.add(temp1);
			}
		}
		return temp;
	}
}
