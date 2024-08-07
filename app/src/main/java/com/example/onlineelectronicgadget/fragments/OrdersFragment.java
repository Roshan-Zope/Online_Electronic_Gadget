package com.example.onlineelectronicgadget.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.adapters.OrdersListAdapter;
import com.example.onlineelectronicgadget.authentication.Auth;
import com.example.onlineelectronicgadget.database.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrdersFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Map<String, Object>> list;
    private OrdersListAdapter adapter;
    private DatabaseHelper db;
    private ProgressBar progressBar;
    private ImageView empty_cart_image;
    private TextView empty_cart_message;
    private Button browse_products_button;
    private TextView ordersTv;
    private String accType;

    public OrdersFragment() {
        // Required empty public constructor
    }

    public static OrdersFragment newInstance(String param1, String param2) {
        OrdersFragment fragment = new OrdersFragment();
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
        View view = inflater.inflate(R.layout.fragment_orders, container, false);
        initComponent(view);
        loadOrders();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new OrdersListAdapter(list, (products, total) -> {
            loadFragment(new OrdersProductListFragment(products, total));
        });
        recyclerView.setAdapter(adapter);
    }

    private void initComponent(View view) {
        db = new DatabaseHelper();
        recyclerView = view.findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        progressBar = view.findViewById(R.id.progressBar);
        empty_cart_image = view.findViewById(R.id.empty_cart_image);
        empty_cart_message = view.findViewById(R.id.empty_cart_message);
        browse_products_button = view.findViewById(R.id.browse_products_button);
        ordersTv = view.findViewById(R.id.ordersTv);

        browse_products_button.setOnClickListener(v -> {
            accType = Auth.currentUser.getAccType();
            if (accType.equals("Customer")) loadFragment(new HomeFragment());
            else if (accType.equals("Retailer")) loadFragment(new AdminHomeScreen());
        });
    }

    private void loadOrders() {
        progressBar.setVisibility(View.VISIBLE);
        db.getOrders(list1 -> {
            if (!list1.isEmpty() && list1 != null) {
                list.clear();
                list.addAll(list1);
                adapter.notifyDataSetChanged();
            } else {
                ordersTv.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                empty_cart_message.setVisibility(View.VISIBLE);
                empty_cart_image.setVisibility(View.VISIBLE);
                browse_products_button.setVisibility(View.VISIBLE);
            }
            progressBar.setVisibility(View.GONE);
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}