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
import com.example.onlineelectronicgadget.models.Product;
import com.example.onlineelectronicgadget.models.SmartWatches;
import com.google.android.material.textfield.TextInputEditText;

public class AddWatchFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private SmartWatches smartWatches;
    private DatabaseHelper db;
    private TextInputEditText processor;
    private TextInputEditText display;
    private TextInputEditText batteryLife;
    private TextInputEditText connectivity;
    private TextInputEditText sensor;
    private TextInputEditText waterResistance;
    private TextInputEditText weight;
    private TextInputEditText color;
    private TextInputEditText warranty;
    private Button save_button;
    private Button cancel_button;

    public AddWatchFragment() {
        // Required empty public constructor
    }

    public AddWatchFragment(SmartWatches smartWatches) {
        this.smartWatches = smartWatches;
        Log.d("myTag", smartWatches.toString());
    }

    public static AddWatchFragment newInstance(String param1, String param2) {
        AddWatchFragment fragment = new AddWatchFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_watch, container, false);
        initComponent(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initComponent(View view) {
        db = new DatabaseHelper();
        processor = view.findViewById(R.id.processor);
        connectivity = view.findViewById(R.id.connectivity);
        sensor = view.findViewById(R.id.sensor);
        display = view.findViewById(R.id.display);
        waterResistance = view.findViewById(R.id.waterResistance);
        batteryLife = view.findViewById(R.id.batteryLife);
        weight = view.findViewById(R.id.weight);
        color = view.findViewById(R.id.color);
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
        fillWatch(smartWatches);
        String id = db.saveProduct(smartWatches);
        Toast.makeText(getActivity(), "product id: " + id, Toast.LENGTH_LONG).show();
        loadFragment(new AdminHomeScreen());
    }

    private void fillWatch(SmartWatches smartWatches) {
        try {
            smartWatches.setProcessor(String.valueOf(processor.getText()).trim());
        } catch (NullPointerException e) {
            smartWatches.setProcessor(null);
        }
        try {
            smartWatches.setConnectivity(String.valueOf(connectivity.getText()).trim());
        } catch (NullPointerException e) {
            smartWatches.setConnectivity(null);
        }
        try {
            smartWatches.setSensor(String.valueOf(sensor.getText()).trim());
        } catch (NullPointerException e) {
            smartWatches.setSensor(null);
        }
        try {
            smartWatches.setDisplay(String.valueOf(display.getText()).trim());
        } catch (NullPointerException e) {
            smartWatches.setDisplay(null);
        }
        try {
            smartWatches.setWaterResistance(String.valueOf(waterResistance.getText()).trim());
        } catch (NullPointerException e) {
            smartWatches.setWaterResistance(null);
        }
        try {
            smartWatches.setBatteryLife(String.valueOf(batteryLife.getText()).trim());
        } catch (NullPointerException e) {
            smartWatches.setBatteryLife(null);
        }
        try {
            smartWatches.setWeight(Double.parseDouble(String.valueOf(weight.getText()).trim()));
        } catch (NullPointerException | NumberFormatException e) {
            smartWatches.setWeight(0);
        }
        try {
            smartWatches.setColor(String.valueOf(color.getText()).trim());
        } catch (NullPointerException e) {
            smartWatches.setColor(null);
        }
        try {
            smartWatches.setWarranty(String.valueOf(warranty.getText()).trim());
        } catch (NullPointerException e) {
            smartWatches.setWarranty(null);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}