package com.example.onlineelectronicgadget.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.models.Laptop;
import com.example.onlineelectronicgadget.models.Product;
import com.example.onlineelectronicgadget.models.SmartTv;
import com.example.onlineelectronicgadget.models.SmartWatches;
import com.example.onlineelectronicgadget.models.Tablets;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

public class EditProductFFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private ChipGroup chipGroupEditProduct;
    private TextInputEditText product_model;
    private TextInputEditText product_price;
    private TextInputEditText product_description;
    private TextInputEditText product_stocks;
    private Button save_button;
    private Button cancel_button;

    public EditProductFFragment() {
        // Required empty public constructor
    }

    public static EditProductFFragment newInstance(String param1, String param2) {
        EditProductFFragment fragment = new EditProductFFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_product_f, container, false);
        initComponent(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initComponent(View view) {
        chipGroupEditProduct = view.findViewById(R.id.chipGroupEditProduct);
        product_model = view.findViewById(R.id.product_model);
        product_price = view.findViewById(R.id.product_price);
        product_description = view.findViewById(R.id.product_description);
        product_stocks = view.findViewById(R.id.product_stocks);
        save_button = view.findViewById(R.id.save_button);
        cancel_button = view.findViewById(R.id.cancel_button);

        save_button.setOnClickListener(v -> onSaveButton());
        cancel_button.setOnClickListener(v -> onCancelButton());
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void onCancelButton() {
        getParentFragmentManager().popBackStack();
    }

    private void onSaveButton() {
        String category = null;
        try {
            Chip chip = getActivity().findViewById(chipGroupEditProduct.getCheckedChipId());
            category = String.valueOf(chip.getText()).trim().toLowerCase();
        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "Please choice type of product", Toast.LENGTH_SHORT).show();
        }
        if (category == null) {
            Toast.makeText(getContext(), "Please choice type of product", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (category) {
            case "laptop":
                Laptop laptop = new Laptop();
                fillProduct(laptop);
                loadFragment(new EditLaptopFragment(laptop));
                break;
            case "tablet":
                Tablets tablet = new Tablets();
                fillProduct(tablet);
                loadFragment(new EditTabFragment(tablet));
                break;
            case "tv":
                SmartTv smartTv = new SmartTv();
                fillProduct(smartTv);
                loadFragment(new EditTvFragment(smartTv));
                break;
            case "watch":
                SmartWatches smartWatch = new SmartWatches();
                fillProduct(smartWatch);
                loadFragment(new EditWatchFragment(smartWatch));
                break;
            default:
                Toast.makeText(getContext(), "Invalid product category.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void fillProduct(Product product) {
        try {
            product.setModel(String.valueOf(product_model.getText()).trim());
        } catch (NullPointerException e) {
            product.setModel(null);
        }
        try {
            product.setPrice(Long.parseLong(String.valueOf(product_price.getText()).trim()));
        } catch (NullPointerException | NumberFormatException e) {
            product.setPrice(-1);
        }
        try {
            product.setDescription(String.valueOf(product_description.getText()).trim());
        } catch (NullPointerException e) {
            product.setDescription(null);
        }
        try {
            product.setStocks(Integer.parseInt(String.valueOf(product_stocks.getText()).trim()));
        } catch (NullPointerException | NumberFormatException e) {
            product.setStocks(-1);
        }
        try {
            Chip chip = getActivity().findViewById(chipGroupEditProduct.getCheckedChipId());
            product.setCategory(String.valueOf(chip.getText()).trim().toLowerCase());
        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "Please choice type of product", Toast.LENGTH_SHORT).show();
        }
    }
}