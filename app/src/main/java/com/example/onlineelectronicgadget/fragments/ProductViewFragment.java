package com.example.onlineelectronicgadget.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.adapters.ReviewListAdapter;
import com.example.onlineelectronicgadget.adapters.SpecListAdapter;
import com.example.onlineelectronicgadget.database.DatabaseHelper;
import com.example.onlineelectronicgadget.models.Laptop;
import com.example.onlineelectronicgadget.models.Product;
import com.example.onlineelectronicgadget.models.SmartTv;
import com.example.onlineelectronicgadget.models.SmartWatches;
import com.example.onlineelectronicgadget.models.Tablets;
import com.example.onlineelectronicgadget.util.CustomAlertDialog;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ProductViewFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private ImageView productImage;
    private ImageButton prevButton;
    private ImageButton nextButton;
    private TextView productName;
    private TextView productPrice;
    private TextView productDescription;
    private RecyclerView specRecycler;
    private RecyclerView.LayoutManager specLayoutManager;
    private SpecListAdapter specAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ReviewListAdapter adapter;
    private TextInputLayout el_review;
    private TextInputEditText et_review;
    private ImageView sendButton;
    private Button addToCartButton;
    private Button buyNowButton;
    private Product product;
    private List<String> imageUrls;
    private List<String> specs;
    private int currentIdx = 0;
    private DatabaseHelper db;
    private RatingBar ratingBar;

    public ProductViewFragment() {
        // Required empty public constructor
    }

    public ProductViewFragment(Product product) {
        this.product = product;
    }

    public static ProductViewFragment newInstance(String param1, String param2) {
        ProductViewFragment fragment = new ProductViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_view, container, false);
        initComponent(view);
        loadImages();


        sendButton.setOnClickListener(v -> {
            if (!String.valueOf(et_review.getText()).trim().isEmpty()) {
                db.saveReview(String.valueOf(et_review.getText()).trim() + " -by " + FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), product.getId());
                et_review.setText("");
            } else {
                el_review.setError("Enter valid review");
            }
        });

        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> Toast.makeText(getActivity(), "Rating: " + rating, Toast.LENGTH_SHORT).show());

        return view;
    }

    private void loadImages() {
        if (product != null) {
            imageUrls = product.getImagesId();
            if (imageUrls != null) {
                if (!imageUrls.isEmpty()) {
                    loadImage(imageUrls.get(currentIdx));
                } else {
                    loadImage("https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.facebook.com%2FAndroidOfficial%2F&psig=AOvVaw375Tj8OcLsp4ua8_OFGTmB&ust=1720096586337000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCNjGkqLxiocDFQAAAAAdAAAAABAE");
                }
            } else {
                loadImage("https://www.google.com/url?sa=i&url=https%3A%2F%2Fwww.facebook.com%2FAndroidOfficial%2F&psig=AOvVaw375Tj8OcLsp4ua8_OFGTmB&ust=1720096586337000&source=images&cd=vfe&opi=89978449&ved=0CBEQjRxqFwoTCNjGkqLxiocDFQAAAAAdAAAAABAE");
            }

        }
    }

    private void loadImage(String s) {
        Glide
            .with(getActivity())
            .load(s)
            .into(productImage);
    }

    private void initComponent(View view) {
        productImage = view.findViewById(R.id.productImage);
        prevButton = view.findViewById(R.id.prevButton);
        nextButton = view.findViewById(R.id.nextButton);
        productName = view.findViewById(R.id.productName);
        productPrice = view.findViewById(R.id.productPrice);
        productDescription = view.findViewById(R.id.productDescription);
        specRecycler = view.findViewById(R.id.specRecycler);
        specLayoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        specRecycler.setLayoutManager(specLayoutManager);
        recyclerView = view.findViewById(R.id.reviewsRecycler);
        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        el_review = view.findViewById(R.id.el_review);
        et_review = view.findViewById(R.id.et_review);
        sendButton = view.findViewById(R.id.sendButton);
        addToCartButton = view.findViewById(R.id.addToCartButton);
        buyNowButton = view.findViewById(R.id.buyNowButton);
        db = new DatabaseHelper();
        specs = new ArrayList<>();
        ratingBar = view.findViewById(R.id.ratingBar);
        
        prevButton.setOnClickListener(v -> showPrevImage());
        nextButton.setOnClickListener(v -> showNextImage());
        addToCartButton.setOnClickListener(v -> onAddToCartButton());
        buyNowButton.setOnClickListener(v -> onBuyNowButton());
    }

    private void onBuyNowButton() {
        List<Product> list = new ArrayList<>();
        list.add(product);
        loadFragment(new PlaceOrderFragment(list));
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void onAddToCartButton() {
        db.addToCart(product, flag -> {
            if (flag) {
                Log.d("myTag", "product added to cart");
                CustomAlertDialog.showCustomDialog(getContext(), "Info", "Product is added to cart");
                loadFragment(new CartFragment());
            } else {
                Log.d("myTag", "product already in cart");
            }
        });
    }

    private void showNextImage() {
        if (imageUrls != null) {
            if (!imageUrls.isEmpty()) {
                currentIdx++;
                if (currentIdx >= imageUrls.size()) {
                    currentIdx = 0;
                }
                loadImage(imageUrls.get(currentIdx));
            }
        }
    }

    private void showPrevImage() {
        if (imageUrls != null) {
            if (!imageUrls.isEmpty()) {
                currentIdx--;
                if (currentIdx <= 0) {
                    currentIdx = imageUrls.size()-1;
                }
                loadImage(imageUrls.get(currentIdx));
            }
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fillView();
        specAdapter = new SpecListAdapter(specs);
        specRecycler.setAdapter(specAdapter);
    }

    private void fillView() {
        productName.setText(product.getModel());
        productPrice.setText("₹ " + product.getPrice());
        productDescription.setText(product.getDescription());
        if (product.getReviews() != null) adapter = new ReviewListAdapter(product.getReviews());
        else adapter = new ReviewListAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        if (product instanceof Laptop) fillLaptop();
        else if (product instanceof Tablets) fillTab();
        else if (product instanceof SmartTv) fillTv();
        else if (product instanceof SmartWatches) fillWatch();
    }

    private void fillWatch() {
        SmartWatches smartWatches = (SmartWatches) product;
        if (specs != null) {
            specs.add("Category: " + smartWatches.getCategory());
            specs.add("Rating: " + smartWatches.getRating());
            if (smartWatches.getStocks() > 0) {
                specs.add("Stocks: " + smartWatches.getStocks());
            } else {
                specs.add("Stocks: Not Available");
                addToCartButton.setEnabled(false);
                buyNowButton.setEnabled(false);
            }
            specs.add("Processor: " + smartWatches.getProcessor());
            specs.add("Water Resistance: " + smartWatches.getWaterResistance());
            specs.add("Display: " + smartWatches.getDisplay());
            specs.add("Battery Life: " + smartWatches.getBatteryLife());
            specs.add("Connectivity: " + smartWatches.getConnectivity());
            specs.add("Weight: " + smartWatches.getWeight());
            specs.add("Sensor: " + smartWatches.getSensor());
            specs.add("Color: " + smartWatches.getColor());
            specs.add("Warranty: " + smartWatches.getWarranty());
        }
    }

    private void fillTv() {
        SmartTv smartTv = (SmartTv) product;

        if (specs != null) {
            specs.add("Category: " + smartTv.getCategory());
            specs.add("Rating: " + smartTv.getRating());
            if (smartTv.getStocks() > 0) {
                specs.add("Stocks: " + smartTv.getStocks());
            } else {
                specs.add("Stocks: Not Available");
                addToCartButton.setEnabled(false);
                buyNowButton.setEnabled(false);
            }
            specs.add("Screen Size: " + smartTv.getScreenSize());
            specs.add("Resolution: " + smartTv.getResolution());
            specs.add("Display Technology: " + smartTv.getDisplayTechnology());
            specs.add("Smart Features: " + smartTv.getSmartFeatures());
            specs.add("Connectivity: " + smartTv.getConnectivity());
            specs.add("Weight: " + smartTv.getWeight());
            specs.add("Operating System: " + smartTv.getOs());
            specs.add("Color: " + smartTv.getColor());
            specs.add("Warranty: " + smartTv.getWarranty());
            specs.add("Sound: " + smartTv.getSound());
            specs.add("Ports: " + smartTv.getPorts());
            specs.add("Dimensions: " + smartTv.getDimension());
        }
    }

    private void fillTab() {
        Tablets tablets = (Tablets) product;

        if (specs != null) {
            specs.add("Category: " + tablets.getCategory());
            specs.add("Rating: " + tablets.getRating());
            if (tablets.getStocks() > 0) {
                specs.add("Stocks: " + tablets.getStocks());
            } else {
                specs.add("Stocks: Not Available");
                addToCartButton.setEnabled(false);
                buyNowButton.setEnabled(false);
            }
            specs.add("Stocks: " + tablets.getStocks());
            specs.add("Processor: " + tablets.getProcessor());
            specs.add("Ram: " + tablets.getRam());
            specs.add("Display: " + tablets.getDisplay());
            specs.add("Battery Life: " + tablets.getBatteryLife());
            specs.add("Connectivity: " + tablets.getConnectivity());
            specs.add("Weight: " + tablets.getWeight());
            specs.add("Storage: " + tablets.getStorage());
            specs.add("Color: " + tablets.getColor());
            specs.add("Warranty: " + tablets.getWarranty());
            specs.add("Camera: " + tablets.getCamera());
            specs.add("Ports: " + tablets.getPorts());
            specs.add("Dimensions: " + tablets.getDimension());
            specs.add("Operating System: " + tablets.getOs());
        }
    }

    private void fillLaptop() {
        Laptop laptop = (Laptop) product;

        if (specs != null) {
            specs.add("Category: " + laptop.getCategory());
            specs.add("Rating: " + laptop.getRating());
            if (laptop.getStocks() > 0) {
                specs.add("Stocks: " + laptop.getStocks());
            } else {
                addToCartButton.setEnabled(false);
                buyNowButton.setEnabled(false);
                specs.add("Stocks: Not Available");
            }
            specs.add("Processor: " + laptop.getProcessor());
            specs.add("Ram: " + laptop.getRam());
            specs.add("Display: " + laptop.getDisplay());
            specs.add("Battery Life: " + laptop.getBatteryLife());
            specs.add("Graphics: " + laptop.getGraphics());
            specs.add("Weight: " + laptop.getWeight());
            specs.add("Storage: " + laptop.getStorage());
            specs.add("Color: " + laptop.getColor());
            specs.add("Warranty: " + laptop.getWarranty());
            specs.add("Ports: " + laptop.getPorts());
            specs.add("Dimensions: " + laptop.getDimension());
            specs.add("Operating System: " + laptop.getOs());
        }
    }
}