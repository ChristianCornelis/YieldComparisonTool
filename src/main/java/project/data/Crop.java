package project.data;

public class Crop {
    public static final int KG_PER_HA = 0;
    public static final int LBS_PER_AC = 1;
     public static final int BU_PER_AC = 2;

    private int units;
    private String type;
    private double yield;

    /**
     * Constructor.
     * @param cropType The crop type
     * @param cropYield The crop yield value
     * @param cropUnits The units that the yield is in
     */
    public Crop(String cropType, double cropYield, int cropUnits) {
        this.type = cropType;
        this.yield = cropYield;
        this.units = cropUnits;
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
     * ToString method for debugging.
     * @return all attributes of the object in a string
     */
    @Override
    public String toString() {
        return "Crop{"
                + "units=" + units
                + ", type='" + type + '\''
                + ", yield=" + yield
                + '}';
    }
}
