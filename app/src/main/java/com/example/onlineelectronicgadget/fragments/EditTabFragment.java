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

import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.database.DatabaseHelper;
import com.example.onlineelectronicgadget.models.Tablets;
import com.google.android.material.textfield.TextInputEditText;

public class EditTabFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private Tablets tablets;
    private DatabaseHelper db;
    private TextInputEditText processor;
    private TextInputEditText ram;
    private TextInputEditText storage;
    private TextInputEditText display;
    private TextInputEditText os;
    private TextInputEditText batteryLife;
    private TextInputEditText weight;
    private TextInputEditText color;
    private TextInputEditText ports;
    private TextInputEditText warranty;
    private TextInputEditText camera;
    private TextInputEditText connectivity;
    private Button save_button;
    private Button cancel_button;


    public EditTabFragment() {
        // Required empty public constructor
    }

    public EditTabFragment(Tablets tablets) {
        this.tablets = tablets;
    }

    public static EditTabFragment newInstance(String param1, String param2) {
        EditTabFragment fragment = new EditTabFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_tab, container, false);
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
        ram = view.findViewById(R.id.ram);
        storage = view.findViewById(R.id.storage);
        display = view.findViewById(R.id.display);
        os = view.findViewById(R.id.os);
        batteryLife = view.findViewById(R.id.batteryLife);
        ports = view.findViewById(R.id.ports);
        weight = view.findViewById(R.id.weight);
        color = view.findViewById(R.id.color);
        warranty = view.findViewById(R.id.warranty);
        camera = view.findViewById(R.id.camera);
        connectivity = view.findViewById(R.id.connectivity);
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
        fillTablet(tablets);
        db.updateProduct(tablets);
        loadFragment(new AdminHomeScreen());
    }

    private void fillTablet(Tablets tablets) {
        try {
            tablets.setProcessor(String.valueOf(processor.getText()).trim());
        } catch (NullPointerException e) {
            tablets.setProcessor(null);
        }
        try {
            tablets.setRam(String.valueOf(ram.getText()).trim());
        } catch (NullPointerException e) {
            tablets.setRam(null);
        }
        try {
            tablets.setStorage(String.valueOf(storage.getText()).trim());
        } catch (NullPointerException e) {
            tablets.setStorage(null);
        }
        try {
            tablets.setDisplay(String.valueOf(display.getText()).trim());
        } catch (NullPointerException e) {
            tablets.setDisplay(null);
        }
        try {
            tablets.setOs(String.valueOf(os.getText()).trim());
        } catch (NullPointerException e) {
            tablets.setOs(null);
        }
        try {
            tablets.setBatteryLife(String.valueOf(batteryLife.getText()).trim());
        } catch (NullPointerException e) {
            tablets.setBatteryLife(null);
        }
        try {
            tablets.setPorts(String.valueOf(ports.getText()).trim());
        } catch (NullPointerException e) {
            tablets.setPorts(null);
        }
        try {
            tablets.setWeight(Double.parseDouble(String.valueOf(weight.getText()).trim()));
            Log.d("myTag", tablets.getWeight()+"");
        } catch (NullPointerException | NumberFormatException e) {
            tablets.setWeight(-1);
            Log.d("myTag", tablets.getWeight()+"");
        }
        try {
            tablets.setColor(String.valueOf(color.getText()).trim());
        } catch (NullPointerException e) {
            tablets.setColor(null);
        }
        try {
            tablets.setWarranty(String.valueOf(warranty.getText()).trim());
        } catch (NullPointerException e) {
            tablets.setWarranty(null);
        }
        try {
            tablets.setCamera(String.valueOf(camera.getText()).trim());
        } catch (NullPointerException e) {
            tablets.setCamera(null);
        }
        try {
            tablets.setConnectivity(String.valueOf(connectivity.getText()).trim());
        } catch (NullPointerException e) {
            tablets.setConnectivity(null);
        }
    }
}