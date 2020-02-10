package project;

public abstract class PromptHelper {

    public static final int IMPORT_PRODUCER_CSV = 0;
    public static final int IMPORT_STATSCAN_CSV = 1;
    public static final int COMPARE_PRODUCER_STATSCAN_YIELDS = 2;

    public static final int IMPORT_KG_PER_HA = 0;
    public static final int IMPORT_LB_PER_AC = 1;
    public static final int IMPORT_BU_PER_AC = 2;

    public static final int CANCEL_TASK = 3;
    public static final int QUIT = 4;

    /**
     * Builds a string representing the status of a given map.
     * @param mapName the name of the map
     * @param isPopulated whether the map is populated
     * @return the formatted string.
     */
    public String mapStatus(String mapName, Boolean isPopulated) {
        return "The " + mapName + " yield map " + (isPopulated ? ("is populated.") : ("is not populated."));
    }

    /**
     * Helper to build a prompt for choosing a year.
     * @return the string.
     */
    public String yearsPrompt() {
        return "Enter a year to compare yields from:\n";
    }

    /**
     * Helper to build prompt for units.
     * @param customPrompt a string to be prepended to the prompt. For re-using this method.
     * @return the pre-built prompt to be output.
     */
    public String unitsPrompt(String customPrompt) {
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
    public String cropPrompt() {
        return "Enter the crop type you wish to compare yields for:\n";
    }

    /**
     * Gets the prompt for a file location to import.
     * @return the prompt
     */
    public String fileLocationPrompt() {
        return "Enter the path to the CSV you would like to import.\n" +
                "Paths should be relative to " + System.getProperty("user.dir") + "\n";
    }

    /**
     * Prompt for an action to perform.
     * @return the string to output.
     */
    public String actionPrompt() {
        return "What would you like to do?\n" +
                "\n(" + IMPORT_PRODUCER_CSV + ") - Import a producer yield CSV." +
                "\n(" + IMPORT_STATSCAN_CSV + ") - Import a Statistics Canada average yield CSV." +
                "\n(" + COMPARE_PRODUCER_STATSCAN_YIELDS +
                ") - Compare producer yield data against Statistics Canada average yield data." +
                "\n(" + QUIT + ") - Quit";
    }
}
