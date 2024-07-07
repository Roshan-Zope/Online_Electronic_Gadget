package com.example.onlineelectronicgadget.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.adapters.CategoryAdapter;
import com.example.onlineelectronicgadget.adapters.ProductHomeAdapter;
import com.example.onlineelectronicgadget.adapters.ProductListAdapter;
import com.example.onlineelectronicgadget.database.DatabaseHelper;
import com.example.onlineelectronicgadget.models.Product;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private DatabaseHelper db;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private ProductHomeAdapter adapter;
    private List<Product> list;
    private List<String> categories;

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
        loadProductByCategory("tablet");
        return view;
    }

    private void loadProductByCategory(String category) {
        Map<String, Object> map = new HashMap<>();
        map.put("category", category);

        Log.d("myTag", "in loadProductByCategory()");
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);

        db.search(map, (products, total) -> {
            Log.d("myTag", "in loadProductByCategory()" + products.toString());
            list.clear();
            list.addAll(products);
            progressBar.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        });
    }

    private void initComponent(View view) {
        Log.d("myTag", "in HomeFragment initComponent()");
        db = new DatabaseHelper();
        progressBar = view.findViewById(R.id.progressBar);
        recyclerView = view.findViewById(R.id.recyclerView);
        categoryRecyclerView = view.findViewById(R.id.categoryRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        categories = new ArrayList<>();
        list = new ArrayList<>();

    }

    private void loadCategories() {
        categories.add("Tablet");
        categories.add("Laptop");
        categories.add("Tv");
        categories.add("Watch");
        categoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Log.d("myTag", "in HomeFragment onViewCreated()");
        super.onViewCreated(view, savedInstanceState);
        categoryAdapter = new CategoryAdapter(categories, category -> {
            loadProductByCategory(category.toLowerCase());
            Log.d("myTag", category.toLowerCase());
        });
        loadCategories();
        categoryRecyclerView.setAdapter(categoryAdapter);

        adapter = new ProductHomeAdapter(list, product -> {
//            List<Product> products = new ArrayList<>();
//            products.add(product);
            loadFragment(new ProductViewFragment(product));
        });

        recyclerView.setAdapter(adapter);

    }
    
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}