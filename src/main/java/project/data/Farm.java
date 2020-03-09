package project.data;

import java.util.Map;

public class Farm extends Crop {
    private String producer;
    private String name;
    private String location;

    /**
     * Constructor.
     * @param farmName the farm name
     * @param farmLocation the farm location
     * @param cropType the type of crop grown on the farm in a given year
     * @param cropYield the yield of the crop grown
     * @param cropUnits the units the crop is in.
     * @param producerName the name of the producer
     * @param year the year.
     */
    public Farm(String farmName, String farmLocation, String cropType,
                double cropYield, int cropUnits, String producerName, int year) {
        super(cropType, cropYield, cropUnits, year);
        name = farmName;
        location = farmLocation;
        producer = producerName;
    }

    /**
     * No-arg constructor for deserializing objects from Firebase.
     */
    public Farm() {

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
     * Returns the producer.
     * @return the producer, string.
     */
    public String getProducer() {
        return producer;
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
     * Setter for producer.
     * @param producer the producer
     */
    public void setProducer(String producer) {
        this.producer = producer;
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
                ", " + super.toString() +
                '}';
    }

    /**
     * Returns a map of the instance variables. Used for database operations.
     * @return a Map with instance variable names as keys and values of those variables as values.
     */
    public Map<String, Object> toMap() {
        Map<String, Object> map = super.toMap();
        map.put("name", name);
        map.put("location", location);
        map.put("producer", producer);

        return map;
    }
}
