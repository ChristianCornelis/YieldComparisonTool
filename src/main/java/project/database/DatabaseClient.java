package project.database;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.*;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.cloud.firestore.CollectionReference;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;

import project.Exceptions;
import project.converters.Converter;
import project.data.Crop;
import project.data.Farm;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Client for database connections.
 */
public class DatabaseClient implements StatsCanDatabase, ProducerDatabase, YieldDatabase {
    private Firestore dbClient;
    /**
     * Inits the connection to the db.
     */
    public DatabaseClient() {
        initConnection();
        dbClient = FirestoreClient.getFirestore();
    }

    /**
     * Inits a connection to the database.
     */
    public void initConnection() {
        try {
            FileInputStream serviceAccount = new FileInputStream("serviceAccountKey.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://yieldcomparisontool.firebaseio.com")
                    .build();
            try {
                if (FirebaseApp.getInstance(FirebaseApp.DEFAULT_APP_NAME) == null) {
                    FirebaseApp.initializeApp(options);
                }
            } catch (IllegalStateException e) {
                FirebaseApp.initializeApp(options);
            }
        } catch (IOException e) {
            System.out.println("ERROR Invalid Firebase Service account credentials.");
            System.out.println(e);
            System.exit(1);
        }
    }

    /**
     * Adds a new producer yield record to the database.
     * @param yield the yield datastructure, a Farm object in this case (extends Crop).
     */
    public void addNewProducerYield(Crop yield) {
        CollectionReference colRef = dbClient.collection("producerYields");
        try {
            addNewYield(colRef, yield);
        } catch (Exceptions.DatabaseWriteException dwe) {
            System.out.println(dwe.getMessage());
        }
    }

    /**
     * Adds a new StatsCan yield record to the database.
     * @param yield the yield datastructure, a Crop object in this case.
     */
    public void addNewStatsCanYield(Crop yield) {
        CollectionReference colRef = dbClient.collection("statsCanYields");
        try {
            addNewYield(colRef, yield);
        } catch (Exceptions.DatabaseWriteException dwe) {
            System.out.println(dwe.getMessage());
        }
    }

    /**
     * Adds a new yield to the db.
     * @param colRef The reference to the collection used to store the new yield.
     * @param yield the yield itself
     * @throws project.Exceptions.DatabaseWriteException if an exception occurs.
     */
    public void addNewYield(CollectionReference colRef, Crop yield)
            throws Exceptions.DatabaseWriteException {
        try {
            yield = validateYieldUnits(yield);  //convert units to metric
            Map<String, Object> data = yield.toMap();
            ApiFuture<DocumentReference> addedDocRef = colRef.add(data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exceptions.DatabaseWriteException("ERROR Failed to write record to database.\n" + yield.toString());

        }
    }

    /**
     * Retrieve all remotely-stored yields for a particular producer.
     * @param producer the producer name.
     * @return map of all yields for the given producer.
     */
    public Map<Integer, ArrayList<Crop>> retrieveProducerYields(String producer) {
        CollectionReference colRef = dbClient.collection("producerYields");
        ApiFuture<QuerySnapshot> future = colRef.whereEqualTo("producer", producer).get();
        List<QueryDocumentSnapshot> documents = null;
        try {
            documents = future.get().getDocuments();
        } catch (Exception e) {
            //TODO: Custom error here.
            System.out.println(e.getMessage());
        }
        return convertRecordsToMap(documents);
    }

    /**
     * Retrieve all remotely-stored yields from StatsCan.
     * @return map of all StatsCan yields.
     */
    public Map<Integer, ArrayList<Crop>> retrieveStatsCanYields() {
        CollectionReference colRef = dbClient.collection("statsCanYields");
        ApiFuture<QuerySnapshot> future = colRef.get();
        List<QueryDocumentSnapshot> documents = null;
        try {
            documents = future.get().getDocuments();
        } catch (Exception e) {
            //TODO: Custom error here.
            System.out.println(e.getMessage());
        }
        return convertRecordsToMap(documents);
    }

    /**
     * Converts records from the DB to a map.
     * @param documents database records to be parsed.
     * @return a map containing years as keys
     */
    public Map<Integer, ArrayList<Crop>> convertRecordsToMap(List<QueryDocumentSnapshot> documents) {
        Map<Integer, ArrayList<Crop>> yields = new HashMap<>();
        Crop crop = null;
        for (QueryDocumentSnapshot doc : documents) {
            //if we have a "producer" field, we're dealing with a Farm object.
            if (doc.getData().keySet().contains("producer")) {
                crop = doc.toObject(Farm.class);
            } else {
                crop = doc.toObject(Crop.class);
            }

            if (!yields.containsKey(crop.getYear())) {
                Crop finalCrop = crop;  //IDEK - weird errors
                yields.put(crop.getYear(), new ArrayList<>() {{ add(finalCrop); }});
            } else {
                yields.get(crop.getYear()).add(crop);
            }
        }

        return yields;
    }

    /**
     * Remove a record matching the year and producer specified.
     * @param year the year
     * @param producer the producer name
     * @throws Exceptions.DatabaseDeletionException on err
     * @throws Exceptions.NoDatabaseRecordsRemovedException on no records deleted.
     */
    public void removeYieldByYearAndProducer(int year, String producer)
            throws Exceptions.DatabaseDeletionException, Exceptions.NoDatabaseRecordsRemovedException {
        Boolean recordsDeleted = false;
        CollectionReference colRef = dbClient.collection("producerYields");
        try {
            ApiFuture<QuerySnapshot> future = colRef.whereEqualTo("producer", producer).whereEqualTo("year", year).get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();
            for (QueryDocumentSnapshot doc : documents) {
                doc.getReference().delete();
                recordsDeleted = true;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exceptions.DatabaseDeletionException(
                    "ERROR failed to delete records with year " + year + " and producer " + producer);
        }
        if (!recordsDeleted) {
            throw new Exceptions.NoDatabaseRecordsRemovedException(
                    "No records to be removed for year " + year + " and producer '" + producer + "'");
        }
    }

    /**
     * Method to validate the yield units. Yield units are only stored in metric values in the database.
     * @param toPut the crop about to be written to the db.
     * @return the crop, with yields in metric units.
     * @throws Exceptions.BushelsConversionKeyNotFoundException if a conversion from bu/ac cannot be performed.
     */
    public Crop validateYieldUnits(Crop toPut) throws Exceptions.BushelsConversionKeyNotFoundException {
        if (toPut.getUnits() != Crop.KG_PER_HA) {
            Converter c = new Converter();
            switch (toPut.getUnits()) {
                case Crop.LBS_PER_AC:
                    toPut.setYield(c.lbsPerAcToKgPerHa(toPut.getYield()));
                    break;
                case Crop.BU_PER_AC:
                    toPut.setYield(c.buPerAcToKgPerHa(toPut.getYield(), toPut.getType()));
                    break;
            }
            toPut.setUnits(Crop.KG_PER_HA);
        }
        return toPut;
    }
}


