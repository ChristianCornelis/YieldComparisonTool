package project;

public class Farm {
    private String name;
    private String location;
    private Crop crop;

    /**
     * Constructor.
     * @param farmName the farm name
     * @param farmLocation the farm location
     * @param cropType the type of crop grown on the farm in a given year
     * @param cropYield the yield of the crop grown
     * @param cropUnits the units the crop is in.
     */
    public Farm(String farmName, String farmLocation, String cropType, double cropYield, int cropUnits) {
        name = farmName;
        location = farmLocation;
        crop = new Crop(cropType, cropYield, cropUnits);
    }

    /**
     * Retrieves the farm name.
     * @return the farm name, string
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the farm location.
     * @return the farm location, string
     */
    public String getLocation() {
        return location;
    }

    /**
     * Sets the farm location.
     * @param location the new location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Sets the farm name.
     * @param name String: the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a string containing all attributes of the farm class.
     * @return string containing all info.
     */
    @Override
    public String toString() {
        return "Farm{" +
                "name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", crop=" + crop.toString() +
                '}';
    }
}
