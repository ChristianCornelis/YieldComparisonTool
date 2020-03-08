package project.database;
import project.Exceptions;
import project.data.Crop;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

/**
 * Interface for all database connections.
 */
public interface YieldDatabase {
    /**
     * Adds a new yield to the db.
     * @param colRef The reference to the collection used to store the new yield.
     * @param year the year of the yield
     * @param yield the yield itself
     * @param source the source of the yield (StatsCan, or producer name)
     * @throws project.Exceptions.DatabaseWriteException if an exception occurs.
     */
   void addNewYield(CollectionReference colRef, int year, Crop yield, String source)
           throws Exceptions.DatabaseWriteException;

    /**
     * Inits a connection to the database.
     */
    void initConnection();

    /**
     * Converts records from the DB to a map.
     * @param documents database records to be parsed.
     * @return a map containing years as keys
     */
    Map<Integer, ArrayList<Crop>> convertRecordsToMap(List<QueryDocumentSnapshot> documents);
}
