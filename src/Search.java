import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * Find a path from a given city to another city using various search methods
 * Usage: java Search inputFile outputFile ("-" for standard input output)
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
            System.out.printf("%s %s\n", input[0], input[1]);
        }
    }

    /**
     * Read city info from "city.dat" file and "edge.dat" file
     *
     * @return Hash map contain information of cities
     */
    public static HashMap<String, CityInfo> readCityInfo() {
        HashMap<String, CityInfo> cityInfo = new HashMap<>();
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
        try {
            Scanner scan = new Scanner(new File("edge.dat"));
            while (scan.hasNext()) {
                String city1 = scan.next();
                String city2 = scan.next();
                cityInfo.get(city1).getEdge().add(city2);
                cityInfo.get(city2).getEdge().add(city1);
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: edge.dat");
            System.exit(0);
        }
        return cityInfo;
    }

    /**
     * Read input from user or from file
     *
     * @param cityInfo information of cities
     * @param fileName file name of input file
     * @return string array contain the start and destination
     */
    public static String[] readInput(HashMap<String, CityInfo> cityInfo, String fileName) {
        Scanner scan;
        String[] input = null;
        String start, destination;
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

    public static void traverse() {
    }

    public static String[] BFS(HashMap<String, CityInfo> cityInfo, String[] input) {
        String start = input[0];
        String end = input[1];
        //LinkedList<String> queue = new LinkedList<>();
        //queue.add(start);

        return null;
    }

    public static String[] DFS(HashMap<String, CityInfo> cityInfo, String[] input) {
        String start = input[0];
        String end = input[1];

        return null;
    }

    public static String[] AS(HashMap<String, CityInfo> cityInfo, String[] input) {
        String start = input[0];
        String end = input[1];

        return null;
    }

    public static double distance(CityInfo city1, CityInfo city2) {
        return Math.sqrt(Math.pow((city1.lat - city2.lat), 2)
                + Math.pow((city1.lon - city2.lon), 2)) * 100;
    }

    static class CityInfo {
        String name;
        String state;
        double lat;
        double lon;
        LinkedList<String> edge = new LinkedList<>();

        public CityInfo(String name, String state, double lat, double lon) {
            this.name = name;
            this.state = state;
            this.lat = lat;
            this.lon = lon;
        }

        public String getName() {
            return name;
        }

        public String getState() {
            return state;
        }

        public double getLat() {
            return lat;
        }

        public double getLon() {
            return lon;
        }

        public LinkedList<String> getEdge() {
            return edge;
        }
    }
}
