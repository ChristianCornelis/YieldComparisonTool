package project;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import com.opencsv.CSVReader;
import project.Exceptions.YieldInvalidException;

/**
 * Abstract class containing methods that an importer should contain for both
 * StatsCan and producer data importing.
 */
public abstract class CSVImporter implements Importer {
    private CSVReader csvReader;
    private Map<Integer, ArrayList<Object>> yields;
    private int sourceUnits;
    private Converter converter;

    /**
     * Construct for CSV importer. Used by all subclasses.
     * @param filename the file to parse
     * @param su the source units yields are present in.
     * @throws FileNotFoundException if filename is not found.
     */
    public CSVImporter(String filename,  int su) throws FileNotFoundException {
//        try {
        csvReader = new CSVReader(new FileReader(filename));
//        }
//        catch (FileNotFoundException e) {
//            System.err.println("Error: File " + filename + " does not exist.");
//            System.exit(1);
//        }
        yields = new HashMap<Integer, ArrayList<Object>>();
        sourceUnits = su;
        converter = new Converter();
    }

    /**
     * Tries to convert yields from a string to a double. Handles for errors.
     * @param parsedYield The parsed yield to convert.
     * @return The converted yield if successful.
     * @throws YieldInvalidException if yield is not in correct format.
     */
    public double parseYield(String parsedYield) throws YieldInvalidException {
        try {
            parsedYield = parsedYield.replace(",", "");
            return Double.parseDouble(parsedYield);
        } catch (NumberFormatException e) {
            throw new YieldInvalidException("Yield " + parsedYield + " not valid.");
        }
    }


    /**
     * Converts a year string to an integer.
     * @param token the string to be converted to an int.
     * @return The year, as an integer
     * @throws NumberFormatException if the year cannot be parsed properly.
     */
    public int parseYear(String token) throws NumberFormatException {
        return Integer.parseInt(token);
    }

    /**
     * Removes the Crop Type header from a CSV header.
     * @param headers the headers from the first line of the CSV read
     * @return the headers, minus the first header - Crop type
     */
    public String[] removeCropTypeHeader(String[] headers) {
        return Arrays.copyOfRange(headers, 1, headers.length - 1);
    }

    /**
     * Parses a line using the csvReader instance variable. Catches any exceptions that may occur.
     * @return array of string tokens from the CSV line.
     */
    public String[] parseLine() {
        String[] tokens = null;
        try {
            tokens = csvReader.readNext();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tokens;
    }

//    /**
//     * Getter for target units.
//     * @return the target units instance variable.
//     */
//    public int getTargetUnits() {
//        return targetUnits;
//    }

    /**
     * Getter for source units.
     * @return the source units instance variable
     */
    public int getSourceUnits() {
        return sourceUnits;
    }

    /**
     * To string method.
     * @return string containing all attributes of CSV importer.
     */
    @Override
    public String toString() {
        return "CSVImporter{" +
                "csvReader=" + csvReader +
                ", yields=" + yields +
                ", sourceUnits=" + sourceUnits +
                '}';
    }
}
