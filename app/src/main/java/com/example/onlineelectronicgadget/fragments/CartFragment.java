package com.example.onlineelectronicgadget.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.adapters.CartListAdapter;
import com.example.onlineelectronicgadget.database.DatabaseHelper;
import com.example.onlineelectronicgadget.models.Order;
import com.example.onlineelectronicgadget.models.Product;
import com.example.onlineelectronicgadget.util.CustomAlertDialog;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private DatabaseHelper db;
    private RecyclerView cartRecycler;
    private RecyclerView.LayoutManager layoutManager;
    private CartListAdapter adapter;
    private List<Product> list;
    private TextView totalAmount;
    private double total;
    private OnCartItemCountChangeListener listener;
    private Button buyButton;
    private ProgressBar progressBar;
    private LinearLayout mainLayout;

    public interface OnCartItemCountChangeListener {
        void onCartItemCountChange(int count);
    }

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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

        if (getActivity() instanceof OnCartItemCountChangeListener) {
            listener = (OnCartItemCountChangeListener) getActivity();
        } else {
            throw new RuntimeException("Activity must implement OnCartItemCountChangeListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        initComponent(view);
        populateList();
        Log.d("myTag", "in cart fragment onCreateView()");
        return view;
    }

    private void initComponent(View view) {
        db = new DatabaseHelper();
        list = new ArrayList<>();
        totalAmount = view.findViewById(R.id.totalAmount);
        cartRecycler = view.findViewById(R.id.cartRecycler);
        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        cartRecycler.setLayoutManager(layoutManager);
        buyButton = view.findViewById(R.id.buyButton);
        progressBar = view.findViewById(R.id.progressBar);
        mainLayout = view.findViewById(R.id.mainLayout);

        buyButton.setOnClickListener(v -> onBuyButton());
    }

    private void onBuyButton() {
        if (list != null && !list.isEmpty()) {
            Order order = new Order();
            order.setProducts(list);
            db.saveOrder(order, flag -> {});
            db.removeFromCart(list, flag -> {});
            CustomAlertDialog.showCustomDialog(getActivity(), "Info", "Your order is placed!");

            loadFragment(new EmptyCartActivity());
            listener.onCartItemCountChange(0);
        }
    }

    private void populateList() {
        mainLayout.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        db.getCart((list1, total1)  -> {
            if (list1.isEmpty()) {
                if (listener != null) {
                    listener.onCartItemCountChange(0);
                }
                loadFragment(new EmptyCartActivity());
                Log.d("myTag", "list is empty");
            } else {
                list.clear();
                list.addAll(list1);
                Log.d("myTag", list.toString());
                adapter.notifyDataSetChanged();
                total = total1;
                totalAmount.setText("₹ " + total);
            }

            if (listener != null) {
                listener.onCartItemCountChange(list.size());
                Log.d("myTag", "after on cart item count change listener");
            }
            progressBar.setVisibility(View.GONE);
            mainLayout.setVisibility(View.VISIBLE);
        });
        adapter = new CartListAdapter(list, product -> {
            loadFragment(new ProductViewFragment(product));
        }, () -> loadFragment(new EmptyCartActivity()), (product) -> {
            List<Product> products = new ArrayList<>();
            products.add(product);
            loadFragment(new PlaceOrderFragment(products));
        }, () -> {
            db.getCart((list1, total1) -> {
                list.clear();
                list.addAll(list1);
                adapter.notifyDataSetChanged();
            });
        }, count -> {listener.onCartItemCountChange(count);});
        cartRecycler.setAdapter(adapter);
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}