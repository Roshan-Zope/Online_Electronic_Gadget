package com.example.onlineelectronicgadget.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.adapters.OrdersProductListAdapter;
import com.example.onlineelectronicgadget.models.Product;

import java.util.List;

public class OrdersProductListFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private List<Product> products;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private OrdersProductListAdapter adapter;
    private TextView total_amount;
    private double total;

    public OrdersProductListFragment() {
        // Required empty public constructor
    }

    public OrdersProductListFragment(List<Product> products, double total) {
        this.products = products;
        this.total = total;
    }

    public static OrdersProductListFragment newInstance(String param1, String param2) {
        OrdersProductListFragment fragment = new OrdersProductListFragment();
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
        Log.d("myTag", "in onCreate() => OrdersProductListFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_orders_product_list, container, false);
        total_amount = view.findViewById(R.id.total_amount);
        total_amount.setText("Total Amount: ₹ " + total);
        recyclerView = view.findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new OrdersProductListAdapter(products, product -> {
            loadFragment(new ProductViewFragment(product));
        });
        recyclerView.setAdapter(adapter);

        Log.d("myTag", "in onCreateView() => OrdersProductListFragment");

        return view;
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("myTag", "in onViewCreated() => OrdersProductListFragment");
    }
}