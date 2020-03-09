package project.database;

import project.data.Crop;

import java.util.ArrayList;
import java.util.Map;

/**
 * Interface for StatsCan database functionality.
 */
public interface StatsCanDatabase {
    /**
     * Adds a new StatsCan yield record to the database.
     * @param yield the yield datastructure, a Crop object in this case.
     */
    void addNewStatsCanYield(Crop yield);
/**
 * Retrieve all remotely-stored yields from StatsCan.
 * @return map of all StatsCan yields.
 */

    Map<Integer, ArrayList<Crop>> retrieveStatsCanYields();
}
