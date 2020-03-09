package project.importers;

import project.data.Crop;

import java.util.ArrayList;
import java.util.Map;

/**
 * Interface for caching methods.
 */
public interface CacheHelper {
    /**
     * Method to check caches for records that already exist.
     * @param cache the cache to check the record for
     * @param record the record to check for.
     * @return True if the record exists, false otherwise.
     */
    boolean exists(Map<Integer, ArrayList<Crop>> cache, Crop record);
}
