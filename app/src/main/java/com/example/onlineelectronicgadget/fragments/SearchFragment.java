package com.example.onlineelectronicgadget.fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.activities.MainActivity;
import com.example.onlineelectronicgadget.adapters.ProductListAdapter;
import com.example.onlineelectronicgadget.adapters.SearchSuggestionsAdapter;
import com.example.onlineelectronicgadget.database.DatabaseHelper;
import com.example.onlineelectronicgadget.models.Laptop;
import com.example.onlineelectronicgadget.models.Product;
import com.example.onlineelectronicgadget.models.SmartTv;
import com.example.onlineelectronicgadget.models.SmartWatches;
import com.example.onlineelectronicgadget.models.Tablets;
import com.example.onlineelectronicgadget.util.QuerySimplifier;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.search.SearchBar;

import org.checkerframework.checker.fenum.qual.SwingBoxOrientation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private DatabaseHelper db;
    private SearchView searchView;
    private RecyclerView search_recyclerView;
    private CircularProgressIndicator progressBar;
    private SearchSuggestionsAdapter adapter;
    private ProductListAdapter productListAdapter;
    private List<String> suggestionList;
    private List<Product> productList;
        
    public SearchFragment() {
        // Required empty public constructor
    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();
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
        Log.d("myTag", "search fragment created");
    }

    private void initComponent(View view) {
        searchView = view.findViewById(R.id.searchView);
        progressBar = view.findViewById(R.id.progressBar);
        search_recyclerView = view.findViewById(R.id.search_recyclerView);
        suggestionList = new ArrayList<>();
        productList = new ArrayList<>();
        db = new DatabaseHelper();
        Log.d("myTag", "search fragment component initialize");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initComponent(view);
        populateList();
        Log.d("myTag", "onCreateView_searchFragment");
        // Inflate the layout for this fragment
        return view;
    }

    private void populateList() {
        suggestionList.add("laptop");
        suggestionList.add("watches");
        suggestionList.add("tv");
        suggestionList.add("tablet");;
        Log.d("myTag", "list populated");
    }

    private void populateProductList(String query) {
        Log.d("myTag", "in populate product list");

        Map<String, Object> parseQuery = QuerySimplifier.parseQuery(query);
        progressBar.setVisibility(View.VISIBLE);

        db.search(parseQuery, (list, total) -> {
            productList.clear();
            productList.addAll(list);
            productListAdapter.notifyDataSetChanged();
            Log.d("myTag", "in search fragment listener");
            progressBar.setVisibility(View.GONE);
        });
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        adapter = new SearchSuggestionsAdapter(suggestionList, suggestion -> {
            Toast.makeText(getActivity(), "You selected: " + suggestion, Toast.LENGTH_SHORT).show();
            search_recyclerView.setVisibility(View.GONE);
            Log.d("myTag", "you selected: " + suggestion);
        });

        search_recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        search_recyclerView.setAdapter(adapter);
        searchView.setIconified(false);
        searchView.requestFocus();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                populateProductList(query);
                search_recyclerView.setAdapter(productListAdapter);
                Log.d("myTag", "in query Listener");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search_recyclerView.setAdapter(adapter);
                filterSuggestions(newText);
                return true;
            }
        });

        searchView.setOnQueryTextFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                showKeyboard(v.findFocus());
            }
        });

        productListAdapter = new ProductListAdapter(productList,
                product -> {
//                    List<Product> products = new ArrayList<>();
//                    products.add(product);
                    loadFragment(new ProductViewFragment(product));
                });

    }

    private void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(view, 0);
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void filterSuggestions(String string) {
        if (string.isEmpty()) {
            search_recyclerView.setVisibility(View.GONE);
            return;
        }

        List<String> filteredList = new ArrayList<>();
        for (String suggestion : suggestionList) {
            if (suggestion.toLowerCase().contains(string.toLowerCase())) {
                filteredList.add(suggestion);
            }
        }
        adapter.updateSuggestions(filteredList);
        search_recyclerView.setVisibility(filteredList.isEmpty() ? View.GONE : View.VISIBLE);
    }
}