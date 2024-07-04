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
import android.widget.ProgressBar;

import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.adapters.CategoryAdapter;
import com.example.onlineelectronicgadget.adapters.ProductHomeAdapter;
import com.example.onlineelectronicgadget.database.DatabaseHelper;
import com.example.onlineelectronicgadget.models.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminHomeScreen extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private FloatingActionButton addProductButton;
    private FloatingActionButton editProductButton;
    private DatabaseHelper db;
    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private RecyclerView categoryRecyclerView;
    private CategoryAdapter categoryAdapter;
    private ProductHomeAdapter adapter;
    private List<Product> list;
    private List<String> categories;

    public AdminHomeScreen() {
        // Required empty public constructor
    }

    public static AdminHomeScreen newInstance(String param1, String param2) {
        AdminHomeScreen fragment = new AdminHomeScreen();
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
        View view = inflater.inflate(R.layout.fragment_admin_home_screen, container, false);
        initComponent(view);
        loadProductByCategory("tablet");
        return view;
    }

    private void loadProductByCategory(String category) {
        Map<String, Object> map = new HashMap<>();
        map.put("category", category);

        Log.d("myTag", "in loadProductByCategory()");
        progressBar.setVisibility(View.VISIBLE);

        db.search(map, products -> {
            Log.d("myTag", "in loadProductByCategory()" + products.toString());
            list.clear();
            list.addAll(products);
            adapter.notifyDataSetChanged();
        });

        progressBar.setVisibility(View.GONE);
    }

    private void initComponent(View view) {
        addProductButton = view.findViewById(R.id.addProductButton);
        editProductButton = view.findViewById(R.id.editProductButton);
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
        super.onViewCreated(view, savedInstanceState);
        addProductButton.setOnClickListener(v -> onAddProductButton());
        editProductButton.setOnClickListener(v -> onEditProductButton());

        categoryAdapter = new CategoryAdapter(categories, category -> {
            loadProductByCategory(category.toLowerCase());
            Log.d("myTag", category.toLowerCase());
        });
        loadCategories();
        categoryRecyclerView.setAdapter(categoryAdapter);

        adapter = new ProductHomeAdapter(list, product -> {
            loadFragment(new ProductViewFragment(product));
        });

        recyclerView.setAdapter(adapter);
    }

    private void onEditProductButton() {
        loadFragment(new EditProductFFragment());
    }

    private void onAddProductButton() {
        loadFragment(new AddProductFragment());
    }


    private void loadFragment(Fragment fragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}