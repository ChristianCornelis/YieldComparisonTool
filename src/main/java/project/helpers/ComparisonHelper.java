package project.helpers;

import project.Exceptions;
import project.comparators.YieldComparator;
import project.data.Crop;

import java.util.ArrayList;
import java.util.Map;

import static project.helpers.PromptHelper.*;

/**
 * Helper class to handle comparisons - grabs input and calls on concrete classes.
 * Temporary - web app will replace this class.
 */
public class ComparisonHelper {

    private InputHandler inputHandler;
    private Map<Integer, ArrayList<Crop>> statsCanYields;
    private Map<Integer, ArrayList<Crop>> producerYields;

    /**
     * Constructor.
     * @param ih the input handler isntance
     * @param statsCanCache the cached statsCan data
     * @param producerCache the cached producer data
     */
    public ComparisonHelper(InputHandler ih,  Map<Integer,
            ArrayList<Crop>> statsCanCache,  Map<Integer, ArrayList<Crop>> producerCache) {
        inputHandler = ih;
         producerYields = producerCache;
        statsCanYields = statsCanCache;
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
        YieldComparator yc = new YieldComparator(comparisonUnits, producerYields,  statsCanYields
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
}
