package project;

import project.comparators.YieldComparator;
import project.data.Crop;
import project.database.DatabaseClient;
import project.helpers.DeletionHandler;
import project.helpers.InputHandler;
import project.helpers.PromptHelper;
import project.importers.ProducerCSVImporter;
import project.importers.StatsCanCSVImporter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static project.helpers.PromptHelper.*;

/**
 * Temporary driver class for the entire project.
 */
public class Driver {
    private InputHandler inputHandler;
    private Map<Integer, ArrayList<Crop>> producerYields;
    private Map<Integer, ArrayList<Crop>> statsCanYields;
    private int producerYieldsUnits;
    private int statsCanYieldsUnits;
    private DatabaseClient dc;
    private String producer;

    /**
     * Constructor.
     */
    public Driver() {
        inputHandler = new InputHandler();
        dc = new DatabaseClient();
        getProducer();
        loadPreviousProducerData();
        loadPreviousStatsCanData();
    }

    /**
     * Re-loads previously-imported producer data.
     */
    public void loadPreviousProducerData() {
        Map<Integer, ArrayList<Crop>> producerYields = dc.retrieveProducerYields(producer);
        if (producerYields.size() != 0) {
            System.out.println(inputHandler.getWelcomeBackMsg(producer));
        }
        setProducerYields(producerYields);
    }

    /**
     * Re-loads previously-imported StatsCan data into the current state of the app.
     */
    public void loadPreviousStatsCanData() {
        Map<Integer, ArrayList<Crop>> statsCanYields = dc.retrieveStatsCanYields();
        if (statsCanYields.size() != 0) {
            System.out.println(inputHandler.getStatsCanDataLoadedMsg());
        }
        setStatsCanYields(statsCanYields);
    }

    /**
     * Imports a file based on an action specified.
     * @param action the action specified based on static constants declared in this class.
     */
    public void importFile(int action) {
        int[] validImportActions = {IMPORT_KG_PER_HA, IMPORT_LB_PER_AC, IMPORT_BU_PER_AC, CANCEL_TASK};
        if (producer.equals("admin") && action == IMPORT_PRODUCER_CSV) {
            System.out.println("Admins cannot import producer data!");
            return;
        }
        int importUnits = inputHandler.chooseAction(
                inputHandler.getUnitsPrompt("Choose the units the yields are in:\n"),
                validImportActions);
        if (importUnits != CANCEL_TASK) {
            System.out.println(inputHandler.getFileLocationPrompt());
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
     * Helper to get the units to be used for comparing crops.
     * @return the constant representing the unit
     */
    private int getComparisonUnits() {
        //Todo: Implement comparison in Bu/ac
        int[] validComparisonActions = {IMPORT_KG_PER_HA, IMPORT_LB_PER_AC, /*IMPORT_BU_PER_AC,*/ CANCEL_TASK};
        return inputHandler.chooseAction(
                inputHandler.getUnitsPrompt("Choose the units you wish to see yield differences in:\n"),
                validComparisonActions
        );
    }

    /**
     * Helper to get the year for comparison.
     * @param yearsIntersection the intersection of all years available for comparing producer and statscan datasets
     * @return the year chosen by the user
     */
    private int getYear(ArrayList<Integer> yearsIntersection) {
        return  inputHandler.chooseAction(
                inputHandler.getYearsPrompt(),
                yearsIntersection,
                inputHandler.getInvalidYearPrompt(yearsIntersection));
    }

    /**
     * Helper to get the crop for comparison.
     * @return the crop name that is to be compared.
     */
    private String getCrop() {
        System.out.println(inputHandler.getCropPrompt());
        return inputHandler.getBasicInput();
    }

    /**
     * Helper to get the producer name.
     */
    private void getProducer() {
        System.out.println(PromptHelper.getProducerPrompt());
        setProducer(inputHandler.getBasicInput());
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
        int comparisonUnits = getComparisonUnits();
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
        int year = getYear(yearsIntersection);
        String crop = getCrop();
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
            int[] validActions = {
                    IMPORT_PRODUCER_CSV, IMPORT_STATSCAN_CSV,
                    COMPARE_PRODUCER_STATSCAN_YIELDS,
                    DELETE_PRODUCER_RECORD, QUIT
            };
            action = inputHandler.chooseAction(inputHandler.getActionPrompt(), validActions);
            if (action == IMPORT_PRODUCER_CSV || action == IMPORT_STATSCAN_CSV) {
                importFile(action);
            } else if (action == COMPARE_PRODUCER_STATSCAN_YIELDS) {
                compareYields();
            } else if (action == DELETE_PRODUCER_RECORD) {
                deleteProducerRecord();
            }
        }

        return;
    }

    private void deleteProducerRecord() {
        DeletionHandler dh = new DeletionHandler(producer, inputHandler, dc);
        dh.deleteProducerRecord();
        //reset cache
        loadPreviousProducerData();
    }
    /**
     * Helper to call on Producer data importer.
     * @param sourceUnits the units the source data is in
     * @param fileLocation the location of the CSV to be imported
     */
    private void importProducerCSV(int sourceUnits, String fileLocation) {
        setProducerYieldsUnits(sourceUnits);
        try {
            ProducerCSVImporter pci = new ProducerCSVImporter(fileLocation, sourceUnits, producer, getProducerYields());
            pci.parse();
            //have to merge maps to avoid kicking caches - need to avoid unnecessary queries
            setProducerYields(mergeMaps(pci.getYields(), getProducerYields()));
            System.out.println(inputHandler.outputMapStatus("Producer yields", true));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println(inputHandler.outputMapStatus("Producer yields", false));
        }
    }

    /**
     * Helper to call on StatsCan data importer.
     * @param sourceUnits the units the source data is in
     * @param fileLocation the location of the CSV to be imported
     */
    private void importStatsCanCSV(int sourceUnits, String fileLocation) {
        setStatsCanYieldsUnits(sourceUnits);
        try {
            StatsCanCSVImporter sci = new StatsCanCSVImporter(fileLocation, sourceUnits, getStatsCanYields());
            sci.parse();
            setStatsCanYields(mergeMaps(sci.getYields(), getStatsCanYields()));
            System.out.println(inputHandler.outputMapStatus("StatsCan yields", true));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println(inputHandler.outputMapStatus("StatsCan yields", false));
        }
    }

    /**
     * Retrieve the StatsCan yields already loaded.
     * @return the StatsCan yields.
     */
    public Map<Integer, ArrayList<Crop>> getStatsCanYields() {
        return statsCanYields;
    }

    /**
     * Getter for producer yields.
     * @return the producer yields.
     */
    public Map<Integer, ArrayList<Crop>> getProducerYields() {
        return producerYields;
    }

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
    public void setProducerYields(Map<Integer, ArrayList<Crop>> producerYields) {
        this.producerYields = producerYields;
    }

    /**
     * Merges two yield caches. Used to maintain state between imports and previously-imported data.
     * @param map1 first map
     * @param map2 second map
     * @return the merged caches.
     */
    public Map<Integer, ArrayList<Crop>> mergeMaps(Map<Integer, ArrayList<Crop>> map1, Map<Integer, ArrayList<Crop>> map2) {
        Map<Integer, ArrayList<Crop>> merged = new HashMap<>(map1);
        merged.putAll(map2);
        return merged;
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
     * Setter for producer.
     * @param producer the producer.
     */
    public void setProducer(String producer) {
        //only set the producer if it is not an administrator!
        if (!producer.toLowerCase().equals("admin")) {
            this.producer = producer;
        } else {
            this.producer = "admin";
        }

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
