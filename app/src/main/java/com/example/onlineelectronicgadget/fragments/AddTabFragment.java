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
import com.example.onlineelectronicgadget.models.Tablets;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Collections;
import java.util.List;

public class AddTabFragment extends Fragment {
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
    private List<String> keywords;

    public AddTabFragment() {
        // Required empty public constructor
    }

    public AddTabFragment(Tablets tablets, List<String> keywords) {
        this.tablets = tablets;
        this.keywords = keywords;
        Collections.addAll(keywords, "processor", "ram", "storage", "display", "operating system", "battery life", "weight", "dimension", "color", "ports", "warranty", "camera", "connectivity");
        Log.d("myTag", tablets.toString());
    }

    public static AddTabFragment newInstance(String param1, String param2) {
        AddTabFragment fragment = new AddTabFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_tab, container, false);
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
        String id = db.saveProduct(tablets);
        Toast.makeText(getActivity(), "product id: " + id, Toast.LENGTH_LONG).show();
        loadFragment(new AdminHomeScreen());
    }

    private void fillTablet(Tablets tablets) {
        try {
            tablets.setProcessor(String.valueOf(processor.getText()).trim());
        } catch (NullPointerException e) {
            tablets.setProcessor(null);
        }
        keywords.add(tablets.getProcessor());
        try {
            tablets.setRam(String.valueOf(ram.getText()).trim());
        } catch (NullPointerException e) {
            tablets.setRam(null);
        }
        keywords.add(tablets.getRam());
        try {
            tablets.setStorage(String.valueOf(storage.getText()).trim());
        } catch (NullPointerException e) {
            tablets.setStorage(null);
        }
        keywords.add(tablets.getStorage());
        try {
            tablets.setDisplay(String.valueOf(display.getText()).trim());
        } catch (NullPointerException e) {
            tablets.setDisplay(null);
        }
        keywords.add(tablets.getDisplay());
        try {
            tablets.setOs(String.valueOf(os.getText()).trim());
        } catch (NullPointerException e) {
            tablets.setOs(null);
        }
        keywords.add(tablets.getOs());
        try {
            tablets.setBatteryLife(String.valueOf(batteryLife.getText()).trim());
        } catch (NullPointerException e) {
            tablets.setBatteryLife(null);
        }
        keywords.add(tablets.getBatteryLife());
        try {
            tablets.setPorts(String.valueOf(ports.getText()).trim());
        } catch (NullPointerException e) {
            tablets.setPorts(null);
        }
        keywords.add(tablets.getPorts());
        try {
            tablets.setWeight(Double.parseDouble(String.valueOf(weight.getText()).trim()));
        } catch (NullPointerException | NumberFormatException e) {
            tablets.setWeight(-1);
        }
        keywords.add(tablets.getWeight() + "");
        try {
            tablets.setColor(String.valueOf(color.getText()).trim());
        } catch (NullPointerException e) {
            tablets.setColor(null);
        }
        keywords.add(tablets.getColor());
        try {
            tablets.setWarranty(String.valueOf(warranty.getText()).trim());
        } catch (NullPointerException e) {
            tablets.setWarranty(null);
        }
        keywords.add(tablets.getWarranty());
        try {
            tablets.setCamera(String.valueOf(camera.getText()).trim());
        } catch (NullPointerException e) {
            tablets.setCamera(null);
        }
        keywords.add(tablets.getWarranty());
        try {
            tablets.setConnectivity(String.valueOf(connectivity.getText()).trim());
        } catch (NullPointerException e) {
            tablets.setConnectivity(null);
        }
        keywords.add(tablets.getConnectivity());
        tablets.setKeywords(keywords);
    }
}