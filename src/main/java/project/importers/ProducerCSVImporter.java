package project.importers;

import project.Exceptions;
import project.InputHandler;
import project.PromptHelper;
import project.data.Farm;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProducerCSVImporter extends CSVImporter {

    private Map<Integer, ArrayList<Farm>> yields;
    /**
     * Constructor.
     * @param filename the file to parse.
     * @param sourceUnits the source units for yield.
     * @throws FileNotFoundException if the filename cannot be found.
     */
    public ProducerCSVImporter(String filename, int sourceUnits) throws FileNotFoundException {
        super(filename, sourceUnits);
        yields = new HashMap<>();
    }

    /**
     * Retrieves the producer name. Used for storing records in the database.
     * @return the producer name
     */
    public String getProducerName() {
        System.out.println(PromptHelper.getProducerPrompt());
        InputHandler ih = new InputHandler();
        return ih.getBasicInput();
    }
    /**
     * Parses the CSV containing producer yield data.
     */
    public void parse() {
        String producer = getProducerName();
        String[] tokens = parseLine();  //skip headers
        while ((tokens = parseLine()) != null) {
            String farmName = tokens[0];
            String location = tokens[1];
            String cropName = tokens[2];
            int year = 0;
            try {
                year = parseYear(tokens[3]);
            } catch (NumberFormatException e) {
                System.out.println(e.getMessage());
                continue;
            }
            String yieldString = tokens[4];
            double yield = 0;
            try {
                yield = parseYield(yieldString);
            } catch (Exceptions.YieldInvalidException e) {
                System.out.println(e.getMessage());
            }
            Farm toPut = new Farm(farmName, location, cropName, yield, super.getSourceUnits());
            getDb().addNewProducerYield(year, toPut, producer);
            setYield(year, toPut);
        }
    }

    /**
     * Sets a new yield for a specific crop in a given year.
     * @param year the year to add the yield to
     * @param toPut Crop object containing yield info.
     */
    public void setYield(int year, Farm toPut) {
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
    public Map<Integer, ArrayList<Farm>> getYields() {
        return yields;
    }

    /**
     * ToString method. Calls on the superclass toString.
     * @return string representation of the class.
     */
    @Override
    public String toString() {
        return super.toString() +
                "\nProducerCSVImporter{" +
                "yields=" + yields +
                '}';
    }
}
