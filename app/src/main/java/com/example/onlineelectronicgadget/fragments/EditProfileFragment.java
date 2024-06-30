package com.example.onlineelectronicgadget.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.authentication.Auth;
import com.example.onlineelectronicgadget.database.DatabaseHelper;
import com.example.onlineelectronicgadget.models.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseUser;

public class EditProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private User user;
    private TextInputEditText username;
    private TextInputEditText email;
    private TextInputEditText oldPass;
    private TextInputEditText newPass;
    private Button save_button;
    private DatabaseHelper db;
    private Auth auth;

    public EditProfileFragment() {
        // Required empty public constructor
    }

    public EditProfileFragment(User user) {
        this.user = user;
        this.db = new DatabaseHelper();
        this.auth = new Auth(getActivity());
    }

    public static EditProfileFragment newInstance(String param1, String param2) {
        EditProfileFragment fragment = new EditProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        initComponent(view);
        return view;
    }

    private void initComponent(View view) {
        username = view.findViewById(R.id.username);
        email = view.findViewById(R.id.email);
        oldPass = view.findViewById(R.id.old_password);
        newPass = view.findViewById(R.id.new_password);
        save_button = view.findViewById(R.id.save_button);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadUserDetails();
        save_button.setOnClickListener(v -> onSaveButton());
    }

    private void onSaveButton(){
        String newEmail = String.valueOf(email.getText()).trim();
        String oldPassword = String.valueOf(oldPass.getText()).trim();
        String newPassword = String.valueOf(newPass.getText()).trim();
        String name = String.valueOf(username.getText()).trim();
        boolean nameChanged = !name.equals(user.getUsername());
        boolean emailChanged = !newEmail.equals(user.getEmail());
        boolean passChanged = !newPassword.equals(user.getPassword());

        if (emailChanged || passChanged || nameChanged) {
            if (emailChanged) {
                Log.d("myTag", "emailChanged => true");
                auth.reAuthenticateUserAndChangeEmail(user.getEmail(), user.getPassword(), newEmail);
                user.setEmail(newEmail);
                loadUserDetails();
            }
            if (passChanged && !newPassword.isEmpty() && !oldPassword.isEmpty()) {
                Log.d("myTag", "passChanged => true");
                auth.reAuthenticateAndChangedPassword(user.getEmail(), oldPassword, newPassword);
                user.setPassword(newPassword);
                loadUserDetails();
            }
            if (nameChanged) {
                Log.d("myTag", "nameChanged => true");
                db.updateUser(user.getId(), name, "username");
                user.setUsername(name);
                loadUserDetails();
            }
            Toast.makeText(getActivity(), "profile updated successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "no changed detected", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadUserDetails() {
        username.setText(user.getUsername());
        email.setText(user.getEmail());
    }
}