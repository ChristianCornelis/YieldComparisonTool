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
// [START fs_include_dependencies]
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
// [END fs_include_dependencies]
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for database connections.
 */
public class DatabaseController {
    private CollectionReference producers;
    /**
     * Inits the connection to the db.
     */
    public DatabaseController() {
        try {
            FileInputStream serviceAccount = new FileInputStream("serviceAccountKey.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://yieldcomparisontool.firebaseio.com")
                    .build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            System.out.println("ERROR Invalid Firebase Service account credentials.");
            System.err.println(e);
        }
        Firestore db = FirestoreClient.getFirestore();
        producers = db.collection("producers");

        try {
            System.out.println("pls");
            ApiFuture<QuerySnapshot> query = db.collection("producerYields").get();
            QuerySnapshot querySnapshot = query.get();
            List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
            for (QueryDocumentSnapshot document : documents) {
                System.out.println("ID: " + document.getId());
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        // asynchronously retrieve all users

    }

    public void createNewProducer(String producerName, String city, String province) {
        try {
            DocumentReference docRef = producers.document(producerName);
            Map<String, Object> data = new HashMap<>();
            data.put("city", city);
            data.put("province", province);
            ApiFuture<WriteResult> result = docRef.set(data);
            // ...
            // result.get() blocks on response
            System.out.println("Update time : " + result.get().getUpdateTime());
        } catch (Exception e) {
            //TODO: Create and throw a custom firebase exception here
            System.out.println(e.getMessage());
        }

    }

    public static void main(String[] args) {
        DatabaseController dc = new DatabaseController();
        dc.createNewProducer("Woo sus", "Gurjuelph", "ON");
    }
}


