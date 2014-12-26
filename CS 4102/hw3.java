package CS_4102;

import java.awt.geom.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;


public class hw3 {

	//syso ctrl + space = System.out.println();
	
	public static File myFile;
	public static Scanner fileScanner;
	public static ArrayList<Point2D.Double> universe;
	public static ArrayList<Point2D.Double> sortedX;
	public static ArrayList<Point2D.Double> sortedY;
	public static double minimumDistance;
	
	public static void main(String[] args) {

		// make them do it right
		if(args.length != 1) {
			System.err.println("Please properly input the command line parameters!");
			System.exit(0);
		}
		
		// initialize the file
		myFile = new File(args[0]);
		
		if(!myFile.exists()) {
			System.err.println("The given filename does not exist!");
			System.exit(0);
		} else {
			universe = new ArrayList<Point2D.Double>();
			enableScanner();
			handleFile();
		}

	}
	
	// attach the scanner to the existing file
	public static void enableScanner() {
		try {
			fileScanner = new Scanner(myFile);
		} catch (FileNotFoundException e) {
			System.err.println("The file could not be properly read!");
			e.printStackTrace();
			System.exit(0);
		}
	}
	
	// do things to the file with the scanner
	public static void handleFile() {
		
		String currentLine = fileScanner.nextLine();
		while(!currentLine.equals("0")) {
			
			int numPoints = Integer.parseInt(currentLine);
			
			for (int i = 0; i < numPoints; i++) {
				String futurePoint = fileScanner.nextLine();
				String[] pointCoordinates = futurePoint.split(" ");
				double x = Double.parseDouble(pointCoordinates[0]);
				double y = Double.parseDouble(pointCoordinates[1]);
				Point2D.Double thePoint = new Point2D.Double(x, y);
				universe.add(thePoint);
			}
			
			processPoints();
			currentLine = fileScanner.nextLine();
			
		}
		
	}
	
	public static void processPoints() {
		
		/*for (Point2D.Double p : universe) {
			System.out.println("X: " + p.getX() + ", Y: " + p.getY());
		}*/
		
		sortedX = new ArrayList<Point2D.Double>(universe);
		sortedY = new ArrayList<Point2D.Double>(universe);
		
		Collections.sort(sortedX, new CompareByX());
		Collections.sort(sortedY, new CompareByY());
		
		minimumDistance = 10001;
		double value = testingX(sortedX);
		
		if(value < minimumDistance) {
			minimumDistance = value;
			System.out.println(value);
		} else System.out.println("infinity");
				
		universe.clear();
		
	}
	
	public static double testingX(List<Point2D.Double> list) {
		
		if(list.size() == 1 || list.size() == 0) {
			return 10001;
		}
		if(list.size() == 2) {
			
			return list.get(0).distance(list.get(1));
			
		}
		if(list.size() == 3) {
			
			double distance1 = list.get(0).distance(list.get(1));
			double distance2 = list.get(0).distance(list.get(2));
			double distance3 = list.get(1).distance(list.get(2));
			
			if(distance1 <= distance2 && distance1 <= distance3)
				return distance1;
			if(distance2 <= distance1 && distance1 <= distance3)
				return distance2;
			if(distance3 <= distance1 && distance1 <= distance2)
				return distance3;
						
		}

		int size = list.size();
		
		ArrayList<Point2D.Double> temp = new ArrayList<Point2D.Double>(list);
		
		double left = testingX(list.subList(0, size/2));
		double right = testingX(temp.subList(size/2, size));
		
		if(left < right) {
			return left;
		} else return right;
				
	}

	public static class CompareByX implements Comparator<Point2D.Double> {
		
		public int compare(Point2D.Double point1, Point2D.Double point2) {

	          return (point1.getX() < point2.getX()) ? -1 : (point1.getX() > point2.getX()) ? 1 : 0;

	    }
		
	}
	
	public static class CompareByY implements Comparator<Point2D.Double> {
		
		public int compare(Point2D.Double point1, Point2D.Double point2) {

	          return (point1.getY() < point2.getY()) ? -1 : (point1.getY() > point2.getY()) ? 1 : 0;

	    }
		
	}
	
}
