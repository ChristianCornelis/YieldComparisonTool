package project;

import project.data.Crop;

import java.util.ArrayList;

/**
 * Helper class for building prompt status strings.
 */
public abstract class PromptHelper {

    public static final int IMPORT_PRODUCER_CSV = 0;
    public static final int IMPORT_STATSCAN_CSV = 1;
    public static final int COMPARE_PRODUCER_STATSCAN_YIELDS = 2;

    public static final int IMPORT_KG_PER_HA = 0;
    public static final int IMPORT_LB_PER_AC = 1;
    public static final int IMPORT_BU_PER_AC = 2;

    public static final int CANCEL_TASK = 3;
    public static final int DELETE_PRODUCER_RECORD = 4;
    public static final int QUIT = 5;

    /**
     * Builds a string representing the status of a given map.
     * @param mapName the name of the map
     * @param isPopulated whether the map is populated
     * @return the formatted string.
     */
    public String outputMapStatus(String mapName, Boolean isPopulated) {
        return "The " + mapName + " yield map " + (isPopulated ? ("is populated.") : ("is not populated."));
    }

    /**
     * Helper to build a prompt for choosing a year.
     * @return the string.
     */
    public String getYearsPrompt() {
        return "Enter a year to compare yields from:\n";
    }

    /**
     * Builds a string with all valid years.
     * @param years the arraylist of valid years.
     * @return a string of all valid years.
     */
    public String getInvalidYearPrompt(ArrayList<Integer> years) {
        StringBuilder stringBuilder = new StringBuilder(
                "That year does not contain data for the given crop in both sets of data.\n" +
                "Choose a different year from the following please:\n");
        for (Integer year : years) {
            stringBuilder.append(year + "\n");
        }
        return stringBuilder.toString();
    }

    /**
     * Helper to build prompt for units.
     * @param customPrompt a string to be prepended to the prompt. For re-using this method.
     * @return the pre-built prompt to be output.
     */
    public String getUnitsPrompt(String customPrompt) {
        return  customPrompt +
                "(" + Crop.KG_PER_HA + ") - Kilograms per hectare (kg/ha)" +
                "\n(" + Crop.LBS_PER_AC + ") - Pounds per acre (lbs/ac)" +
                "\n(" + Crop.BU_PER_AC + ") - Bushels per acre (bu/ac)" +
                "\n(" + CANCEL_TASK + ") - Cancel this task";
    }

    /**
     * Helper to represent prompt for entering a crop type to compare against.
     * @return the prompt
     */
    public String getCropPrompt() {
        return "Enter the crop type you wish to compare yields for:\n";
    }

    /**
     * Gets the prompt for a file location to import.
     * @return the prompt
     */
    public String getFileLocationPrompt() {
        return "Enter the path to the CSV you would like to import.\n" +
                "Paths should be relative to " + System.getProperty("user.dir") + "\n";
    }

    /**
     * Prompt for an action to perform.
     * @return the string to output.
     */
    public String getActionPrompt() {
        return "What would you like to do?\n" +
                "\n(" + IMPORT_PRODUCER_CSV + ") - Import a producer yield CSV." +
                "\n(" + IMPORT_STATSCAN_CSV + ") - Import a Statistics Canada average yield CSV." +
                "\n(" + COMPARE_PRODUCER_STATSCAN_YIELDS +
                ") - Compare producer yield data against Statistics Canada average yield data." +
                "\n(" + DELETE_PRODUCER_RECORD +
                ") - Delete producer record(s)" +
                "\n(" + QUIT + ") - Quit";
    }

    /**
     * Prompt for getting the name of a producer at import-time.
     * @return the string to output.
     */
    public static String getProducerPrompt() {
        return "Welcome to the Yield Comparison Tool!\nEnter the producer name you wish to use the application as:";
    }

    /**
     * Prompt for welcoming back the user.
     * @param producer the producer
     * @return the string containing the prompt.
     */
    public String getWelcomeBackMsg(String producer) {
        return "The previously-imported yields for producer '" + producer + "' have been re-loaded.";
    }

    /**
     * Prompt for successful retrieval of previously-loaded StatsCan data.
     * @return the string to be output.
     */
    public String getStatsCanDataLoadedMsg() {
        return "The previously-imported yields from Statistics Canada have been re-loaded.";
    }

    /**
     * Get prompt for producer deletion options.
     * @return the string.
     */
    public String getProducerDeletionPrompt() {
        return "Enter a valid option for producer record deletion below:\n" +
                "\n(" + DeletionHandler.DELETE_ALL_RECORDS +
                ") - Delete all records imported by the current producer." +
                "\n(" + DeletionHandler.DELETE_RECORDS_BY_YEAR +
                ") - Delete all records imported by the current producer in a specific year." +
                "\n(" + DeletionHandler.DELETE_RECORDS_BY_RANGE +
                ") - Delete all records imported by the current producer in a range of years.";
    }

    /**
     * Get the prompt for selecting a year to delete records from.
     * @return the string
     */
    public String getProducerDeletionByYearPrompt() {
        return "Enter a year to delete all producer records from:";
    }
}
