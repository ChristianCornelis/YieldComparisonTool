package project.database;
import project.Exceptions;
import project.data.Crop;
import com.google.cloud.firestore.CollectionReference;
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
}
