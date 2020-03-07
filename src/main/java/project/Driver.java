package project;

import project.comparators.YieldComparator;
import project.data.Crop;
import project.data.Farm;
import project.importers.ProducerCSVImporter;
import project.importers.StatsCanCSVImporter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;

import static project.PromptHelper.*;

/**
 * Temporary driver class for the entire project.
 */
public class Driver {
    private InputHandler inputHandler;
    private Map<Integer, ArrayList<Farm>> producerYields;
    private Map<Integer, ArrayList<Crop>> statsCanYields;
    private int producerYieldsUnits;
    private int statsCanYieldsUnits;

    /**
     * Constructor.
     */
    public Driver() {
        inputHandler = new InputHandler();
    }

    /**
     * Imports a file based on an action specified.
     * @param action the action specified based on static constants declared in this class.
     */
    public void importFile(int action) {
        int[] validImportActions = {IMPORT_KG_PER_HA, IMPORT_LB_PER_AC, IMPORT_BU_PER_AC, CANCEL_TASK};
        int importUnits = inputHandler.chooseAction(
                inputHandler.unitsPrompt("Choose the units the yields are in:\n"),
                validImportActions);
        if (importUnits != CANCEL_TASK) {
            System.out.println(inputHandler.fileLocationPrompt());
            String fileLocation = inputHandler.getBasicInput();

            int sourceUnits = -1;
            switch (importUnits) {
                case IMPORT_KG_PER_HA:
                    sourceUnits = Crop.KG_PER_HA;
                    break;
                case IMPORT_LB_PER_AC:
                    sourceUnits = Crop.LBS_PER_AC;
                    break;
                case IMPORT_BU_PER_AC:
                    sourceUnits = Crop.BU_PER_AC;
                    break;
            }
            if (action == IMPORT_PRODUCER_CSV) {
                importProducerCSV(sourceUnits, fileLocation);
            } else {
                importStatsCanCSV(sourceUnits, fileLocation);
            }
        }
    }

    /**
     * Method to compare yields.
     */
    public void compareYields() {
        if (producerYields == null || statsCanYields == null) {
            System.out.println("Cannot perform comparison.");
            return;
        }
        //Todo: Implement comparison in Bu/ac
        int[] validComparisonActions = {IMPORT_KG_PER_HA, IMPORT_LB_PER_AC, /*IMPORT_BU_PER_AC,*/ CANCEL_TASK};
        int comparisonUnits = inputHandler.chooseAction(
                inputHandler.unitsPrompt("Choose the units you wish to see yield differences in:\n"),
                validComparisonActions
        );
        if (comparisonUnits == CANCEL_TASK) {
            return;
        }
        YieldComparator yc = new YieldComparator(
                producerYieldsUnits, statsCanYieldsUnits, comparisonUnits, producerYields,  statsCanYields
        );
        ArrayList<Integer> yearsIntersection = yc.keyIntersection(
                producerYields.keySet(),
                statsCanYields.keySet()
        );
        int year = inputHandler.chooseAction(
                inputHandler.yearsPrompt(), yearsIntersection, inputHandler.invalidYearPrompt(yearsIntersection));
        System.out.println(inputHandler.cropPrompt());
        String crop = inputHandler.getBasicInput();
        double diff = 0;
        try {
            diff = yc.compareCropsByYear(crop, year);
        } catch (Exceptions.InvalidComparatorParamsException e) {
            System.out.println(e.getMessage());
            return;
        } catch (Exceptions.BushelsConversionKeyNotFoundException e) {
            System.out.println(e.getMessage());
            return;
        }
        String unitStr = yc.formatUnitsString(comparisonUnits);
        System.out.println(yc.formatComparison(diff, crop, year, unitStr));
        return;
    }

    /**
     * Driver to run the importers and comparisons on data imported.
     */
    public void run() {
        int action = -1;
        while (action != QUIT) {
            System.out.println(inputHandler.getYieldsStatus(producerYields, statsCanYields));
            int[] validActions = {IMPORT_PRODUCER_CSV, IMPORT_STATSCAN_CSV, COMPARE_PRODUCER_STATSCAN_YIELDS, QUIT};
            action = inputHandler.chooseAction(inputHandler.actionPrompt(), validActions);
            if (action == IMPORT_PRODUCER_CSV || action == IMPORT_STATSCAN_CSV) {
                importFile(action);
            } else if (action == COMPARE_PRODUCER_STATSCAN_YIELDS) {
                compareYields();
            }
        }

        return;
    }

    private void importProducerCSV(int sourceUnits, String fileLocation) {
        setProducerYieldsUnits(sourceUnits);
        try {
            ProducerCSVImporter pci = new ProducerCSVImporter(fileLocation, sourceUnits);
            pci.parse();
            setProducerYields(pci.getYields());
            System.out.println(inputHandler.mapStatus("Producer yields", true));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println(inputHandler.mapStatus("Producer yields", false));
        }
    }

    private void importStatsCanCSV(int sourceUnits, String fileLocation) {
        setStatsCanYieldsUnits(sourceUnits);
        try {
            StatsCanCSVImporter sci = new StatsCanCSVImporter(fileLocation, sourceUnits);
            sci.parse();
            setStatsCanYields(sci.getYields());
            System.out.println(inputHandler.mapStatus("StatsCan yields", true));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println(inputHandler.mapStatus("StatsCan yields", false));
        }
    }

    /**
     * Retrieve the StatsCan yields already loaded.
     * @return the StatsCan yields.
     */
    public Map<Integer, ArrayList<Crop>> getStatsCanYields() {
        return statsCanYields;
    }

    //No getter for producer units - meant to stay encapsulated.

    /**
     * Get the input handler.
     * @return the input handler.
     */
    public InputHandler getInputHandler() {
        return inputHandler;
    }

    /**
     * Get the untis that the producer yields are in.
     * @return the units.
     */
    public int getProducerYieldsUnits() {
        return producerYieldsUnits;
    }

    /**
     * Get the units the StatsCan yields are in.
     * @return the units.
     */
    public int getStatsCanYieldsUnits() {
        return statsCanYieldsUnits;
    }

    /**
     * Update the input handler.
     * @param inputHandler the new input handler.
     */
    public void setInputHandler(InputHandler inputHandler) {
        this.inputHandler = inputHandler;
    }

    /**
     * Setter for producerYields map.
     * @param producerYields the yields to set to.
     */
    public void setProducerYields(Map<Integer, ArrayList<Farm>> producerYields) {
        this.producerYields = producerYields;
    }

    /**
     * Setter for statsCanYields map.
     * @param statsCanYields the yields to set to.
     */
    public void setStatsCanYields(Map<Integer, ArrayList<Crop>>  statsCanYields) {
        this.statsCanYields = statsCanYields;
    }

    /**
     * Setter for Producer yields units.
     * @param producerYieldsUnits units final const to be set to.
     */
    public void setProducerYieldsUnits(int producerYieldsUnits) {
        this.producerYieldsUnits = producerYieldsUnits;
    }

    /**
     * Setter for StatsCan yields units.
     * @param statsCanYieldsUnits units final const to be set to.
     */
    public void setStatsCanYieldsUnits(int statsCanYieldsUnits) {
        this.statsCanYieldsUnits = statsCanYieldsUnits;
    }

    /**
     * Driver program.
     * @param args system args.
     */
    public static void main(String[] args) {
        Driver driver = new Driver();
        driver.run();
    }
}
