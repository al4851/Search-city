import java.io.*;
import java.util.*;

/**
 * Find a path from a given city to another city using various search methods
 * Usage: java Search inputFile outputFile ("-" for standard input/output)
 *
 * @author Alfred Li
 */
public class Search {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java Search inputFile outputFile");
            System.exit(0);
        } else {
            HashMap<String, CityInfo> cityInfo = readCityInfo();
            String[] input = readInput(cityInfo, args[0]);
            result(cityInfo, args[1], input);
        }
    }

    /**
     * Read city info from "city.dat" file and "edge.dat" file
     * city.dat: file contain information of city
     * edge.dat: file contain connection between cities
     *
     * @return Hash map contain information of cities
     */
    public static HashMap<String, CityInfo> readCityInfo() {
        HashMap<String, CityInfo> cityInfo = new HashMap<>();
        // Read city.dat for information of city
        try {
            Scanner scan = new Scanner(new File("city.dat"));
            while (scan.hasNext()) {
                String name = scan.next();
                String state = scan.next();
                double lat = scan.nextDouble();
                double lon = scan.nextDouble();
                cityInfo.put(name, new CityInfo(name, state, lat, lon));
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: city.dat");
            System.exit(0);
        }
        // Read edge.dat for connection between cities
        try {
            Scanner scan = new Scanner(new File("edge.dat"));
            while (scan.hasNext()) {
                String city1 = scan.next();
                String city2 = scan.next();
                cityInfo.get(city1).edge.add(city2);
                cityInfo.get(city2).edge.add(city1);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: edge.dat");
            System.exit(0);
        }

        return cityInfo;
    }

    /**
     * Read start and destination city
     *
     * @param cityInfo information of cities
     * @param fileName file name of input file
     * @return string array contain the start and destination
     */
    public static String[] readInput(HashMap<String, CityInfo> cityInfo, String fileName) {
        Scanner scan;
        String[] input = null;
        String start, destination;
        // Standard input case
        if (fileName.equalsIgnoreCase("-")) {
            scan = new Scanner(System.in);
            System.out.println("Please enter the start city name:");
            start = scan.nextLine();
            validInput(cityInfo, start);
            System.out.println("Please enter the destination city name:");
            destination = scan.nextLine();
            validInput(cityInfo, destination);
            input = new String[]{start, destination};
        } else {
            // File input case
            try {
                scan = new Scanner(new File(fileName));
                start = scan.next();
                validInput(cityInfo, start);
                destination = scan.next();
                validInput(cityInfo, destination);
                input = new String[]{start, destination};
            } catch (FileNotFoundException e) {
                System.err.println("File not found: " + fileName);
                System.exit(0);
            }
        }
        return input;
    }

    /**
     * Validation the existence of city name
     *
     * @param cityInfo information of cities
     * @param city name of city to validate
     */
    public static void validInput(HashMap<String, CityInfo> cityInfo, String city) {
        try {
            if (!cityInfo.containsKey(city))
                throw new Exception(city);
        } catch (Exception e) {
            System.err.println("No such city: " + e.getMessage());
            System.exit(0);
        }
    }

    /**
     * Run all search method and output result
     *
     * @param cityInfo information of cities
     * @param fileName file name for output
     * @param input start and destination city name
     */
    public static void result(HashMap<String, CityInfo> cityInfo,
                              String fileName, String[] input) {
        LinkedList<String> path;
        // Standard output case
        if (fileName.equalsIgnoreCase("-")) {
            path = BFS(cityInfo, input);
            System.out.println("\nBreadth-First Search Results: ");
            stdOutput(cityInfo, path);
            path = DFS(cityInfo, input);
            System.out.println("\nDepth-First Search Results:");
            stdOutput(cityInfo, path);
            path = AS(cityInfo, input);
            System.out.println("\nA* Search Results: ");
            stdOutput(cityInfo, path);
        } else {
            // File output case
            try {
                FileWriter fileWriter = new FileWriter(fileName);
                PrintWriter printWriter = new PrintWriter(fileWriter);
                path = BFS(cityInfo, input);
                printWriter.println("\nBreadth-First Search Results: ");
                outputFile(cityInfo, path, printWriter);
                path = DFS(cityInfo, input);
                printWriter.println("\nDepth-First Search Results: ");
                outputFile(cityInfo, path, printWriter);
                path = AS(cityInfo, input);
                printWriter.println("\nA* Search Results: ");
                outputFile(cityInfo, path, printWriter);
                printWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Standard output format
     *
     * @param cityInfo information of cities
     * @param path result path
     */
    public static void stdOutput(HashMap<String, CityInfo> cityInfo,
                                 LinkedList<String> path) {
        double distance = 0;
        String previous = path.get(0);
        System.out.println(path.get(0));
        for (int i = 1; i < path.size(); i++) {
            System.out.println(path.get(i));
            distance += distance(cityInfo.get(path.get(i)), cityInfo.get(previous));
            previous = path.get(i);
        }
        System.out.printf("That took %d hops to find.\n", path.size() - 1);
        System.out.printf("Total distance = %d miles.\n", Math.round(distance));
    }

    /**
     * File output format
     *
     * @param cityInfo information of cities
     * @param path result path
     * @param printWriter File output writer
     */
    public static void outputFile(HashMap<String, CityInfo> cityInfo,
                                  LinkedList<String> path, PrintWriter printWriter) {
        double distance = 0;
        String previous = path.get(0);
        printWriter.println(path.get(0));
        for (int i = 1; i < path.size(); i++) {
            printWriter.println(path.get(i));
            distance += distance(cityInfo.get(path.get(i)), cityInfo.get(previous));
            previous = path.get(i);
        }
        printWriter.printf("That took %d hops to find.\n", path.size() - 1);
        printWriter.printf("Total distance = %d miles.\n\n", Math.round(distance));
    }

    /**
     * Breath-first search algorithm to find the path of two city
     *
     * @param cityInfo information of cities
     * @param input start and destination city
     * @return path from start to destination
     */
    public static LinkedList<String> BFS(HashMap<String, CityInfo> cityInfo, String[] input) {
        String start = input[0];
        String end = input[1];
        LinkedList<String> queue = new LinkedList<>();
        LinkedList<String> path = new LinkedList<>();
        HashMap<String, String> predecessors = new HashMap<>();
        queue.add(start);
        predecessors.put(start, null);

        while (!queue.isEmpty()) {
            String current = queue.remove(0);
            if (current.equalsIgnoreCase(end)) {
                break;
            }
            LinkedList<String> edge = new LinkedList<>(cityInfo.get(current).edge);
            Collections.sort(edge);
            for (String s : edge) {
                if (!predecessors.containsKey(s)) {
                    if (cityInfo.get(current).edge.contains(end)) {
                        predecessors.put(end, current);
                        queue.add(0, end);
                        break;
                    }
                    predecessors.put(s, current);
                    queue.add(s);
                }
            }
        }

        if (predecessors.containsKey(end)) {
            String current = end;
            path.add(0, current);
            while (!current.equalsIgnoreCase(start)) {
                current = predecessors.get(current);
                path.add(0, current);
            }
        }
        return path;
    }

    /**
     * Depth-first search algorithm to find the path of two city
     *
     * @param cityInfo information of cities
     * @param input start and destination city
     * @return path from start to destination
     */
    public static LinkedList<String> DFS(HashMap<String, CityInfo> cityInfo, String[] input) {
        String start = input[0];
        String end = input[1];
        Stack<String> stack = new Stack<>();
        LinkedList<String> path = new LinkedList<>();
        HashMap<String, String> predecessors = new HashMap<>();
        stack.push(start);
        predecessors.put(start, null);

        while (!stack.isEmpty()) {
            String current = stack.pop();
            if (current.equalsIgnoreCase(end)) {
                break;
            }
            LinkedList<String> edge = new LinkedList<>(cityInfo.get(current).edge);
            edge.sort(Collections.reverseOrder());
            for (String s : edge) {
                if (!predecessors.containsKey(s)) {
                    if (cityInfo.get(current).edge.contains(end)) {
                        predecessors.put(end, current);
                        stack.push(end);
                        break;
                    }
                    predecessors.put(s, current);
                    stack.push(s);
                }
            }
        }

        if (predecessors.containsKey(end)) {
            String current = end;
            path.add(0, current);
            while (!current.equalsIgnoreCase(start)) {
                current = predecessors.get(current);
                path.add(0, current);
            }
        }
        return path;
    }

    /**
     * A* search algorithm to find the path of two city
     *
     * @param cityInfo information of cities
     * @param input start and destination city
     * @return path from start to destination
     */
    public static LinkedList<String> AS(HashMap<String, CityInfo> cityInfo, String[] input) {
        String start = input[0];
        String end = input[1];
        LinkedList<ASNode> queue = new LinkedList<>();
        LinkedList<String> path = new LinkedList<>();
        double gn, hn;
        queue.add(new ASNode(start, null, 0, distance(cityInfo.get(start), cityInfo.get(end))));
        ASNode goal = null;

        while (!queue.isEmpty()) {
            ASNode current = queue.remove();
            if (current.name.equalsIgnoreCase(end)) {
                goal = current;
                break;
            }
            for (String s : cityInfo.get(current.name).edge) {
                gn = distance(cityInfo.get(s), cityInfo.get(current.name)) + current.gn;
                hn = distance(cityInfo.get(s), cityInfo.get(end));
                ASNode next = new ASNode(s, current, gn, hn);
                queue.add(next);
            }
            Collections.sort(queue);
        }

        if (goal != null) {
            path.add(0, goal.name);
        }
        ASNode current = goal;
        while (!current.name.equalsIgnoreCase(start)) {
            path.add(0, current.previous.name);
            current = current.previous;
        }
        return path;
    }

    /**
     * Calculate the distance between two city by latitude and longitude
     *
     * @param city1 object contain latitude and longitude info of city 1
     * @param city2 object contain latitude and longitude info of city 2
     * @return the distance between two city
     */
    public static double distance(CityInfo city1, CityInfo city2) {
        return Math.sqrt(Math.pow((city1.lat - city2.lat), 2)
                + Math.pow((city1.lon - city2.lon), 2)) * 100;
    }

    /**
     * Class represent a city's info
     */
    static class CityInfo {
        String name;
        String state;
        double lat;
        double lon;
        // List of cities has direct connection to this city
        LinkedList<String> edge = new LinkedList<>();

        /**
         * Constructor of CityInfo
         *
         * @param name name of city
         * @param state state of city
         * @param lat latitude of city
         * @param lon longitude of city
         */
        public CityInfo(String name, String state, double lat, double lon) {
            this.name = name;
            this.state = state;
            this.lat = lat;
            this.lon = lon;
        }
    }

    /**
     * Class represent a node in the A* search algorithm
     */
    static class ASNode implements Comparable<ASNode>{
        String name;
        ASNode previous;
        double gn;
        double hn;
        double fn;

        /**
         * Constructor of ASNode
         * @param name name of current city
         * @param previous the previous ASNode come from
         * @param gn distance take from start to current city
         * @param hn direct distance between current city and destination
         */
        public ASNode(String name, ASNode previous, double gn, double hn) {
            this.name = name;
            this.previous = previous;
            this.gn = gn;
            this.hn = hn;
            this.fn = gn + hn;
        }

        /**
         * Comparator to compare between two ASNode
         *
         * @param o the other ASNode
         * @return the order of two ASNode
         */
        @Override
        public int compareTo(ASNode o) {
            if (this.fn < o.fn)
                return -1;
            else if (o.fn < this.fn)
                return 1;
            return 0;
        }
    }
}
