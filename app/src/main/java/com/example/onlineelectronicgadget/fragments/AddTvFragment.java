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
import com.example.onlineelectronicgadget.models.SmartTv;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Collections;
import java.util.List;


public class AddTvFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private SmartTv smartTv;
    private DatabaseHelper db;
    private TextInputEditText screenSize;
    private TextInputEditText resolution;
    private TextInputEditText displayTechnology;
    private TextInputEditText os;
    private TextInputEditText ports;
    private TextInputEditText connectivity;
    private TextInputEditText smartFeatures;
    private TextInputEditText sound;
    private TextInputEditText weight;
    private TextInputEditText dimension;
    private TextInputEditText color;
    private TextInputEditText warranty;
    private Button save_button;
    private Button cancel_button;
    private List<String> keywords;

    public AddTvFragment() {
        // Required empty public constructor
    }

    public AddTvFragment(SmartTv smartTv, List<String> keywords) {
        this.smartTv = smartTv;
        this.keywords = keywords;
        Collections.addAll(keywords, "screen size", "resolution", "display technology", "operating system", "ports", "connectivity", "smart features", "sound", "weight", "dimension", "color", "warranty");
        Log.d("myTag", smartTv.toString());
    }

    public static AddTvFragment newInstance(String param1, String param2) {
        AddTvFragment fragment = new AddTvFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_tv, container, false);
        initComponent(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void initComponent(View view) {
        db = new DatabaseHelper();
        screenSize = view.findViewById(R.id.screenSize);
        resolution = view.findViewById(R.id.resolution);
        displayTechnology = view.findViewById(R.id.display_technology);
        connectivity = view.findViewById(R.id.connectivity);
        os = view.findViewById(R.id.os);
        smartFeatures = view.findViewById(R.id.smartFeatures);
        ports = view.findViewById(R.id.ports);
        weight = view.findViewById(R.id.weight);
        color = view.findViewById(R.id.color);
        warranty = view.findViewById(R.id.warranty);
        sound = view.findViewById(R.id.sound);
        dimension = view.findViewById(R.id.dimension);
        save_button = view.findViewById(R.id.save_button);
        cancel_button = view.findViewById(R.id.cancel_button);

        save_button.setOnClickListener(v -> onSaveButton());
        cancel_button.setOnClickListener(v -> onCancelButton());
    }

    private void onCancelButton() {
        getParentFragmentManager().popBackStack();
    }

    private void onSaveButton() {
        fillTv(smartTv);
        String id = db.saveProduct(smartTv);
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

    private void fillTv(SmartTv smartTv) {
        try {
            smartTv.setScreenSize(String.valueOf(screenSize.getText()).trim());
        } catch (NullPointerException e) {
            smartTv.setScreenSize(null);
        }
        keywords.add(smartTv.getScreenSize());
        try {
            smartTv.setResolution(String.valueOf(resolution.getText()).trim());
        } catch (NullPointerException e) {
            smartTv.setResolution(null);
        }
        keywords.add(smartTv.getResolution());
        try {
            smartTv.setDisplayTechnology(String.valueOf(displayTechnology.getText()).trim());
        } catch (NullPointerException e) {
            smartTv.setDisplayTechnology(null);
        }
        keywords.add(smartTv.getDisplayTechnology());
        try {
            smartTv.setConnectivity(String.valueOf(connectivity.getText()).trim());
        } catch (NullPointerException e) {
            smartTv.setConnectivity(null);
        }
        keywords.add(smartTv.getConnectivity());
        try {
            smartTv.setOs(String.valueOf(os.getText()).trim());
        } catch (NullPointerException e) {
            smartTv.setOs(null);
        }
        keywords.add(smartTv.getOs());
        try {
            smartTv.setSmartFeatures(String.valueOf(smartFeatures.getText()).trim());
        } catch (NullPointerException e) {
            smartTv.setSmartFeatures(null);
        }
        keywords.add(smartTv.getSmartFeatures());
        try {
            smartTv.setPorts(String.valueOf(ports.getText()).trim());
        } catch (NullPointerException e) {
            smartTv.setPorts(null);
        }
        keywords.add(smartTv.getPorts());
        try {
            smartTv.setWeight(Double.parseDouble(String.valueOf(weight.getText()).trim()));
        } catch (NullPointerException | NumberFormatException e) {
            smartTv.setWeight(0);
        }
        keywords.add(smartTv.getWeight() + "");
        try {
            smartTv.setColor(String.valueOf(color.getText()).trim());
        } catch (NullPointerException e) {
            smartTv.setColor(null);
        }
        keywords.add(smartTv.getColor());
        try {
            smartTv.setWarranty(String.valueOf(warranty.getText()).trim());
        } catch (NullPointerException e) {
            smartTv.setWarranty(null);
        }
        keywords.add(smartTv.getWarranty());
        try {
            smartTv.setDimension(String.valueOf(dimension.getText()).trim());
        } catch (NullPointerException e) {
            smartTv.setDimension(null);
        }
        keywords.add(smartTv.getDimension());
        try {
            smartTv.setSound(String.valueOf(sound.getText()).trim());
        } catch (NullPointerException e) {
            smartTv.setSound(null);
        }
        keywords.add(smartTv.getSound());
        smartTv.setKeywords(keywords);
    }
}