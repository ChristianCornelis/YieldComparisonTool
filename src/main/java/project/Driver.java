package project;

import project.data.Crop;
import project.database.DatabaseClient;
import project.helpers.*;

import java.util.ArrayList;
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
     * Helper to get the producer name.
     */
    private void getProducer() {
        System.out.println(PromptHelper.getProducerPrompt());
        setProducer(inputHandler.getBasicInput());
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
                ComparisonHelper ch = new ComparisonHelper(inputHandler, statsCanYields, producerYields);
                ch.setupCompareYields();
            } else if (action == DELETE_PRODUCER_RECORD) {
                DeletionHandler dh = new DeletionHandler(producer, inputHandler, dc);
                dh.deleteProducerRecord();
                //reset cache
                loadPreviousProducerData();
            }
        }

        return;
    }

    /**
     * Method to handle caching logic for importing data.
     * @param action the import action.
     */
    private void importFile(int action) {
        ImportHelper ih = new ImportHelper(inputHandler, producer, statsCanYields, producerYields);
        ih.importFile(action);
        switch (action) {
            case IMPORT_PRODUCER_CSV:
                setProducerYields(ih.getYields());
                setProducerYieldsUnits(ih.getYieldUnits());
                break;
            case IMPORT_STATSCAN_CSV:
                setStatsCanYields(ih.getYields());
                setStatsCanYieldsUnits(ih.getYieldUnits());
                break;
        }
        return;
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
     * Setter for producerYields map.
     * @param producerYields the yields to set to.
     */
    public void setProducerYields(Map<Integer, ArrayList<Crop>> producerYields) {
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
