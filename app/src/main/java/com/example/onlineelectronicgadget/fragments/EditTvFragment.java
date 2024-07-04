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

import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.database.DatabaseHelper;
import com.example.onlineelectronicgadget.models.SmartTv;
import com.google.android.material.textfield.TextInputEditText;

public class EditTvFragment extends Fragment {
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

    public EditTvFragment() {
        // Required empty public constructor
    }

    public EditTvFragment(SmartTv smartTv) {
        this.smartTv = smartTv;
    }

    public static EditTvFragment newInstance(String param1, String param2) {
        EditTvFragment fragment = new EditTvFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_tv, container, false);
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
        db.updateProduct(smartTv);
        loadFragment(new AdminHomeScreen());
    }

    private void fillTv(SmartTv smartTv) {
        try {
            smartTv.setScreenSize(String.valueOf(screenSize.getText()).trim());
        } catch (NullPointerException e) {
            smartTv.setScreenSize(null);
        }
        try {
            smartTv.setResolution(String.valueOf(resolution.getText()).trim());
        } catch (NullPointerException e) {
            smartTv.setResolution(null);
        }
        try {
            smartTv.setDisplayTechnology(String.valueOf(displayTechnology.getText()).trim());
        } catch (NullPointerException e) {
            smartTv.setDisplayTechnology(null);
        }
        try {
            smartTv.setConnectivity(String.valueOf(connectivity.getText()).trim());
        } catch (NullPointerException e) {
            smartTv.setConnectivity(null);
        }
        try {
            smartTv.setOs(String.valueOf(os.getText()).trim());
        } catch (NullPointerException e) {
            smartTv.setOs(null);
        }
        try {
            smartTv.setSmartFeatures(String.valueOf(smartFeatures.getText()).trim());
        } catch (NullPointerException e) {
            smartTv.setSmartFeatures(null);
        }
        try {
            smartTv.setPorts(String.valueOf(ports.getText()).trim());
        } catch (NullPointerException e) {
            smartTv.setPorts(null);
        }
        try {
            smartTv.setWeight(Double.parseDouble(String.valueOf(weight.getText()).trim()));
        } catch (NullPointerException | NumberFormatException e) {
            smartTv.setWeight(-1);
        }
        try {
            smartTv.setColor(String.valueOf(color.getText()).trim());
        } catch (NullPointerException e) {
            smartTv.setColor(null);
        }
        try {
            smartTv.setWarranty(String.valueOf(warranty.getText()).trim());
        } catch (NullPointerException e) {
            smartTv.setWarranty(null);
        }
        try {
            smartTv.setDimension(String.valueOf(dimension.getText()).trim());
        } catch (NullPointerException e) {
            smartTv.setDimension(null);
        }
        try {
            smartTv.setSound(String.valueOf(sound.getText()).trim());
        } catch (NullPointerException e) {
            smartTv.setSound(null);
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