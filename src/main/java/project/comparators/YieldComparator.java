package project.comparators;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import project.converters.Converter;
import project.data.Crop;
import project.Exceptions.InvalidComparatorParamsException;
import project.Exceptions.BushelsConversionKeyNotFoundException;
import project.data.Farm;

/**
 * Comparator class for yields.
 * Contains logic for both Producer and StatsCan data so as to avoid the need to instantiate two comparators.
 */
public class YieldComparator implements Comparator {
    private Converter converter;
    private int producerUnits;
    private int statsCanUnits;
    private int targetUnits;
    private Map<Integer, ArrayList<Farm>> producerYields;
    private Map<Integer, ArrayList<Crop>> statsCanYields;

    /**
     * Constructor.
     * @param pYieldUnits the producer yield units.
     * @param sYieldUnits the StatsCan yield units.
     * @param tYieldUnits the target units the comparison should be done in.
     * @param pYields the producer yields map.
     * @param sYields the StatsCan yields map.
     */
    public YieldComparator(
            int pYieldUnits, int sYieldUnits, int tYieldUnits,
            Map<Integer, ArrayList<Farm>> pYields, Map<Integer, ArrayList<Crop>> sYields
    ) {
        converter = new Converter();
        producerUnits = pYieldUnits;
        statsCanUnits = sYieldUnits;
        targetUnits = tYieldUnits;
        producerYields = pYields;
        statsCanYields = sYields;
    }

    /**
     * Calculates the difference between the first value and the second value.
     * @param value1 First value to compare.
     * @param value2 Second value to compare.
     * @return the difference.
     */
    public double difference(double value1, double value2) {
        return value1 - value2;
    }

    /**
     * Calculates the intersection of two maps' keysets.
     * @param set1 first map
     * @param set2 second map
     * @return ArrayList representing the intersection.
     */
    public ArrayList<Integer> keyIntersection(Set<Integer> set1, Set<Integer> set2) {
        ArrayList<Integer> intersection = new ArrayList<>();
        for (Integer key : set1) {
            if (set2.contains(key)) {
                intersection.add(key);
            }
        }
        return intersection;
    }

    /**
     * Compares crops in a given year.
     * @param crop the crop to compare.
     * @param year the year to compare yields from.
     * @return the difference between producer and StatsCan yields.
     * @throws InvalidComparatorParamsException if either map does not contain either the year or crop needed.
     * @throws BushelsConversionKeyNotFoundException if a bushel conversion cannot be performed for the crop.
     */
    public double compareCropsByYear(String crop, int year)
            throws InvalidComparatorParamsException, BushelsConversionKeyNotFoundException {
        double producerYield = retrieveProducerCropYield(crop, producerYields.get(year));

        double statsCanYield = retrieveStatsCanCropYield(crop, statsCanYields.get(year));

        return difference(producerYield, statsCanYield);

    }

    /**
     * Format a string representing the comparison units.
     * @param comparisonUnits the units being used to compare.
     * @return the formatted string.
     */
    public String formatUnitsString(int comparisonUnits) {
        String unitStr = "";
        switch (comparisonUnits) {
            case Crop.KG_PER_HA:
                unitStr = "kg/ha";
                break;
            case Crop.LBS_PER_AC:
                unitStr = "lbs/ac";
                break;
            case Crop.BU_PER_AC:
                unitStr = "bu/ac";
                break;
        }
        return unitStr;
    }
    /**
     * Method to format a yield comparison output string.
     * @param diff the difference in yield
     * @param cropType the crop
     * @param year the year
     * @param units the target units
     * @return the pre-built string.
     */
    public String formatComparison(double diff, String cropType, int year, String units) {
        return "The difference between your " + cropType +
                " yield and national avg yields" +
                " in " + year + " was:\n" +
                String.format("%.2f", diff) + " " + units;
    }

    /**
     * Retrieves StatsCan yields if they are present.
     * @param crop the crop type being investigated.
     * @param yields ArrayList of all crop yields from a given year.
     * @return the desired crop yield.
     * @throws InvalidComparatorParamsException if the crop does not exist in the arraylist.
     * @throws BushelsConversionKeyNotFoundException if the crop cannot be converted to bushels if necessary.
     */
    private double retrieveStatsCanCropYield(String crop, ArrayList<Crop> yields)
            throws InvalidComparatorParamsException, BushelsConversionKeyNotFoundException {
        for (Crop yield : yields) {
            if (yield.getType().equals(crop)) {
                if (yield.getUnits() == targetUnits) {
                    return yield.getYield();
                } else {
                    return converter.convertYield(yield.getYield(), crop, statsCanUnits, targetUnits);
                }
            }
        }
        throw new InvalidComparatorParamsException(
                "The Statistics Canada yield map does not contain the crop " + crop + "!"
        );
    }

    /**
     * Retrieves producer yields if they are present.
     * @param crop the crop type being investigated.
     * @param yields ArrayList of all crop yields from a given year.
     * @return the desired crop yield.
     * @throws InvalidComparatorParamsException if the crop does not exist in the arraylist.
     * @throws BushelsConversionKeyNotFoundException if the crop cannot be converted to bushels if necessary.
     */
    private double retrieveProducerCropYield(String crop, ArrayList<Farm> yields)
            throws InvalidComparatorParamsException, BushelsConversionKeyNotFoundException {
        for (Farm yield : yields) {
            if (yield.getCrop().getType().equals(crop)) {
                if (yield.getCrop().getUnits() == targetUnits) {
                    return yield.getCrop().getYield();
                } else {
                    return converter.convertYield(yield.getCrop().getYield(), crop, producerUnits, targetUnits);
                }
            }
        }
        throw new InvalidComparatorParamsException(
                "The Producer yield map does not contain the crop " + crop + "!"
        );
    }

    /**
     * Get the converter object in use.
     * @return the converter.
     */
    public Converter getConverter() {
        return converter;
    }

    /**
     * Get the units the producer yields are in.
     * @return the producer yields unit.
     */
    public int getProducerUnits() {
        return producerUnits;
    }

    /**
     * Get the units the StatsCan yields are in.
     * @return the statscan yield units.
     */
    public int getStatsCanUnits() {
        return statsCanUnits;
    }

    /**
     * get the targetted units for comparison.
     * @return the taget units.
     */
    public int getTargetUnits() {
        return targetUnits;
    }

    /**
     * Get the statsCan units being used.
     * @return the units.
     */
    public Map<Integer, ArrayList<Crop>> getStatsCanYields() {
        return statsCanYields;
    }

    // No getter for Producer Units - meant to stay private to users.


    /**
     * Set the target units.
     * @param targetUnits the target units
     */
    public void setTargetUnits(int targetUnits) {
        this.targetUnits = targetUnits;
    }

    /**
     * Update the StatsCan yields in use.
     * @param statsCanYields the new StatsCan yields.
     */
    public void setStatsCanYields(Map<Integer, ArrayList<Crop>> statsCanYields) {
        this.statsCanYields = statsCanYields;
    }

    /**
     * Update the StatsCan units in use.
     * @param statsCanUnits the new statsCanUnits
     */
    public void setStatsCanUnits(int statsCanUnits) {
        this.statsCanUnits = statsCanUnits;
    }
    /**
     * Update the converter in use.
     * @param converter the new converter.
     */
    public void setConverter(Converter converter) {
        this.converter = converter;
    }

}
