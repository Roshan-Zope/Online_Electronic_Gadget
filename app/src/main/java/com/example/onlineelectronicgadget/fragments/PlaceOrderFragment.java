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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.database.DatabaseHelper;
import com.example.onlineelectronicgadget.models.Order;
import com.example.onlineelectronicgadget.models.Product;
import com.example.onlineelectronicgadget.util.CustomAlertDialog;

import java.util.List;

public class PlaceOrderFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private DatabaseHelper db;
    private List<Product> products;
    private ImageView product_image;
    private TextView product_name;
    private TextView product_description;
    private TextView product_price1;
    private Button place_order_button;

    public PlaceOrderFragment() {
        // Required empty public constructor
    }

    public PlaceOrderFragment(List<Product> products) {
        this.products = products;
    }

    public static PlaceOrderFragment newInstance(String param1, String param2) {
        PlaceOrderFragment fragment = new PlaceOrderFragment();
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
        View view = inflater.inflate(R.layout.fragment_place_order, container, false);
        initComponent(view);
        return view;
    }

    private void initComponent(View view) {
        product_image = view.findViewById(R.id.product_image);
        product_description = view.findViewById(R.id.product_description);
        product_name = view.findViewById(R.id.product_name);
        product_price1 = view.findViewById(R.id.product_price1);
        place_order_button = view.findViewById(R.id.place_order_button);
        db = new DatabaseHelper();

        place_order_button.setOnClickListener(v -> onPlaceOrder());
    }

    private void onPlaceOrder() {
        Order order = new Order();
        order.setProducts(products);
        String accType = db.saveOrder(order, flag -> {
            if (!flag) {
                db.removeFromCart(products, flag1 -> {
                    if (flag1) {
                        Log.d("myTag", "product already in cart");
                    }
                });
                CustomAlertDialog.showCustomDialog(getContext(), "Info", "Order is placed");
            }
        });
        if (accType.equals("Customer")) loadFragment(new HomeFragment());
        else if (accType.equals("Retailer")) loadFragment(new AdminHomeScreen());
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

        if (products != null) {
            if (products.get(0).getImagesId() != null && !products.get(0).getImagesId().isEmpty()) {
                Glide
                        .with(getActivity())
                        .load(products.get(0).getImagesId().get(0))
                        .into(product_image);
            } else {
                Glide
                        .with(getActivity())
                        .load(R.drawable.ic_launcher_foreground)
                        .into(product_image);
            }
            product_description.setText(products.get(0).getDescription());
            product_name.setText(products.get(0).getModel());
            product_price1.setText("₹ " + products.get(0).getPrice());
        }
    }
}