package com.example.onlineelectronicgadget.database;

import com.example.onlineelectronicgadget.authentication.Auth;
import com.example.onlineelectronicgadget.models.Laptop;
import com.example.onlineelectronicgadget.models.Order;
import com.example.onlineelectronicgadget.models.Product;

import android.util.Log;
import android.widget.Toast;

import androidx.transition.Transition;

import com.example.onlineelectronicgadget.models.SmartTv;
import com.example.onlineelectronicgadget.models.SmartWatches;
import com.example.onlineelectronicgadget.models.Tablets;
import com.example.onlineelectronicgadget.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DatabaseHelper {
    private final FirebaseFirestore firestore;

    public interface Callback {
        void onComplete(List<Product> list, double total);
    }

    public interface OrderCallback {
        void onCall(List<Order> list);
    }

    public interface FireStoreCallback {
        void onCallback(User user);
    }

    public interface AccountTypeCallback {
        void onCallback(String accType);
    }

    public interface IsProductAlreadyPresentCallback {
        void onCallback(Boolean flag);
    }

    public DatabaseHelper() {
        this.firestore = FirebaseFirestore.getInstance();
    }

    public void getCart(Callback callback) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        List<Product> list = new ArrayList<>();
        if (user != null) {
            firestore.collection("cart").whereEqualTo("uid", user.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                       if (task.isSuccessful()) {
                           for (QueryDocumentSnapshot document : task.getResult()) {
                               Product product = document.get("product", Product.class);
                               list.add(product);
                           }
                           double total = getTotal(list);
                           Log.d("myTag", list.toString());
                           callback.onComplete(list, total);
                           Log.d("mmyTag", "cart retrieve");
                       } else {
                           callback.onComplete(null, 0);
                       }
                    });
        }
    }

    private double getTotal(List<Product> list) {
        double total = 0;
        for (Product product : list) {
            total += product.getPrice();
        }
        return total;
    }

    public void getOrders(OrderCallback listener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        List<Order> list = new ArrayList<>();
        if (user != null) {
            firestore.collection("orders").whereEqualTo("uid", user.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Order order = document.toObject(Order.class);
                                list.add(order);
                            }
                            Log.d("myTag", list.toString());
                            listener.onCall(list);
                        } else {
                            listener.onCall(null);
                        }
                    });
        }
    }

    public void addToCart(Product product, IsProductAlreadyPresentCallback listener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (product != null && user != null) {
            isProductAlreadyPresentInCart(product.getId(), (list,total) -> {
                if (list != null && list.isEmpty()) {
                    DocumentReference docRef = firestore.collection("cart").document();

                    Map<String, Object> map = new HashMap<>();
                    map.put("uid", user.getUid());
                    map.put("product", product);

                    firestore.collection("cart")
                            .document(docRef.getId())
                            .set(map)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Log.d("myTag", "product added to cart");
                                    listener.onCallback(true);
                                } else {
                                    Log.d("myTag", "unable to add to cart");
                                    listener.onCallback(false);
                                }
                            });
                } else {
                    listener.onCallback(false);
                }
            });
        } else {
            Log.d("myTag", "unable to add product to cart");
        }
    }

    public String saveOrder(Product product, IsProductAlreadyPresentCallback listener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (product != null && user != null) {
            DocumentReference docRef = firestore.collection("orders").document();

            Date currentDate = Calendar.getInstance().getTime();
            String formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(currentDate);

            Map<String, Object> map = new HashMap<>();
            map.put("uid", user.getUid());
            map.put("product", product);
            map.put("date", formattedDate);

            firestore.collection("orders")
                    .document(docRef.getId())
                    .set(map)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("myTag", "order placed");
                        } else {
                            Log.d("myTag", "unable to place order");
                        }
                    });

            isProductAlreadyPresentInCart(product.getId(), (list,total) -> {
                if (list != null && !list.isEmpty()) {
                    listener.onCallback(true);
                }
                listener.onCallback(false);
            });

            Map<String, Object> map1 = new HashMap<>();
            map1.put("stocks", product.getStocks()-1);

            firestore.collection("products")
                    .document(product.getId())
                    .update(map1)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Log.d("myTag", "stock is updated successfully");
                        } else {
                            Log.d("myTag", "unable to update stock");
                        }
                    });
        }

        return Auth.currentUser.getAccType();
    }

    public void isProductAlreadyPresentInCart(String id, Callback listener) {
        List<Product> list = new ArrayList<>();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            firestore.collection("cart").whereEqualTo("uid", user.getUid())
                    .whereEqualTo("product.id", id)
                    .get()
                    .addOnCompleteListener(task -> {
                       if (task.isSuccessful()) {
                           for (QueryDocumentSnapshot document : task.getResult()) {
                               Product product = document.get("product", Product.class);
                               list.add(product);
                           }
                           listener.onComplete(list, 0);
                       } else {
                           listener.onComplete(null, 0);
                       }
                    });
        }
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

    public void updateProduct(Product product) {
        if (product != null) {
            Map<String, Object> map = productToMap(product);
            Log.d("myTag", map.toString());
            firestore.collection("products")
                    .whereEqualTo("category", product.getCategory())
                    .whereEqualTo("model", product.getModel())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            boolean updated = false;
                            for (DocumentSnapshot document : task.getResult()) {
                                document.getReference()
                                        .update(map).addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) {
                                                Log.d("myTag", "product updated successfully");
                                            } else {
                                                Log.d("myTag", "unable to update product");
                                            }
                                        });
                                updated = true;
                            }
                            if (!updated) {
                                Log.d("myTag", "No matching documents found");
                            }
                        } else {
                            Log.d("myTag", "check model");
                        }
                    });
        } else {
            Log.d("myTag", "product is null");
        }
    }

    private Map<String, Object> productToMap(Product product) {
        Map<String, Object> map = new HashMap<>();

        if (product.getPrice() != -1) map.put("price", product.getPrice());
        if (product.getDescription() != null && !product.getDescription().isEmpty()) map.put("description", product.getDescription());
        if (product.getStocks() != -1) map.put("stocks", product.getStocks());
        if (product.getImagesId() != null && !product.getImagesId().isEmpty()) map.put("imagesId", FieldValue.arrayUnion(product.getImagesId()));
        if (product instanceof Laptop) {
            Laptop laptop = (Laptop) product;
            if (laptop.getProcessor() != null && !laptop.getProcessor().isEmpty()) map.put("processor", laptop.getProcessor());
            if (laptop.getRam() != null && !laptop.getRam().isEmpty()) map.put("ram", laptop.getRam());
            if (laptop.getStorage() != null && !laptop.getStorage().isEmpty()) map.put("storage", laptop.getStorage());
            if (laptop.getDisplay() != null && !laptop.getDisplay().isEmpty()) map.put("display", laptop.getDisplay());
            if (laptop.getOs() != null && !laptop.getOs().isEmpty()) map.put("os", laptop.getOs());
            if (laptop.getBatteryLife() != null && !laptop.getBatteryLife().isEmpty()) map.put("batteryLife", laptop.getBatteryLife());
            if (laptop.getPorts() != null && !laptop.getPorts().isEmpty()) map.put("ports", laptop.getPorts());
            if (laptop.getWeight() != -1) map.put("weight", laptop.getWeight());
            if (laptop.getColor() != null && !laptop.getColor().isEmpty()) map.put("color", laptop.getColor());
            if (laptop.getWarranty() != null && !laptop.getWarranty().isEmpty()) map.put("warranty", laptop.getWarranty());
            if (laptop.getDimension() != null && !laptop.getDimension().isEmpty()) map.put("dimension", laptop.getDimension());
        }
        if (product instanceof Tablets) {
            Tablets tablets = (Tablets) product;
            if (tablets.getProcessor() != null && !tablets.getProcessor().isEmpty()) map.put("processor", tablets.getProcessor());
            if (tablets.getRam() != null && !tablets.getRam().isEmpty()) map.put("ram", tablets.getRam());
            if (tablets.getStorage() != null && !tablets.getStorage().isEmpty()) map.put("storage", tablets.getStorage());
            if (tablets.getDisplay() != null && !tablets.getDisplay().isEmpty()) map.put("display", tablets.getDisplay());
            if (tablets.getOs() != null && !tablets.getOs().isEmpty()) map.put("os", tablets.getOs());
            if (tablets.getBatteryLife() != null && !tablets.getBatteryLife().isEmpty()) map.put("batteryLife", tablets.getBatteryLife());
            if (tablets.getPorts() != null && !tablets.getPorts().isEmpty()) map.put("ports", tablets.getPorts());
            if (tablets.getWeight() != -1) {
                map.put("weight", tablets.getWeight());
                Log.d("myTag", tablets.getWeight()+"");
            }
            if (tablets.getColor() != null && !tablets.getColor().isEmpty()) map.put("color", tablets.getColor());
            if (tablets.getWarranty() != null && !tablets.getWarranty().isEmpty()) map.put("warranty", tablets.getWarranty());
            if (tablets.getCamera() != null && !tablets.getCamera().isEmpty()) map.put("camera", tablets.getCamera());
            if (tablets.getConnectivity() != null && !tablets.getConnectivity().isEmpty()) map.put("connectivity", tablets.getConnectivity());

        }
        if (product instanceof SmartTv) {
            SmartTv smartTv = (SmartTv) product;
            if (smartTv.getScreenSize() != null && !smartTv.getScreenSize().isEmpty()) map.put("screenSize", smartTv.getScreenSize());
            if (smartTv.getResolution() != null && !smartTv.getResolution().isEmpty()) map.put("resolution", smartTv.getResolution());
            if (smartTv.getDisplayTechnology() != null && !smartTv.getDisplayTechnology().isEmpty()) map.put("displayTechnology", smartTv.getDisplayTechnology());
            if (smartTv.getConnectivity() != null && !smartTv.getConnectivity().isEmpty()) map.put("connectivity", smartTv.getConnectivity());
            if (smartTv.getOs() != null && !smartTv.getOs().isEmpty()) map.put("os", smartTv.getOs());
            if (smartTv.getSmartFeatures() != null && !smartTv.getSmartFeatures().isEmpty()) map.put("smartFeatures", smartTv.getSmartFeatures());
            if (smartTv.getPorts() != null && !smartTv.getPorts().isEmpty()) map.put("ports", smartTv.getPorts());
            if (smartTv.getWeight() != -1) map.put("weight", smartTv.getWeight());
            if (smartTv.getColor() != null && !smartTv.getColor().isEmpty()) map.put("color", smartTv.getColor());
            if (smartTv.getWarranty() != null && !smartTv.getWarranty().isEmpty()) map.put("warranty", smartTv.getWarranty());
            if (smartTv.getDimension() != null && !smartTv.getDimension().isEmpty()) map.put("dimension", smartTv.getDimension());
            if (smartTv.getSound() != null && !smartTv.getSound().isEmpty()) map.put("sound", smartTv.getSound());
        }
        if (product instanceof SmartWatches) {
            SmartWatches smartWatches = (SmartWatches) product;
            if (smartWatches.getProcessor() != null && !smartWatches.getProcessor().isEmpty()) map.put("processor", smartWatches.getProcessor());
            if (smartWatches.getConnectivity() != null && !smartWatches.getConnectivity().isEmpty()) map.put("connectivity", smartWatches.getConnectivity());
            if (smartWatches.getSensor() != null && !smartWatches.getSensor().isEmpty()) map.put("sensor", smartWatches.getSensor());
            if (smartWatches.getDisplay() != null && !smartWatches.getDisplay().isEmpty()) map.put("display", smartWatches.getDisplay());
            if (smartWatches.getWaterResistance() != null && !smartWatches.getWaterResistance().isEmpty()) map.put("waterResistance", smartWatches.getWaterResistance());
            if (smartWatches.getBatteryLife() != null && !smartWatches.getBatteryLife().isEmpty()) map.put("batteryLife", smartWatches.getBatteryLife());
            if (smartWatches.getWeight() != -1) map.put("weight", smartWatches.getWeight());
            if (smartWatches.getConnectivity() != null && !smartWatches.getColor().isEmpty()) map.put("color", smartWatches.getColor());
            if (smartWatches.getWarranty() != null && !smartWatches.getWarranty().isEmpty()) map.put("warranty", smartWatches.getWarranty());
        }

        return map;
    }

    public void removeFromCart(String id, IsProductAlreadyPresentCallback listener) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            firestore.collection("cart").whereEqualTo("uid", user.getUid())
                    .whereEqualTo("product.id", id)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                document.getReference().delete()
                                        .addOnCompleteListener(task1 -> {
                                            if (task1.isSuccessful()) listener.onCallback(true);
                                            else listener.onCallback(false);
                                        });
                            }
                        } else {
                            listener.onCallback(false);
                        }
                    });
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

    public void deleteCart() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            firestore.collection("cart").whereEqualTo("uid", user.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                       if (task.isSuccessful()) {
                           for (DocumentSnapshot document : task.getResult()) {
                               DocumentReference docRef = document.getReference();
                               docRef.delete().addOnCompleteListener(task1 -> {
                                   if (task1.isSuccessful()) {
                                       Log.d("myTag", "document deleted");
                                   } else {
                                       Log.d("myTag", "unable to clear data");
                                   }
                               });
                           }
                       } else {
                           Log.d("myTag", "unable to clear data");
                       }
                    });
        } else {
            Log.d("myTag", "unable to clear data");
        }
    }

    public void deleteOrders() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            firestore.collection("orders").whereEqualTo("uid", user.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                       if (task.isSuccessful()) {
                           for (DocumentSnapshot document : task.getResult()) {
                               DocumentReference docRef = document.getReference();
                               docRef.delete().addOnCompleteListener(task1 -> {
                                           if (task1.isSuccessful()) {
                                               Log.d("myTag", "document deleted");
                                           } else {
                                               Log.d("myTag", "unable to clear data");
                                           }
                                       });
                           }
                       } else {
                           Log.d("myTag", "unable to clear data");
                       }
                    });
        } else {
            Log.d("myTag", "unable to clear data");
        }
    }

    public void getUserAccountType(String email, AccountTypeCallback callback) {
        firestore.collection("users").whereEqualTo("email", email).get()
                .addOnCompleteListener(task -> {
                   if (task.isSuccessful())  {
                       String accType = null;
                       User user = null;
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
                callback.onComplete(productList, 0);
            } else {
                Log.d("myTag", "Error getting document" + task.getException());
                callback.onComplete(new ArrayList<>(), 0);
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
