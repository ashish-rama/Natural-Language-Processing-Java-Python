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
public class RegexDollarAmounts {

	public static ArrayList<String> lines = new ArrayList<String>();

	public static void main(String[] args) {
		String inputFile = args[0];
		populateLinesArray(inputFile);

		//checks for dollar signs and checks for any size number, billion, million, thousand, hundred, commas, and decimals

		Pattern p = Pattern.compile("\\$\\d+\\s?(billion|million|thousand|hundred)\\s(dollars)\\s(and)\\s\\d+\\s(cents)|"
				+ "\\$\\d+\\s?(billion|million|thousand|hundred)\\s?\\d+?\\s?(million)?\\s?\\d+?\\s?(thousand)?\\s?\\d+?\\s?(hundred)?\\s?\\d+?|"
				+ "\\$\\d+\\s?(dollars|dollar)|"
				+ "\\$\\d+\\s?(billion|million|thousand|hundred)\\s(dollars)|"
				+ "\\$\\d+\\s?(billion|million|thousand|hundred)|"
				+ "\\$(([1-9]\\d{0,2}(,\\d{3})*)|(([1-9]\\d*)?\\d))(\\.\\d\\d)?");


		ArrayList<String> dollarAmounts = new ArrayList<String>();
		Matcher m = null;
		StringBuffer result = null;

		for(int i = 0; i < lines.size(); i++) {
			m = p.matcher((String) lines.get(i));
			
			while(m.find()) {
				System.out.println("Found number: " + m.group());
				dollarAmounts.add(m.group());
				
				result = new StringBuffer();
				m.appendReplacement(result, "[ " + m.group() + " ]");
				m.appendTail(result);				
				
				lines.set(i, result.toString());
			}
		}
		System.out.println(dollarAmounts.size());

		createBracketOutputFile(lines);
		createOutputFile(dollarAmounts);

	}

	private static void createOutputFile(ArrayList<String> dollarAmounts) {
		try {
			File file = new File("dollar_output.txt");
			if(file.exists()) {
				file.delete();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter("dollar_output.txt", true));
			for(String num : dollarAmounts) {
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
			File file = new File("bracket_output_dollar.txt");
			if(file.exists()) {
				file.delete();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter("bracket_output_dollar.txt", true));
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
