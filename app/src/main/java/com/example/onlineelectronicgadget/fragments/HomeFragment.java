package com.example.onlineelectronicgadget.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextClock;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.database.DatabaseHelper;
import com.example.onlineelectronicgadget.models.Laptop;
import com.example.onlineelectronicgadget.models.Product;
import com.example.onlineelectronicgadget.models.SmartTv;
import com.example.onlineelectronicgadget.models.SmartWatches;
import com.example.onlineelectronicgadget.models.Tablets;

import java.util.HashMap;

public class HomeFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private ImageView laptopImage;
    private ImageView tabImage;
    private ImageView watchImage;
    private ImageView tvImage;
    private TextView laptopName;
    private TextView tabName;
    private TextView watchName;
    private TextView tvName;
    private TextView laptopDesc;
    private TextView tabDesc;
    private TextView watchDesc;
    private TextView tvDesc;
    private Laptop laptop;
    private SmartTv tv;
    private SmartWatches watch;
    private Tablets tablet;
    private DatabaseHelper db;
    private ProgressBar progressBar;
    private LinearLayout linearLayout;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d("myTag", "in HomeFragment onCreate()");
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("myTag", "in HomeFragment onCreateView()");
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initComponent(view);

        return view;
    }

    private void initComponent(View view) {
        Log.d("myTag", "in HomeFragment initComponent()");
        laptopImage = view.findViewById(R.id.laptopImage);
        tabImage = view.findViewById(R.id.tabImage);
        watchImage = view.findViewById(R.id.watchImage);
        tvImage = view.findViewById(R.id.tvImage);
        laptopName = view.findViewById(R.id.laptopName);
        tabName = view.findViewById(R.id.tabName);
        watchName = view.findViewById(R.id.watchName);
        tvName = view.findViewById(R.id.tvName);
        laptopDesc = view.findViewById(R.id.laptopDesc);
        tabDesc = view.findViewById(R.id.tabDesc);
        watchDesc = view.findViewById(R.id.watchDesc);
        tvDesc = view.findViewById(R.id.tvDesc);
        db = new DatabaseHelper();
        progressBar = view.findViewById(R.id.progressBar);
        linearLayout = view.findViewById(R.id.linearLayout);

        laptopImage.setOnClickListener(this);
        tvImage.setOnClickListener(this);
        tabImage.setOnClickListener(this);
        watchImage.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("myTag", "in HomeFragment onViewCreated()");
        super.onViewCreated(view, savedInstanceState);
        linearLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        loadProduct(() -> {
            progressBar.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public void onClick(View v) {
        Log.d("myTag", "in HomeFragment onClick()");
        Product clickedProduct = null;

        int id = v.getId();
        if (id == R.id.laptopImage) clickedProduct = laptop;
        else if (id == R.id.tabImage) clickedProduct = tablet;
        else if (id == R.id.watchImage) clickedProduct = watch;
        else if (id == R.id.tvImage) clickedProduct = tv;

        if (clickedProduct != null) {
            loadFragment(new ProductViewFragment(clickedProduct));
        }
    }

    private void loadProduct(Runnable onComplete) {
        Log.d("myTag", "in HomeFragment loadProduct()");
        populateGrid("laptop", laptopImage, laptopName, laptopDesc, onComplete);
        populateGrid("tablet", tabImage, tabName, tabDesc, onComplete);
        populateGrid("tv", tvImage, tvName, tvDesc, onComplete);
        populateGrid("watch", watchImage, watchName, watchDesc, onComplete);
    }

    private void populateGrid(String category, ImageView imageView, TextView tv_name, TextView tv_desc, Runnable onComplete) {
        Log.d("myTag", "in HomeFragment populatedGrid()");
        db.search(new HashMap<String, Object>(){{
            put("category", category);
        }}, list -> {
            if (list != null & !list.isEmpty()) {
                Product product = list.get(0);

                if (category.equals("laptop")) laptop = (Laptop) list.get(0);
                else if (category.equals("watch")) watch = (SmartWatches) list.get(0);
                else if (category.equals("tv")) tv = (SmartTv) list.get(0);
                else if (category.equals("tablet")) tablet = (Tablets) list.get(0);

                if (product.getImagesId() != null && !product.getImagesId().isEmpty()) {
                    Glide.with(getContext())
                            .load(product.getImagesId().get(0))
                            .placeholder(R.drawable.laptop)
                            .into(imageView);
                } else {
                    Glide.with(getContext())
                            .load(R.drawable.ic_launcher_foreground)
                            .placeholder(R.drawable.laptop)
                            .into(imageView);
                }
                tv_name.setText(list.get(0).getBrand() + " " + list.get(0).getModel());
                tv_desc.setText(list.get(0).getDescription());
            }
        });
        onComplete.run();
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}