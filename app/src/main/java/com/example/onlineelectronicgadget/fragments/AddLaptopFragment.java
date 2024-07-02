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
import android.widget.Toast;

import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.database.DatabaseHelper;
import com.example.onlineelectronicgadget.models.Laptop;
import com.example.onlineelectronicgadget.models.Product;
import com.google.android.material.textfield.TextInputEditText;

public class AddLaptopFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private DatabaseHelper db;
    private Laptop laptop;
    private TextInputEditText processor;
    private TextInputEditText ram;
    private TextInputEditText storage;
    private TextInputEditText display;
    private TextInputEditText os;
    private TextInputEditText batteryLife;
    private TextInputEditText ports;
    private TextInputEditText weight;
    private TextInputEditText dimension;
    private TextInputEditText color;
    private TextInputEditText warranty;
    private Button save_button;
    private Button cancel_button;

    public AddLaptopFragment() {
        // Required empty public constructor
    }

    public AddLaptopFragment(Laptop laptop) {
        this.laptop = laptop;
        Log.d("myTag", laptop.toString());
    }

    public static AddLaptopFragment newInstance(String param1, String param2) {
        AddLaptopFragment fragment = new AddLaptopFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_laptop, container, false);
        initComponent(view);
        return view;
    }

    private void initComponent(View view) {
        db = new DatabaseHelper();
        processor = view.findViewById(R.id.processor);
        ram = view.findViewById(R.id.ram);
        storage = view.findViewById(R.id.storage);
        display = view.findViewById(R.id.display);
        os = view.findViewById(R.id.os);
        batteryLife = view.findViewById(R.id.batteryLife);
        ports = view.findViewById(R.id.ports);
        weight = view.findViewById(R.id.weight);
        color = view.findViewById(R.id.color);
        dimension = view.findViewById(R.id.dimension);
        warranty = view.findViewById(R.id.warranty);
        save_button = view.findViewById(R.id.save_button);
        cancel_button = view.findViewById(R.id.cancel_button);

        save_button.setOnClickListener(v -> onSaveButton());
        cancel_button.setOnClickListener(v -> onCancelButton());
    }

    private void onCancelButton() {
        getParentFragmentManager().popBackStack();
    }

    private void onSaveButton() {
        fillLaptop(laptop);
        String id = db.saveProduct(laptop);
        Toast.makeText(getActivity(), "product id: " + id, Toast.LENGTH_LONG).show();
        loadFragment(new AdminHomeScreen());
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void fillLaptop(Laptop laptop) {
        try {
            laptop.setProcessor(String.valueOf(processor.getText()).trim());
        } catch (NullPointerException e) {
            laptop.setProcessor(null);
        }
        try {
            laptop.setRam(String.valueOf(ram.getText()).trim());
        } catch (NullPointerException e) {
            laptop.setRam(null);
        }
        try {
            laptop.setStorage(String.valueOf(storage.getText()).trim());
        } catch (NullPointerException e) {
            laptop.setStorage(null);
        }
        try {
            laptop.setDisplay(String.valueOf(display.getText()).trim());
        } catch (NullPointerException e) {
            laptop.setDisplay(null);
        }
        try {
            laptop.setOs(String.valueOf(os.getText()).trim());
        } catch (NullPointerException e) {
            laptop.setOs(null);
        }
        try {
            laptop.setBatteryLife(String.valueOf(batteryLife.getText()).trim());
        } catch (NullPointerException e) {
            laptop.setBatteryLife(null);
        }
        try {
            laptop.setPorts(String.valueOf(ports.getText()).trim());
        } catch (NullPointerException e) {
            laptop.setPorts(null);
        }
        try {
            laptop.setWeight(Double.parseDouble(String.valueOf(weight.getText()).trim()));
        } catch (NullPointerException | NumberFormatException e) {
            laptop.setWeight(0);
        }
        try {
            laptop.setColor(String.valueOf(color.getText()).trim());
        } catch (NullPointerException e) {
            laptop.setColor(null);
        }
        try {
            laptop.setWarranty(String.valueOf(warranty.getText()).trim());
        } catch (NullPointerException e) {
            laptop.setWarranty(null);
        }
        try {
            laptop.setDimension(String.valueOf(dimension.getText()).trim());
        } catch (NullPointerException e) {
            laptop.setDimension(null);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}