package project.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Crop {
    public static final int KG_PER_HA = 0;
    public static final int LBS_PER_AC = 1;
     public static final int BU_PER_AC = 2;

    private int units;
    private String type;
    private double yield;
    private int year;

    /**
     * Constructor.
     * @param cropType The crop type
     * @param cropYield The crop yield value
     * @param cropUnits The units that the yield is in
     * @param yr the year of the yield.
     */
    public Crop(String cropType, double cropYield, int cropUnits, int yr) {
        this.type = cropType;
        this.yield = cropYield;
        this.units = cropUnits;
        this.year = yr;
    }

    /**
     * No-arg constructor for deserializing objects from Firebase.
     */
    public Crop() {

    }
    /**
     * Getter for yield.
     * @return The yield of the crop
     */
    public double getYield() {
        return yield;
    }

    /**
     * The getter for the units of the yield.
     * @return The yield units.
     */
    public int getUnits() {
        return units;
    }

    /**
     * The getter for the crop type.
     * @return the crop type
     */
    public String getType() {
        return type;
    }

    /**
     * The getter for the year of the crop.
     * @return the year.
     */
    public int getYear() {
        return year;
    }

    /**
     * Setter for the yield.
     * @param toSet The yield to set to
     */
    public void setYield(double toSet) {
        this.yield = toSet;
    }

    /**
     * Setter for the units.
     * @param toSet The units to set the crop to
     */
    public void setUnits(int toSet) {
        this.units = toSet;
    }

    /**
     * Setter for the crop type.
     * @param toSet the type to set the crop to
     */
    public void setType(String toSet) {
        this.type = toSet;
    }

    /**
     * Setter for the year.
     * @param year the year to set the year to.
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * ToString method for debugging.
     * @return all attributes of the object in a string
     */
    @Override
    public String toString() {
        return "Crop{"
                + "units=" + units
                + ", type='" + type + '\''
                + ", yield=" + yield
                + ", year=" + year
                + '}';
    }

    /**
     * Returns a map of the instance variables. Used for database operations.
     * @return a Map with instance variable names as keys and values of those variables as values.
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        map.put("yield", yield);
        map.put("type", type);
        map.put("units", units);
        map.put("year", year);
        return map;
    }

    /**
     * Equals method to check for equality between objects.
     * @param o object to compare.
     * @return true if the objects contain the same info, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Crop)) return false;
        Crop crop = (Crop) o;
        return getUnits() == crop.getUnits() &&
                Double.compare(crop.getYield(), getYield()) == 0 &&
                getYear() == crop.getYear() &&
                getType().equals(crop.getType());
    }

    /**
     * Method to get the has values for the instance variables.
     * @return hashcode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getUnits(), getType(), getYield(), getYear());
    }
}
