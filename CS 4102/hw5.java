package CS_4102;

/*
 * Philip Liberato (pnl8zp)
 * CS 4102 Coding 5
 * 3/25/2014
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.Stack;

class Node {
	String type; // specifies the type of node
	String name; // referenced name
	int capacity; // defines the max back flow of the node
	int forwardFlow = 0; // counter to represent the current forward flow
	boolean visited = false; // incredibly self-explanatory
	ArrayList<Integer> adjacencyList = new ArrayList<Integer>();

	public Node(String type, String name, int capacity) {
		this.type = type;
		this.name = name;
		this.capacity = capacity;
	}
}

public class hw5 {
	public static Scanner scanner;
	public static int requests = 0; // r - total number so class requests by students
	public static int nCourses = 0; // c - total number of offered courses
	public static int nRequiredClasses = 0; // n - precise number of courses required for each student to take
	public static int nStudents = 0; // s - total number of students enrolling for classes
	public static ArrayList<Node> courses; // a list containing all of the courses offered for each case of the input file
	public static ArrayList<Node> students; // a list containing all of the students requesting classes for each case of the input file
	private static HashMap<String, ArrayList<String>> studentLoader; // HashMap required to store strings of requested student classes before courses are loaded
	public static ArrayList<Node> graph; // the list of all nodes contained in the graph... this is the data representation of the graph
	public static int maxFlow = 0;  // the global counter for maximal flow

	public static void main(String[] args) {
		// inappropriate command line parameter
		if (args.length != 1) {
			System.out.println("Please include the filename as a command line parameter.");
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
			scanner = new Scanner(input);
		} catch (FileNotFoundException e) {
			// if this catch block is entered, some serious shit is going down
			System.out.println("An unexpected error has occured.");
			System.exit(0);
		}

		// Initialize the various collections
		courses = new ArrayList<Node>();
		studentLoader = new HashMap<String, ArrayList<String>>();
		students = new ArrayList<Node>();
		graph = new ArrayList<Node>();

		// grab the first line of the file
		String currentLine = scanner.nextLine();
		while (!currentLine.equals("0 0 0")) {
			String[] splitLine = currentLine.split(" ");
			requests = Integer.parseInt(splitLine[0]);
			nCourses = Integer.parseInt(splitLine[1]);
			nRequiredClasses = Integer.parseInt(splitLine[2]);
			
			// handle every student class request
			for (int i = 0; i < requests; i++) {
				currentLine = scanner.nextLine();
				String[] studentClassRequest = currentLine.split(" "); // break the line into an array of parsable integers
				String name = studentClassRequest[0]; // name of the student for the request
				String course = studentClassRequest[1]; // class the named student wishes to take
				if (studentLoader.containsKey(name)) { // update the list if the key already exists
					ArrayList<String> currentList = studentLoader.get(name);
					currentList.add(course);
					studentLoader.put(name, currentList);
				} else { // the key doesn't exist, so make it happen
					ArrayList<String> genericList = new ArrayList<String>();
					genericList.add(course);
					studentLoader.put(name, genericList);
				}
			}
			for (int c = 0; c < nCourses; c++) { // yes i'm lame and made a terrible c++ joke
				currentLine = scanner.nextLine(); // ^ not even sorry about it
				String[] courseInfoLine = currentLine.split(" "); // parse the course line
				String courseName = courseInfoLine[0]; // grab the course name
				int maxSize = Integer.parseInt(courseInfoLine[1]); // grab the course capacity
				courses.add(new Node("class", courseName, maxSize)); // add the course node to the list of course nodes

			}
			buildAdjacencies();
			System.out.println((algorithm() == (nStudents * nRequiredClasses)) ? "Yes" : "No"); // the greatest line of java i've ever written
			reset(); // get ready for the next case in the file
			currentLine = scanner.nextLine(); // } advance the file scanner
			currentLine = scanner.nextLine(); // } advance the file scanner
		}
	}

	// ensure that the ArrayList of Node objects properly references the index of the course nodes in the adjacency lists of the student nodes
	public static void buildAdjacencies() {
		// Create the Start Node
		graph.add(new Node("#", "start", nRequiredClasses));
		// Add the properly ordered student nodes to the ArrayList
		for (String stName : studentLoader.keySet()) {
			Node student = new Node("student", stName, 1);
			graph.add(student);
			graph.get(0).adjacencyList.add(graph.indexOf(student));
			nStudents++;
		}
		// Add the class nodes to the ArrayList
		for (Node cl : courses) {
			graph.add(cl);
		}

		// Add the final node to the ArrayList to complete the graph
		Node finish = new Node("*", "finish", nStudents * nRequiredClasses);
		graph.add(finish);

		// Convert from studentLoader to the appropriate indecises for the adjacency lists
		for (String studentName : studentLoader.keySet()) {
			ArrayList<Integer> adjacencyLoader = new ArrayList<Integer>();
			for (Node course : courses) {
				for (String classString : studentLoader.get(studentName)) {
					if (classString.equals(course.name)) {
						adjacencyLoader.add(graph.indexOf(course));
					}
				}
			}
			// update the adjacency list for each student
			for (Node n : graph) {
				if (n.type.equals("student")) {
					if (n.name.equals(studentName)) {
						n.adjacencyList = adjacencyLoader;
					}
				}
			}
		}

		// set the final node index for each course object
		for (Node node : graph) {
			if (node.type.equals("class")) {
				ArrayList<Integer> adjacencyToFinal = new ArrayList<Integer>();
				adjacencyToFinal.add(graph.indexOf(finish));
				node.adjacencyList = adjacencyToFinal;
			}
		}
	}

	// the actual meet of the assignment
	public static int algorithm() {
		Stack<Node> stack = new Stack<Node>();
		stack.push(graph.get(0));
		int counter = 0;
		while (!stack.isEmpty()) {
			Node node = stack.peek();
			if (node.name.equals("finish")) {
				counter++;
				stack.pop();
				stack.pop();
			} else if (node.visited == false && node.forwardFlow < node.capacity) {
				List<Node> temp = new ArrayList<Node>();
				for (int index : node.adjacencyList) {
					Node child = graph.get(index);
					temp.add(child);
				}
				if (!stack.peek().type.equals("class")) {
					stack.peek().visited = true;
				}
				node.forwardFlow++;
				for (Node child : temp) {
					stack.push(child);
				}
			} else {
				stack.pop();
			}
		}

		return counter;

	}

	// Reset all variables for use on additional cases in the test file
	public static void reset() {
		students.clear();
		courses.clear();
		studentLoader.clear();
		graph.clear();
		maxFlow = 0;
		requests = 0;
		nRequiredClasses = 0;
		nCourses = 0;
		nStudents = 0;
	}
}
