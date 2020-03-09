package project.database;

import project.Exceptions;
import project.data.Crop;

import java.util.ArrayList;
import java.util.Map;

/**
 * Interface for producer DBs.
 */
public interface ProducerDatabase {

    /**
     * Adds a new producer yield record to the database.
     * @param yield the yield datastructure, a Farm object in this case (extends Crop).
     */
    void addNewProducerYield(Crop yield);

    /**
     * Retrieve all remotely-stored yields for a particular producer.
     * @param producer the producer name.
     * @return map of all yields for the given producer.
     */
    Map<Integer, ArrayList<Crop>> retrieveProducerYields(String producer);

    /**
     * Remove a record matching the year and producer specified.
     * @param year the year
     * @param producer the producer name
     * @throws project.Exceptions.DatabaseDeletionException if failed.
     * @throws Exceptions.NoDatabaseRecordsRemovedException if no records deleted.
     */
    void removeYieldByYearAndProducer(int year, String producer)
            throws Exceptions.DatabaseDeletionException, Exceptions.NoDatabaseRecordsRemovedException;
}
