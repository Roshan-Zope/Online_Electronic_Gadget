package com.example.onlineelectronicgadget.database;

import com.example.onlineelectronicgadget.models.Laptop;
import com.example.onlineelectronicgadget.models.Product;
import android.util.Log;
import android.widget.Toast;

import com.example.onlineelectronicgadget.models.SmartTv;
import com.example.onlineelectronicgadget.models.SmartWatches;
import com.example.onlineelectronicgadget.models.Tablets;
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

    public void saveReview(String review, String id) {
        DocumentReference document = firestore.collection("products").document(id);

        document.update("reviews", FieldValue.arrayUnion(review))
                .addOnCompleteListener(task -> {
                    Log.d("myTag", "review added");
                }).addOnFailureListener(e -> {
                    Log.d("myTag", "" + e.getMessage());
                });
    }

    public interface Callback {
        void onComplete(List<Product> list);
    }

    public DatabaseHelper() {
        this.firestore = FirebaseFirestore.getInstance();
    }

    public void saveUser(String username, String email, String password) {
        Map<String, Object> user = new HashMap<>();
        user.put("username", username);
        user.put("email", email);
        user.put("password", password);

        firestore.collection("users")
                .add(user)
                .addOnSuccessListener(documentReference -> Log.d("myTag", "Document referenceId => " + documentReference.getId()))
                .addOnFailureListener(e -> Log.d("myTag", "Failure => " + e));
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
