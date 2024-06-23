package com.example.onlineelectronicgadget.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.models.Product;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ProductViewFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private ImageView productImage;
    private FloatingActionButton prevButton;
    private FloatingActionButton nextButton;
    private TextView productName;
    private TextView productPrice;
    private TextView productDescription;
    private Button addToCartButton;
    private Button buyNowButton;
    private Product product;

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
        // Inflate the layout for this fragment
        return view;
    }

    private void initComponent(View view) {
        productImage = view.findViewById(R.id.productImage);
        prevButton = view.findViewById(R.id.prevButton);
        nextButton = view.findViewById(R.id.nextButton);
        productName = view.findViewById(R.id.productName);
        productPrice = view.findViewById(R.id.productPrice);
        productDescription = view.findViewById(R.id.productDescription);
        addToCartButton = view.findViewById(R.id.addToCartButton);
        buyNowButton = view.findViewById(R.id.buyNowButton);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productName.setText(product.getBrand());
        productPrice.setText(String.valueOf(product.getPrice()));
        productDescription.setText(product.getDescription());
    }
}