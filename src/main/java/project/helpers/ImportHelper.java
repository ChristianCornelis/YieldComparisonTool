package project.helpers;

import project.data.Crop;
import project.importers.ProducerCSVImporter;
import project.importers.StatsCanCSVImporter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Map;

import static project.helpers.PromptHelper.*;
import static project.helpers.PromptHelper.IMPORT_PRODUCER_CSV;

/**
 * Helper class to handle connecting user input to importers.
 * Temporary - web app will replace this class.
 */
public class ImportHelper {
    private String producer;
    private InputHandler inputHandler;
    private Map<Integer, ArrayList<Crop>> yields;
    private int yieldUnits;
    private Map<Integer, ArrayList<Crop>> statsCanCache;
    private Map<Integer, ArrayList<Crop>> producerCache;

    /**
     * Constructor.
     * @param ih the input handler instance
     * @param producerName producer name
     * @param statsCanYields cached statsCanYields
     * @param producerYields cached producerYields.
     */
    public ImportHelper(InputHandler ih, String producerName, Map<Integer,
            ArrayList<Crop>> statsCanYields,  Map<Integer, ArrayList<Crop>> producerYields) {
        inputHandler = ih;
        producer = producerName;
        statsCanCache = statsCanYields;
        producerCache = producerYields;
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
     * Helper to call on Producer data importer.
     * @param sourceUnits the units the source data is in
     * @param fileLocation the location of the CSV to be imported
     */
    private void importProducerCSV(int sourceUnits, String fileLocation) {
        setYieldUnits(sourceUnits);
        try {
            ProducerCSVImporter pci = new ProducerCSVImporter(fileLocation, sourceUnits, producer, getStatsCanCache());
            pci.parse();
            //set the yields to the merged return value - the cache is now updated!
            setYields(pci.getYields());
            //have to merge maps to avoid kicking caches - need to avoid unnecessary queries
//            setProducerYields(mergeMaps(pci.getYields(), getProducerYields()));
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
        setYieldUnits(sourceUnits);
        try {
            StatsCanCSVImporter sci = new StatsCanCSVImporter(fileLocation, sourceUnits, getProducerCache());
            sci.parse();
            setYields(sci.getYields());
//            setStatsCanYields(mergeMaps(sci.getYields(), getStatsCanYields()));
            System.out.println(inputHandler.outputMapStatus("StatsCan yields", true));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
            System.out.println(inputHandler.outputMapStatus("StatsCan yields", false));
        }
    }

    /**
     * Getter for yields.
     * @return the yields
     */
    public Map<Integer, ArrayList<Crop>> getYields() {
        return yields;
    }

    /**
     * Getter for yield units.
     * @return the yield units.
     */
    public int getYieldUnits() {
        return yieldUnits;
    }

    /**
     * Getter for producer cache.
     * @return the cache.
     */
    public Map<Integer, ArrayList<Crop>> getProducerCache() {
        return producerCache;
    }

    /**
     * Getter for statscancache.
     * @return the cache.
     */
    public Map<Integer, ArrayList<Crop>> getStatsCanCache() {
        return statsCanCache;
    }

    /**
     * Setter for yields.
     * @param yields the yields to be set.
     */
    public void setYields(Map<Integer, ArrayList<Crop>> yields) {
        this.yields = yields;
    }

    /**
     * Setter for yield units.
     * @param yieldUnits the yield units.
     */
    public void setYieldUnits(int yieldUnits) {
        this.yieldUnits = yieldUnits;
    }
}
