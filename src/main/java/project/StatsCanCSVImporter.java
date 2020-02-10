package project;


import project.Exceptions.YieldInvalidException;

import java.io.FileNotFoundException;

/**
 * Class for importing data from Stastistics Canada.
 */
public class StatsCanCSVImporter extends CSVImporter {

//    /**
//     * Constructor.
//     * @param filename the filename to parse
//     */
//    public StatsCanCSVImporter(String filename) {
//        try {
//            csvReader = new CSVReader(new FileReader(filename));
//        } catch (FileNotFoundException e) {
//            System.err.println("Error: File " + filename + " does not exist");
//        }
//        yields = new HashMap<Integer, ArrayList<Crop>>();
//        targetUnits = Crop.KG_PER_HA;
//        sourceUnits = Crop.KG_PER_HA;
//    }

    /**
     * Constructor with filename, target units, and source units.
     * @param filename The file to read from.
     * @param su The source units.
     * @throws FileNotFoundException if the filename cannot be found.
     */
    public StatsCanCSVImporter(String filename, int su) throws FileNotFoundException {
        super(filename, su);
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
//                    yield = convertYield(yield, cropName);
                } catch (YieldInvalidException e) {
                    System.err.println(e.getMessage());
                }
                Crop toPut = new Crop(cropName, yield, super.getSourceUnits());
                System.out.println("Adding crop " + cropName);
                int year = parseYear(yearString);
                super.setYield(year, toPut);
                tokenCnt++;
            }
        }
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
        System.out.println("ah");
    }
}
