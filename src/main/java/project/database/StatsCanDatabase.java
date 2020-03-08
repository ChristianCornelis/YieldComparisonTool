package project.database;

import project.data.Crop;

/**
 * Interface for StatsCan database functionality.
 */
public interface StatsCanDatabase {
    /**
     * Adds a new StatsCan yield record to the database.
     * @param year the year of the yield
     * @param yield the yield datastructure, a Crop object in this case.
     */
    void addNewStatsCanYield(int year, Crop yield);
}
