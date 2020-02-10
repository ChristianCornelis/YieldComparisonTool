package project;


import project.Exceptions.YieldInvalidException;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Class for importing data from Stastistics Canada.
 */
public class StatsCanCSVImporter extends CSVImporter {
    private Map<Integer, ArrayList<Crop>> yields;
    /**
     * Constructor with filename, target units, and source units.
     * @param filename The file to read from.
     * @param su The source units.
     * @throws FileNotFoundException if the filename cannot be found.
     */
    public StatsCanCSVImporter(String filename, int su) throws FileNotFoundException {
        super(filename, su);
        yields = new HashMap<>();
    }

    /**
     * Converts the yield from a string to the appropriate type, depending on the source and target yield units.
     * @param yieldString The string to convert to a yield.
     * @return The yield, as a double in the correct units.
     */

    /**
     * Parses the CSV containing StatsCan data.
     */
    public void parse() {

        String[] headers = parseLine();
        String[] tokens;
        while ((tokens = parseLine()) != null) {
            int tokenCnt = 1;
            String cropName = tokens[0].replace(",", "");
            for (String yearString : headers) {
                //skip over crop type
                if (yearString.contains("crop")) {
                    continue;
                }
                double yield = 0;
                try {
                    yield = parseYield(tokens[tokenCnt]);
                } catch (YieldInvalidException e) {
                    System.out.println(e.getMessage());
                }
                Crop toPut = new Crop(cropName, yield, super.getSourceUnits());
                System.out.println("Adding crop " + cropName);
                int year = 0;
                try {
                    year = parseYear(yearString);
                } catch (NumberFormatException e) {
                    System.out.println(e.getMessage());
                    continue;
                }
                setYield(year, toPut);
                tokenCnt++;
            }
        }
    }

    /**
     * Sets a new yield for a specific crop in a given year.
     * @param year the year to add the yield to
     * @param toPut Crop object containing yield info.
     */
    public void setYield(int year, Crop toPut) {
        if (!yields.containsKey(year)) {
            yields.put(year, new ArrayList<>() {{ add(toPut); }});
        } else {
            yields.get(year).add(toPut);
        }
    }

    /**
     * Getter for yields.
     * @return the yields map.
     */
    public Map<Integer, ArrayList<Crop>> getYields() {
        return yields;
    }

    /**
     * ToString method. Calls on the superclass toString.
     * @return string representation of the class.
     */
    @Override
    public String toString() {
        return super.toString() +
                "\nStatsCanCSVImporter{" +
                "yields=" + yields +
                '}';
    }

    /**
     * Driver program.
     * @param args args for prog
     */
    public static void main(String[] args) {
        String fileName = args[0];
        int sourceUnits = Integer.parseInt(args[1]);
        int targetUnits = Integer.parseInt(args[2]);
        try {
            StatsCanCSVImporter sci = new StatsCanCSVImporter(fileName, sourceUnits);
            sci.parse();
        } catch (FileNotFoundException e) {
            System.err.println(e.getMessage());
        }
    }
}
