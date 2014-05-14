package CS_4102;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * Philip Liberato (pnl8zp) CS 4102 Programming 6 4/29/2014
 */

public class hw6 {
	
	public static Scanner fScanner;
	public static char[][] theBoard;
	public static Position startPosition;
	public static Position endPosition;
	public static ArrayList<Position> visitedPositions;
	public static double w_g = 0;
	public static double w_h = 0;
	
	public static void main(String[] args) {

		// inappropriate command line parameter
		if (args.length != 1) {
			System.out
					.println("Please include the filename as a command line parameter.");
			System.exit(0);
		}

		// create the file object
		File input = new File(args[0]);
		if (!input.exists()) {
			// called when you referenced a file not in the directory
			System.err.println("This file could not be found.");
			System.exit(0);
		}

		try {
			fScanner = new Scanner(input);
		} catch (FileNotFoundException e) {
			// if this catch block is entered, some serious shit is going down
			System.out.println("An unexpected error has occured.");
			System.exit(0);
		}

		String currentLine = "";
		int caseCounter = 0;
		int rowCounter = 0;
		int row = 0;
		int column = 0;
		boolean readingCaseInfo = true;
		int numCases = Integer.parseInt(fScanner.nextLine());		
		
		while(caseCounter < numCases) {
					
			if (readingCaseInfo) {
				column = fScanner.nextInt();
				row = fScanner.nextInt();
				w_g = fScanner.nextDouble();
				w_h = fScanner.nextDouble();
				readingCaseInfo = false;
				rowCounter = 0;
				theBoard = new char[row][column];
				fScanner.nextLine();
				// System.out.println("Row: " + row + " Column: " + column + " W_g: " + w_g + " W_h: " + w_h);
			} else {

				currentLine = fScanner.nextLine();

				for(int i = 0; i < currentLine.length(); i++) {
					theBoard[rowCounter][i] = currentLine.charAt(i);
					if(currentLine.charAt(i) == 'S') {
						startPosition = new Position(rowCounter, i, 'S', 0);
					}
					if(currentLine.charAt(i) == 'F') {
						endPosition = new Position(rowCounter, i, 'F', 0);
					}
				}
								
				if(rowCounter == row - 1) {
					/*for(char[] line : theBoard) {
						System.out.println(Arrays.toString(line));
					}*/
					
					algorithm(theBoard, w_g, w_h, row, column);
					
					rowCounter = 0;
					readingCaseInfo = true;
					caseCounter++;
					
				}
				
				rowCounter++;
				
			}
				
		}

	}
	
	public static void algorithm(char[][] theBoard, double wg, double wh, int r, int c) {
		
		PriorityQueue<Position> theQueue = new PriorityQueue<Position>(r*c, 
	            new Comparator<Position>(){
			
			// Compare by lowest f-score
            public int compare(Position a, Position b){
                if (a.fCost > b.fCost) return 1;
                if (a.fCost == b.fCost) return 0;
                return -1;
            }
        });
		
		visitedPositions = new ArrayList<Position>();
				
		theQueue.add(startPosition);
		
		Position previousPosition = new Position(r, c, '.', -1);
		
		while(!theQueue.isEmpty()) {
			Position p = theQueue.poll();
			visitedPositions.add(p);

			if(p.row == endPosition.row && p.column == endPosition.column) {
				// we're done so finish the algorithm
				// and return g
				System.out.println(previousPosition.gSteps + 1);
				break;
			}
						
			List<Position> newNeighbors = getNeighbors(p, r, c);
			for(Position nPosition : newNeighbors) {
				theQueue.add(nPosition);
			}
			
			previousPosition = p;
						
		}
			
	}
	
	public static List<Position> getNeighbors(Position p, int boardRows, int boardColumns) {
		
		List<Position> neighbors = new ArrayList<Position>();
				
		// top
		if(p.row - 1 >= 0) {
			int r = p.row - 1;
			int c = p.column;
			if(theBoard[r][c] != 'X' && !visitedPositions.contains(new Position(r, c, theBoard[r][c], p.gSteps + 1))) {
				Position newPosition = new Position(r, c, theBoard[r][c], p.gSteps + 1);
				neighbors.add(newPosition);
				visitedPositions.add(newPosition);
			}
		}
		// top-left
		if(p.row - 1 >= 0 && p.column - 1 >= 0) {
			int r = p.row - 1;
			int c = p.column - 1;
			if(theBoard[r][c] != 'X' && !visitedPositions.contains(new Position(r, c, theBoard[r][c], p.gSteps + 1))) {
				Position newPosition = new Position(r, c, theBoard[r][c], p.gSteps + 1);
				neighbors.add(newPosition);
				visitedPositions.add(newPosition);
			}
		}
		// top-right
		if(p.row - 1 >= 0 && p.column + 1 < boardColumns) {
			int r = p.row - 1;
			int c = p.column + 1;
			if(theBoard[r][c] != 'X' && !visitedPositions.contains(new Position(r, c, theBoard[r][c], p.gSteps + 1))) {
				Position newPosition = new Position(r, c, theBoard[r][c], p.gSteps + 1);
				neighbors.add(newPosition);
				visitedPositions.add(newPosition);
			}
		}
		// left
		if(p.column - 1 >= 0) {
			int r = p.row;
			int c = p.column - 1;
			if(theBoard[r][c] != 'X' && !visitedPositions.contains(new Position(r, c, theBoard[r][c], p.gSteps + 1))) {
				Position newPosition = new Position(r, c, theBoard[r][c], p.gSteps + 1);
				neighbors.add(newPosition);
				visitedPositions.add(newPosition);
			}
		}
		// right
		if(p.column + 1 < boardColumns) {
			int r = p.row;
			int c = p.column + 1;
			if(theBoard[r][c] != 'X' && !visitedPositions.contains(new Position(r, c, theBoard[r][c], p.gSteps + 1))) {
				Position newPosition = new Position(r, c, theBoard[r][c], p.gSteps + 1);
				neighbors.add(newPosition);
				visitedPositions.add(newPosition);
			}
		}
		// bottom
		if(p.row + 1 < boardRows) {
			int r = p.row + 1;
			int c = p.column;
			if(theBoard[r][c] != 'X' && !visitedPositions.contains(new Position(r, c, theBoard[r][c], p.gSteps + 1))) {
				Position newPosition = new Position(r, c, theBoard[r][c], p.gSteps + 1);
				neighbors.add(newPosition);
				visitedPositions.add(newPosition);
			}
		}
		// bottom-left
		if(p.row + 1 < boardRows && p.column - 1 >= 0) {
			int r = p.row + 1;
			int c = p.column - 1;
			if(theBoard[r][c] != 'X' && !visitedPositions.contains(new Position(r, c, theBoard[r][c], p.gSteps + 1))) {
				Position newPosition = new Position(r, c, theBoard[r][c], p.gSteps + 1);
				neighbors.add(newPosition);
				visitedPositions.add(newPosition);
			}
		}
		// bottom-right
		if(p.row + 1 < boardRows && p.column + 1 < boardColumns) {
			int r = p.row + 1;
			int c = p.column + 1;
			if(theBoard[r][c] != 'X' && !visitedPositions.contains(new Position(r, c, theBoard[r][c], p.gSteps + 1))) {
				Position newPosition = new Position(r, c, theBoard[r][c], p.gSteps + 1);
				neighbors.add(newPosition);
				visitedPositions.add(newPosition);
			}
		}
		
		return neighbors;
	}
	
	public static double distanceFormula(int c, int r) {
		double a = Math.pow(Math.abs(endPosition.column - c), 2);
		double b = Math.pow(Math.abs(endPosition.row - r), 2);
		return Math.sqrt(a + b);
		
		//return Math.sqrt((endPosition.column - x1)^2 + (endPosition.row - y1)^2);
	}

	public static class Position {
		int row;
		int column;
		double fCost;
		double gCost = 0;
		double hCost = 0;
		int gSteps = 0;
		char value;
		
		public Position(int r, int c, char v, int g) {
			this.row = r;
			this.column = c;
			this.value = v;
			
			if(endPosition == null || v == 'F' || v == 'S') {
				fCost = 0;
				gCost = 0;
				hCost = 0;
			} else {
				gSteps = g;
				gCost = w_g * (gSteps);
				hCost = w_h * distanceFormula(c, r);
				fCost = gCost + hCost;
			}
			
			// System.out.println(v + ": [" + row + ", " + column + "]");
		}
		
		@Override
		public boolean equals(Object other){
		    if (other == null) return false;
		    if (other == this) return true;
		    if (!(other instanceof Position)) return false;
		    
		    Position otherPosition = (Position) other;

		    if(this.row == otherPosition.row && this.column == otherPosition.column) {
		    	return true;
		    } else {
		    	return false;
		    }
		}

	}
	
}















