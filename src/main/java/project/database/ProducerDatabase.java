package project.database;

import project.data.Farm;

/**
 * Interface for producer DBs.
 */
public interface ProducerDatabase {

    /**
     * Adds a new producer yield record to the database.
     * @param year the year of the yield
     * @param yield the yield datastructure, a Farm object in this case (extends Crop).
     * @param producer the producer
     */
    void addNewProducerYield(int year, Farm yield, String producer);
}
