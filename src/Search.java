/**
 * Find a path from a given city to another city using various search methods
 *
 * @author Alfred Li
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;

public class Search {
    public static void main(String[] args) {
        HashMap<String, CityInfo> cityInfo = readCity();
        readEdge();
        if (args.length < 2) {
            System.err.println("Usage: java Search inputFile outputFile");
            System.exit(0);
        } else if (args[0].equalsIgnoreCase("-")) {
            System.out.println(args[0] + args[1]);
        } else if (args[1].equalsIgnoreCase("-") ) {
            System.out.println(args[0] + args[1]);
        } else if (args[0].equalsIgnoreCase("-") && args[1].equalsIgnoreCase("-")) {
            System.out.println(args[0] + args[1]);
        }
    }

    public static HashMap<String, CityInfo> readCity() {
        HashMap<String, CityInfo> cityInfoMap = new HashMap<>();
        try {
            Scanner scan = new Scanner(new File("city.dat"));
            while (scan.hasNext()) {
                String name = scan.next();
                String state = scan.next();
                double lat = scan.nextDouble();
                double lon = scan.nextDouble();
                //System.out.printf("%s %s %f %f\n", name, state, lat, lon);
                cityInfoMap.put(name, new CityInfo(name, state, lat, lon));
            }
        } catch (FileNotFoundException e) {
            System.err.println("File not found: city.dat");
            System.exit(0);
        }
        return cityInfoMap;
    }

    public static void readEdge() {
        try {
            Scanner scan = new Scanner(new File("edge.dat"));

        } catch (FileNotFoundException e) {
            System.err.println("File not found: edge.dat");
            System.exit(0);
        }
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
    }
}
