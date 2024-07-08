package com.example.onlineelectronicgadget.fragments;

import android.content.Intent;
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
import android.widget.TextView;
import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.activities.LoginActivity;
import com.example.onlineelectronicgadget.activities.RegisterActivity;
import com.example.onlineelectronicgadget.authentication.Auth;
import com.example.onlineelectronicgadget.database.DatabaseHelper;
import com.example.onlineelectronicgadget.models.User;
import com.example.onlineelectronicgadget.util.CustomAlertDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private TextView user_name;
    private TextView user_email;
    private Button edit_profile_button;
    private TextView orders;
    private TextView aboutUs;
    private TextView deleteAcc;
    private TextView logOut;
    private User user;
    private DatabaseHelper db;
    private Auth auth;

    public AccountFragment() {
        // Required empty public constructor
    }
    public static AccountFragment newInstance(String param1, String param2) {
        AccountFragment fragment = new AccountFragment();
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
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        initComponent(view);
        loadProfile();
        return view;
    }

    private void initComponent(View view) {
        user_name = view.findViewById(R.id.user_name);
        user_email = view.findViewById(R.id.user_email);
        edit_profile_button = view.findViewById(R.id.edit_profile_button);
        orders = view.findViewById(R.id.orders);
        aboutUs = view.findViewById(R.id.aboutUs);
        deleteAcc = view.findViewById(R.id.deleteAcc);
        logOut = view.findViewById(R.id.logOut);
        db = new DatabaseHelper();
        auth = new Auth(getActivity());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edit_profile_button.setOnClickListener(v -> {
            loadFragment(new EditProfileFragment(user));
        });

        logOut.setOnClickListener(v -> {
            CustomAlertDialog.showDialog(getContext(), "Alert!", "Do you want to log out?", flag -> {
                if (flag) {
                    FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                    firebaseAuth.signOut();
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().finish();
                    startActivity(intent);
                }
            });
        });

        deleteAcc.setOnClickListener(v -> {
            CustomAlertDialog.showDialog(getContext(), "Alert!", "Do you want delete your account? This will erase your all data.", flag -> {
                if (flag) {
                    db.deleteOrders();
                    db.deleteCart();
                    auth.deleteAcc(user.getEmail(), user.getPassword());
                    db.deleteAcc(user);
                    Intent intent = new Intent(getActivity(), RegisterActivity.class);
                    getActivity().finish();
                    startActivity(intent);
                }
            });
        });

        orders.setOnClickListener(v -> {
            loadFragment(new OrdersFragment());
        });
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }


    private void loadProfile() {
        user = auth.getCurrentUser();

        if (user != null) {
            user_name.setText(user.getUsername());
            user_email.setText(user.getEmail());
        } else {
            Log.d("myTag", "User is null in loadProfile");
        }

    }
}