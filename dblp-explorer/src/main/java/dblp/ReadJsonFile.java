package dblp;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static dblp.PaperPredicate.*;

public class ReadJsonFile {

	public static void main (String[] args) throws FileNotFoundException, ParseException {

		List<JsonObject> allPapers = new ArrayList<JsonObject>(); //stored all papers read from file
		Scanner scanSysIn = new Scanner (System.in);
		System.out.println("Please enter source file's location & name including extension:");
		String fileName = scanSysIn.nextLine().trim();
		System.out.println("Please enter the searching keyword: ");
		String keyword = scanSysIn.next().trim();
		System.out.println("Please enter the number of searching tiers, n: ");
		int n = scanSysIn.nextInt();

		long startTime = System.currentTimeMillis();

		Scanner scanner = new Scanner (new File(fileName));


		while (scanner.hasNext())  {

				JsonObject jsonObj = (JsonObject) new JsonParser().parse(scanner.nextLine());
				allPapers.add(jsonObj);
		}
		int SIZE = allPapers.size();


		if (n>=0) {
				List<JsonObject> tier0 = new ArrayList<JsonObject>();
				try{
					tier0 = filterPapers(allPapers, containKeyword(keyword));
					System.out.println("Paper containing keyword(s): " +tier0.size()+" paper(s):" );
					for (int i=0;i<tier0.size();i++)
						System.out.println(tier0.get(i));
				} catch(Exception noFoundException) {
					System.out.println("No paper found including or referenced by that keyword");
				}

				List<JsonObject> upperTier = tier0;
				int j = 1;
			while (upperTier!= null && j <=n) {
					List<JsonObject> lowerTier = findReferences (upperTier,allPapers);
					if (lowerTier.size() == 0) {
						System.out.println("Papers of tiers " +j+ " to " +n+ " not available.");
						System.out.println("SEARCH COMPLETED!");
						break;
					}
					else {
					System.out.println("Paper tier "+j+" : "+ lowerTier.size()+ " paper(s): ");
					for (int i=0;i<lowerTier.size();i++)
						System.out.println(lowerTier.get(i));
					//update upperTier and j
					upperTier = lowerTier;
					j++; }

				}

		}
			else {
			Exception nException = new Exception("n has to be an integer greater than or equal to 1!");
		}
			Runtime runtime = Runtime.getRuntime();
			// Run the garbage collector
			runtime.gc();
			// Calculate the used memory
			long memory = runtime.totalMemory() - runtime.freeMemory();
			//System.out.println("Used memory is bytes: " + memory);
			System.out.println("Used memory in megabytes: "
				+ (memory/(1000000)));

			long endTime = System.currentTimeMillis();
			long elapsedTime = endTime - startTime;
			System.out.println("Running time in seconds: " + elapsedTime/1000);


	}//end main


	public static List<JsonObject> findReferences(List<JsonObject> upperTierPapers, List<JsonObject> allPapers) {
		//TODO check if can put allPapers into constructor so we don't have to pass it as an argumnent
		List<JsonObject> referencedPapers = null;
		JsonArray referencesID = null;
		for (int i=0;i<upperTierPapers.size();i++) {
			if (upperTierPapers.get(i).get("references") != null) { //TODO check if it can be more eff
				referencesID = upperTierPapers.get(i).get("references").getAsJsonArray();

				for (int j=0;j<referencesID.size();j++) { //get references of each upper tier paper
					String refID = referencesID.get(j).getAsString();
					List<JsonObject> temp = filterPapers(allPapers,findPaper(refID));
					if (temp != null && referencedPapers == null)
						referencedPapers = temp;
					else if (temp!= null && referencedPapers != null)
						referencedPapers = Stream.concat(temp.stream(),referencedPapers.stream())
									.collect(Collectors.toList());
			}
			}
		}
		return referencedPapers;
	}//end find references
}