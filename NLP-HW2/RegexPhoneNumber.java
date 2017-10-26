import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.regex.*;
/**
 * 
 * @author Ashish Ramachandran (ar3986); NLP HW2
 *
 */
public class RegexPhoneNumber {

	public static ArrayList<String> lines = new ArrayList<String>();

	public static void main(String[] args) {
		String inputFile = args[0];
		populateLinesArray(inputFile);

		//checks if it has space between each non-digit character, if it uses 
		//dashes or parentheses, and correct number of digits for a phone number

		Pattern p = Pattern.compile("\\d{3}\\s?-\\d{3}\\s?-\\s?\\d{4}|"		//dashes
				+ "\\(\\s?\\d{3}\\s?\\)\\s?-?\\d{3}\\s?-?\\s?\\d{4}|"		//parentheses
				+ "\\d{3}\\s\\d{3}\\s\\d{4}");								//spaces


		ArrayList<String> phoneNums = new ArrayList<String>();
		Matcher m = null;
		StringBuffer result = null;

		for(int i = 0; i < lines.size(); i++) {
			m = p.matcher((String) lines.get(i));
			result = new StringBuffer();
			while(m.find()) {
				System.out.println("Found number: " + m.group());
				phoneNums.add(m.group());
				m.appendReplacement(result, "[ " + m.group() + " ]");
				m.appendTail(result);
				
				lines.set(i, result.toString());
			}
		}
		System.out.println(phoneNums.size());

		createBracketOutputFile(lines);
		createOutputFile(phoneNums);

	}

	private static void createOutputFile(ArrayList<String> phoneNums) {
		try {
			File file = new File("phone_output.txt");
			if(file.exists()) {
				file.delete();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter("phone_output.txt", true));
			for(String num : phoneNums) {
				writer.write(num);
				writer.newLine();
				writer.flush();
			}
			writer.close();
		} catch (Exception e) {

		}
	}

	private static void createBracketOutputFile(ArrayList<String> lines) {
		try {
			File file = new File("bracket_output_phone.txt");
			if(file.exists()) {
				file.delete();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter("bracket_output_phone.txt", true));
			for(String line : lines) {
				writer.write(line);
				writer.newLine();
				writer.flush();
			}
			writer.close();
		} catch (Exception e) {

		}
	}

	private static void populateLinesArray(String input) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(input));
			while(true) {
				String line = in.readLine();
				if(line == null)
					break;
				if(line.isEmpty())
					continue;
				lines.add(line);
			}
			in.close();
		} catch (Exception e) {

		}
	}

}
