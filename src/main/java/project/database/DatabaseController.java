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
//import com.google.cloud.firestore.QueryDocumentSnapshot;
//import com.google.cloud.firestore.QuerySnapshot;
//import com.google.cloud.firestore.WriteResult;
//import com.google.cloud.firestore.FieldValue;

import project.data.Crop;
import project.data.Farm;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Controller for database connections.
 */
public class DatabaseController implements ProducerDatabase, YieldDatabase {
//    private CollectionReference producers;
    private Firestore dbClient;
    /**
     * Inits the connection to the db.
     */
    public DatabaseController() {
        initConnection();
        dbClient = FirestoreClient.getFirestore();
//        producers = dbClient.collection("producers");

//        try {
//            System.out.println("pls");
//            ApiFuture<QuerySnapshot> query = db.collection("producerYields").get();
//            QuerySnapshot querySnapshot = query.get();
//            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
//            for (QueryDocumentSnapshot document : documents) {
//                System.out.println("ID: " + document.getId());
//            }
//        } catch (Exception e) {
//            System.out.println(e.getMessage());
//        }
        // asynchronously retrieve all users

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

//    public void createNewProducer(String producerName, String city, String province) {
//        try {
//            DocumentReference docRef = producers.document(producerName);
//            Map<String, Object> data = new HashMap<>();
//            data.put("city", city);
//            data.put("province", province);
//            ApiFuture<WriteResult> result = docRef.set(data);
//            // ...
//            // result.get() blocks on response
//            System.out.println("Update time : " + result.get().getUpdateTime());
//        } catch (Exception e) {
//            //TODO: Create and throw a custom firebase exception here
//            System.out.println(e.getMessage());
//        }
//    }

    /**
     * Adds a new producer yield record to the database.
     * @param year the year of the yield
     * @param yield the yield datastructure, a Farm object in this case (extends Crop).
     * @param producer the producer
     */
    public void addNewProducerYield(int year, Farm yield, String producer) {
        CollectionReference colRef = dbClient.collection("producerYields");
        addNewYield(colRef, year, yield, producer);
    }

    /**
     * Adds a new yield to the db.
     * @param colRef The reference to the collection used to store the new yield.
     * @param year the year of the yield
     * @param yield the yield itself
     * @param source the source of the yield (StatsCan, or producer name)
     */
    public void addNewYield(CollectionReference colRef, int year, Crop yield, String source) {
        try {
            Map<String, Object> data = yield.toMap();
            data.put("year", year);
            data.put("producer", source);
            ApiFuture<DocumentReference> addedDocRef = colRef.add(data);
        } catch (Exception e) {
            //TODO: Create and throw a custom firebase exception here
            System.out.println(e.getMessage());
        }
    }

    /**
     * Dricer to test db.
     * @param args the args
     */
    public static void main(String[] args) {
        DatabaseController dc = new DatabaseController();
//        dc.createNewProducer("Woo sus", "Gurjuelph", "ON");
        dc.addNewProducerYield(2019, new Farm("TestFarm", "Ailsa Craig", "corn", 1002, Crop.KG_PER_HA), "Test");
    }
}


