package com.example.onlineelectronicgadget.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.adapters.ReviewListAdapter;
import com.example.onlineelectronicgadget.database.DatabaseHelper;
import com.example.onlineelectronicgadget.models.Laptop;
import com.example.onlineelectronicgadget.models.Product;
import com.example.onlineelectronicgadget.models.SmartTv;
import com.example.onlineelectronicgadget.models.SmartWatches;
import com.example.onlineelectronicgadget.models.Tablets;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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
    private TextView spec1;
    private TextView spec2;
    private TextView spec3;
    private TextView spec4;
    private TextView spec5;
    private TextView spec6;
    private TextView spec7;
    private TextView spec8;
    private TextView spec9;
    private TextView spec10;
    private TextView spec11;
    private TextView spec12;
    private TextView spec13;
    private TextView spec14;
    private TextView spec15;
    private TextView spec16;
    private TextView spec17;
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
    private int currentIdx = 0;
    private DatabaseHelper db;

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
                db.saveReview(String.valueOf(et_review.getText()).trim(), product.getId());
                et_review.setText("");
            } else {
                el_review.setError("Enter valid review");
            }
        });

        return view;
    }

    private void loadImages() {
        if (product != null) {
            imageUrls = product.getImagesId();
            if (!imageUrls.isEmpty()) {
                loadImage(imageUrls.get(currentIdx));
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
        spec1 = view.findViewById(R.id.spec1);
        spec2 = view.findViewById(R.id.spec2);
        spec3 = view.findViewById(R.id.spec3);
        spec4 = view.findViewById(R.id.spec4);
        spec5 = view.findViewById(R.id.spec5);
        spec6 = view.findViewById(R.id.spec6);
        spec7 = view.findViewById(R.id.spec7);
        spec8 = view.findViewById(R.id.spec8);
        spec9 = view.findViewById(R.id.spec9);
        spec10 = view.findViewById(R.id.spec10);
        spec11 = view.findViewById(R.id.spec11);
        spec12 = view.findViewById(R.id.spec12);
        spec13 = view.findViewById(R.id.spec13);
        spec14 = view.findViewById(R.id.spec14);
        spec15 = view.findViewById(R.id.spec15);
        spec16 = view.findViewById(R.id.spec16);
        spec17 = view.findViewById(R.id.spec17);
        recyclerView = view.findViewById(R.id.reviewsRecycler);
        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        el_review = view.findViewById(R.id.el_review);
        et_review = view.findViewById(R.id.et_review);
        sendButton = view.findViewById(R.id.sendButton);
        addToCartButton = view.findViewById(R.id.addToCartButton);
        buyNowButton = view.findViewById(R.id.buyNowButton);
        db = new DatabaseHelper();
        
        prevButton.setOnClickListener(v -> showPrevImage());
        nextButton.setOnClickListener(v -> showNextImage());
    }

    private void showNextImage() {
        if (!imageUrls.isEmpty()) {
            currentIdx++;
            if (currentIdx >= imageUrls.size()) {
                currentIdx = 0;
            }
            loadImage(imageUrls.get(currentIdx));
        }
    }

    private void showPrevImage() {
        if (!imageUrls.isEmpty()) {
            currentIdx--;
            if (currentIdx <= 0) {
                currentIdx = imageUrls.size()-1;
            }
            loadImage(imageUrls.get(currentIdx));
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fillView();
    }

    private void fillView() {
        productName.setText(product.getBrand() + " " + product.getModel());
        productPrice.setText("₹ " + String.valueOf(product.getPrice()));
        productDescription.setText(product.getDescription());
        spec1.setText("Category: " + product.getCategory());
        spec1.setVisibility(View.VISIBLE);
        spec2.setText("Rating: " + product.getRating());
        spec2.setVisibility(View.VISIBLE);
        spec3.setText("Stocks: " + product.getStocks());
        spec3.setVisibility(View.VISIBLE);
        Log.d("myTag", product.getReviews().toString());
        adapter = new ReviewListAdapter(product.getReviews());
        recyclerView.setAdapter(adapter);

        if (product instanceof Laptop) fillLaptop();
        else if (product instanceof Tablets) fillTab();
        else if (product instanceof SmartTv) fillTv();
        else if (product instanceof SmartWatches) fillWatch();
    }

    private void fillWatch() {
        SmartWatches smartWatches = (SmartWatches) product;
        spec4.setText("Processor: " + smartWatches.getProcessor());
        spec4.setVisibility(View.VISIBLE);
        spec7.setText("Water Resistance: " + smartWatches.getWaterResistance());
        spec8.setText("Display: " + smartWatches.getDisplay());
        spec8.setVisibility(View.VISIBLE);
        spec10.setText("Battery Life: " + smartWatches.getBatteryLife());
        spec10.setVisibility(View.VISIBLE);
        spec11.setText("Connectivity: " + smartWatches.getConnectivity());
        spec11.setVisibility(View.VISIBLE);
        spec12.setText("Weight: " + smartWatches.getWeight());
        spec12.setVisibility(View.VISIBLE);
        spec13.setText("Sensor: " + smartWatches.getSensor());
        spec13.setVisibility(View.VISIBLE);
        spec14.setText("Color: " + smartWatches.getColor());
        spec14.setVisibility(View.VISIBLE);
        spec15.setText("Warranty: " + smartWatches.getWarranty());
        spec15.setVisibility(View.VISIBLE);
    }

    private void fillTv() {
        SmartTv smartTv = (SmartTv) product;
        spec4.setText("Screen size: " + smartTv.getScreenSize());
        spec4.setVisibility(View.VISIBLE);
        spec5.setText("Resolution: " + smartTv.getResolution());
        spec5.setVisibility(View.VISIBLE);
        spec6.setText("Display Technology: " + smartTv.getDisplayTechnology());
        spec6.setVisibility(View.VISIBLE);
        spec7.setText("Connectivity: " + smartTv.getConnectivity());
        spec7.setVisibility(View.VISIBLE);
        spec8.setText("Smart Features: " + smartTv.getSmartFeatures());
        spec8.setVisibility(View.VISIBLE);
        spec9.setText("Operating System: " + smartTv.getOs());
        spec9.setVisibility(View.VISIBLE);
        spec10.setText("Sound: " + smartTv.getSound());
        spec10.setVisibility(View.VISIBLE);
        spec11.setText("Ports: " + smartTv.getPorts());
        spec11.setVisibility(View.VISIBLE);
        spec12.setText("Weight: " + smartTv.getWeight());
        spec12.setVisibility(View.VISIBLE);
        spec13.setText("Dimension: " + smartTv.getDimension());
        spec13.setVisibility(View.VISIBLE);
        spec14.setText("Color: " + smartTv.getColor());
        spec14.setVisibility(View.VISIBLE);
        spec15.setText("Warranty: " + smartTv.getWarranty());
        spec15.setVisibility(View.VISIBLE);
    }

    private void fillTab() {
        Tablets tablets = (Tablets) product;
        spec4.setText("Processor: " + tablets.getProcessor());
        spec4.setVisibility(View.VISIBLE);
        spec5.setText("Ram: " + tablets.getRam());
        spec5.setVisibility(View.VISIBLE);
        spec6.setText("Storage: " + tablets.getStorage());
        spec6.setVisibility(View.VISIBLE);
        spec7.setText("Camera: " + tablets.getCamera());
        spec7.setVisibility(View.VISIBLE);
        spec8.setText("Display: " + tablets.getDisplay());
        spec8.setVisibility(View.VISIBLE);
        spec9.setText("Operating System: " + tablets.getOs());
        spec9.setVisibility(View.VISIBLE);
        spec10.setText("Battery Life: " + tablets.getBatteryLife());
        spec10.setVisibility(View.VISIBLE);
        spec11.setText("Ports: " + tablets.getPorts());
        spec11.setVisibility(View.VISIBLE);
        spec12.setText("Weight: " + tablets.getWeight());
        spec12.setVisibility(View.VISIBLE);
        spec13.setText("Dimension: " + tablets.getDimension());
        spec13.setVisibility(View.VISIBLE);
        spec14.setText("Color: " + tablets.getColor());
        spec14.setVisibility(View.VISIBLE);
        spec15.setText("Warranty: " + tablets.getWarranty());
        spec15.setVisibility(View.VISIBLE);
        spec16.setText("Connectivity: " + tablets.getConnectivity());
        spec16.setVisibility(View.VISIBLE);
    }

    private void fillLaptop() {
        Laptop laptop = (Laptop) product;
        spec4.setText("Processor: " + laptop.getProcessor());
        spec4.setVisibility(View.VISIBLE);
        spec5.setText("Ram: " + laptop.getRam());
        spec5.setVisibility(View.VISIBLE);
        spec6.setText("Storage: " + laptop.getStorage());
        spec6.setVisibility(View.VISIBLE);
        spec7.setText("Graphics: " + laptop.getGraphics());
        spec7.setVisibility(View.VISIBLE);
        spec8.setText("Display: " + laptop.getDisplay());
        spec8.setVisibility(View.VISIBLE);
        spec9.setText("Operating System: " + laptop.getOs());
        spec9.setVisibility(View.VISIBLE);
        spec10.setText("Battery Life: " + laptop.getBatteryLife());
        spec10.setVisibility(View.VISIBLE);
        spec11.setText("Ports: " + laptop.getPorts());
        spec11.setVisibility(View.VISIBLE);
        spec12.setText("Weight: " + laptop.getWeight());
        spec12.setVisibility(View.VISIBLE);
        spec13.setText("Dimension: " + laptop.getDimension());
        spec13.setVisibility(View.VISIBLE);
        spec14.setText("Color: " + laptop.getColor());
        spec14.setVisibility(View.VISIBLE);
        spec15.setText("Warranty: " + laptop.getWarranty());
        spec15.setVisibility(View.VISIBLE);
    }
}