package CS_4102;
// Philip Liberato (pnl8zp)
// CS 4102 1pm, 1/27/2014

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class hw2 {

	private static File myFile;
	private static Scanner fileScanner;
	private static int currentLine = 0;
	private static int cases;
	private static int nCompanies;
	private static int nBoxesTotal;
	private static int nBoxesTaken;
	private static int iteration = 1;
	private static HashMap<String, Integer> shipOne;
	private static HashMap<String, Integer> shipHalf;
	private static HashMap<String, Integer> companyCost;

	public static void main(String[] args) {

		if (!(args.length > 0)) {
			System.out
					.println("Please include the filename as a command line parameter.");
		} else {
			myFile = new File(args[0]);
			if (!myFile.exists()) {
				System.out.println("This file could not be found.");
				System.exit(1);
			} else {
				handleFile();
			}
		}

	}

	public static void handleFile() {
		try {
			fileScanner = new Scanner(myFile);
		} catch (FileNotFoundException e) {
			System.out.println("The file could not be properly scanned.");
			System.exit(1);
		}

		shipOne = new HashMap<String, Integer>();
		shipHalf = new HashMap<String, Integer>();

		while (fileScanner.hasNextLine()) {

			if (currentLine == 0) {
				System.out.println("");
				cases = fileScanner.nextInt();
				currentLine++;
				fileScanner.nextLine();
			} else if (currentLine == 1) {
				String sHolder = fileScanner.nextLine();
				String[] parser = sHolder.split("\\s+");
				nBoxesTotal = Integer.parseInt(parser[0]);
				nBoxesTaken = Integer.parseInt(parser[1]);
				nCompanies = Integer.parseInt(parser[2]);
				currentLine++;
			} else if (currentLine != (nCompanies + 2)) {
				String temp = fileScanner.nextLine();
				String[] parser = temp.split("\\s+");
				String company = parser[0];
				shipOne.put(company, Integer.parseInt(parser[1]));
				shipHalf.put(company, Integer.parseInt(parser[2]));
				currentLine++;
			} else if (iteration <= cases) {
				printResults(iteration);
				iteration++;
				currentLine = 1;
				shipOne.clear();
				shipHalf.clear();
			} else {
				// stub
			}
		}
		printResults(iteration);
	}
	
	public static void calculateCost() {
				
		companyCost = new HashMap<String, Integer>();
		
		for(String company : shipOne.keySet()) {
			
			int cost = 0;
			int boxTotal = nBoxesTotal;
			int boxTaken = nBoxesTaken;
			
			while(boxTotal > boxTaken) {
				
				if(boxTotal / 2 < boxTaken) {
					cost += shipOne.get(company) * (boxTotal - boxTaken);
					boxTotal = boxTaken;											// I NEED TO ACCOUNT FOR THE +/- 1 HERE
				} else if(shipHalf.get(company) >= (shipOne.get(company) * (boxTotal / 2))) {
					cost += shipOne.get(company) * (boxTotal - boxTaken);
					boxTotal = boxTaken;
				} else {
					cost += shipHalf.get(company);
					boxTotal = boxTotal / 2; // i NEED TO ACCOUNT FOR THE +/- 1 HERE
				}
				
			}
			
			companyCost.put(company, cost);
			
		}
				
	}
	
	public static Map<String, Integer> sortMap() {
		
		List<Map.Entry<String, Integer>> entryList = new LinkedList<Map.Entry<String, Integer>>(companyCost.entrySet());

        Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {

            public int compare(Map.Entry<String, Integer> entry1, Map.Entry<String, Integer> entry2) {
            	if(entry1.getValue() == entry2.getValue()) {
            		return ((entry1.getKey()).compareTo(entry2.getKey()));
            	} else {
            		return (entry1.getValue()).compareTo(entry2.getValue());
            	}
            }
        });
        												
        Map<String, Integer> result = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> companyCostEntry : entryList) {
            result.put(companyCostEntry.getKey(), companyCostEntry.getValue());
        }
        
        return result;
		
	}

	public static void printResults(int caseNumber) {

		System.out.println("Case " + caseNumber);
		calculateCost();
		//sortMap();
		
		for (String s : sortMap().keySet()) {
			System.out.println(s + " " + companyCost.get(s));
		}
	}

}
