package com.example.onlineelectronicgadget.database;

import com.example.onlineelectronicgadget.models.Laptop;
import com.example.onlineelectronicgadget.models.Product;

import android.util.Log;

import androidx.transition.Transition;

import com.example.onlineelectronicgadget.models.SmartTv;
import com.example.onlineelectronicgadget.models.SmartWatches;
import com.example.onlineelectronicgadget.models.Tablets;
import com.example.onlineelectronicgadget.models.User;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper {
    private final FirebaseFirestore firestore;

    public interface Callback {
        void onComplete(List<Product> list);
    }

    public interface FireStoreCallback {
        void onCallback(User user);
    }

    public interface AccountTypeCallback {
        void onCallback(String accType);
    }

    public DatabaseHelper() {
        this.firestore = FirebaseFirestore.getInstance();
    }

    public String saveProduct(Product product) {
        if (product != null) {
            DocumentReference docRef = firestore.collection("products").document();

            firestore.collection("products")
                    .document(docRef.getId())
                    .set(product)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("myTag", "product added");
                        } else {
                            Log.d("myTag", "unable to add product");
                        }
                    });

            return docRef.getId();
        } else {
            return "-1";
        }
    }

    public void deleteAcc(User user) {
        firestore.collection("users").document(user.getId())
                .delete()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("myTag", "database helper => account deleted");
                    }
                }).addOnFailureListener(e -> {
                    Log.d("myTag", "database helper => deletion failed");
                });
    }

    public void getUserAccountType(String email, AccountTypeCallback callback) {
        firestore.collection("users").whereEqualTo("email", email).get()
                .addOnCompleteListener(task -> {
                   if (task.isSuccessful())  {
                       String accType = null;
                       for (QueryDocumentSnapshot document : task.getResult()) {
                           accType = document.getString("accType");
                       }
                       callback.onCallback(accType);
                   } else {
                       callback.onCallback(null);
                   }
                });
    }

    public void updateUser(String uid, String value, String field) {
        Map<String, Object> map = new HashMap<>();
        map.put(field, value);
        Log.d("myTag", "in update user method" + map + "uid => " + uid);

        firestore.collection("users").document(uid)
                .update(map)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("myTag", "user detail updated");
                    }
                }).addOnFailureListener(e -> {
                    Log.d("myTag", "error: " + e.getMessage());
                });
    }

    public void getCurrUser(String uid, FireStoreCallback callback) {
        firestore.collection("users").document(uid).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        User user = task.getResult().toObject(User.class);
                        Log.d("myTag", "current user => " + user.getId());
                        callback.onCallback(user);
                    }
                    else {
                        callback.onCallback(null);
                    }
                }).addOnFailureListener(e -> {
                        callback.onCallback(null);
                });
    }

    public void saveReview(String review, String id) {
        DocumentReference document = firestore.collection("products").document(id);

        document.update("reviews", FieldValue.arrayUnion(review))
                .addOnCompleteListener(task -> {
                    Log.d("myTag", "review added");
                }).addOnFailureListener(e -> {
                    Log.d("myTag", "" + e.getMessage());
                });
    }

    public void saveUser(String uid, String username, String email, String password, String accType) {
        Map<String, Object> user = new HashMap<>();
        user.put("id", uid);
        user.put("username", username);
        user.put("email", email);
        user.put("password", password);
        user.put("accType", accType);

        firestore.collection("users")
                .document(uid)
                .set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("myTag", "user added ");
                    }
                }).addOnFailureListener(e -> {
                    Log.d("myTag", " " + e.getMessage());
                });
    }

    public void search(Map<String, Object> specifications, Callback callback) {
        String category = (String) specifications.remove("category");
        CollectionReference collectionReference = firestore.collection("products");

        Query query = collectionReference.whereEqualTo("category", category);
        Integer minPrice = null, maxPrice = null;


        for (Map.Entry<String, Object> entry : specifications.entrySet()) {
            if (entry.getKey().equals("minPrice")) {
                minPrice = (Integer) entry.getValue();
            } else if (entry.getKey().equals("maxPrice")) {
                maxPrice = (Integer) entry.getValue();
            } else {
                query = query.whereEqualTo(entry.getKey(), entry.getValue());
            }
        }

        if (minPrice != null) {
            query = query.whereGreaterThanOrEqualTo("price", minPrice);
        }
        if (maxPrice != null) {
            query = query.whereLessThanOrEqualTo("price", maxPrice);
        }

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Product> productList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Product product = documentToProduct(document);
                    if (product != null) productList.add(product);
                    Log.d("myTag", product.toString());
                }
                callback.onComplete(productList);
            } else {
                Log.d("myTag", "Error getting document" + task.getException());
            }
        });
    }

    private Product documentToProduct(QueryDocumentSnapshot document) {
        switch (document.getString("category")) {
            case "laptop" :
                return createLaptop(document);
            case "tv" :
                return createTv(document);
            case "watch" :
                return createWatch(document);
            case "tablet" :
                return createTablet(document);
            default:
                return null;
        }
    }

    private Tablets createTablet(QueryDocumentSnapshot document) {
        Tablets tablets = document.toObject(Tablets.class);
        tablets.setId(document.getId());
        return tablets;
    }

    private SmartWatches createWatch(QueryDocumentSnapshot document) {
        SmartWatches smartWatches = document.toObject(SmartWatches.class);
        smartWatches.setId(document.getId());
        return smartWatches;
    }

    private SmartTv createTv(QueryDocumentSnapshot document) {
        SmartTv smartTv = document.toObject(SmartTv.class);
        smartTv.setId(document.getId());
        return smartTv;
    }

    private Laptop createLaptop(QueryDocumentSnapshot document) {
        Laptop laptop = document.toObject(Laptop.class);
        laptop.setId(document.getId());
        return laptop;
    }
}
