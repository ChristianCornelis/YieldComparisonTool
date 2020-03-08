package project.database;

import project.data.Crop;

import java.util.ArrayList;
import java.util.Map;

/**
 * Interface for producer DBs.
 */
public interface ProducerDatabase {

    /**
     * Adds a new producer yield record to the database.
     * @param year the year of the yield
     * @param yield the yield datastructure, a Farm object in this case (extends Crop).
     */
    void addNewProducerYield(int year, Crop yield);

    /**
     * Retrieve all remotely-stored yields for a particular producer.
     * @param producer the producer name.
     * @return map of all yields for the given producer.
     */
    Map<Integer, ArrayList<Crop>> retrieveProducerYields(String producer);
}
