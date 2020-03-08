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
//import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
//import com.google.cloud.firestore.WriteResult;
//import com.google.cloud.firestore.FieldValue;

import project.Exceptions;
import project.data.Crop;
import project.data.Farm;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * Controller for database connections.
 */
public class DatabaseClient implements StatsCanDatabase, ProducerDatabase, YieldDatabase {
//    private CollectionReference producers;
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
            System.err.println(e);
            System.exit(1);
        }
    }

    /**
     * Adds a new producer yield record to the database.
     * @param year the year of the yield
     * @param yield the yield datastructure, a Farm object in this case (extends Crop).
     */
    public void addNewProducerYield(int year, Crop yield) {
        CollectionReference colRef = dbClient.collection("producerYields");
        try {
            addNewYield(colRef, year, yield);
        } catch (Exceptions.DatabaseWriteException dwe) {
            System.out.println(dwe.getMessage());
        }
    }

    /**
     * Adds a new StatsCan yield record to the database.
     * @param year the year of the yield
     * @param yield the yield datastructure, a Crop object in this case.
     */
    public void addNewStatsCanYield(int year, Crop yield) {
        CollectionReference colRef = dbClient.collection("statsCanYields");
        try {
            addNewYield(colRef, year, yield);
        } catch (Exceptions.DatabaseWriteException dwe) {
            System.out.println(dwe.getMessage());
        }
    }

    /**
     * Adds a new yield to the db.
     * @param colRef The reference to the collection used to store the new yield.
     * @param year the year of the yield
     * @param yield the yield itself
     * @throws project.Exceptions.DatabaseWriteException if an exception occurs.
     */
    public void addNewYield(CollectionReference colRef, int year, Crop yield)
            throws Exceptions.DatabaseWriteException {
        try {
            Map<String, Object> data = yield.toMap();
            ApiFuture<DocumentReference> addedDocRef = colRef.add(data);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exceptions.DatabaseWriteException("ERROR Failed to write record to database.");

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
     * Converts records from the DB to a map.
     * @param documents database records to be parsed.
     * @return a map containing years as keys
     */
    public Map<Integer, ArrayList<Crop>> convertRecordsToMap(List<QueryDocumentSnapshot> documents) {
        Map<Integer, ArrayList<Crop>> yields = new HashMap<>();
        Crop crop = null;
        for (QueryDocumentSnapshot doc : documents) {
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

}


