package com.example.onlineelectronicgadget.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.activities.MainActivity;
import com.example.onlineelectronicgadget.adapters.SearchSuggestionsAdapter;
import com.google.android.material.search.SearchBar;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private SearchView searchView;
    private RecyclerView search_recyclerView;
    private SearchSuggestionsAdapter adapter;
    private List<String> suggestionList;
        
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
        search_recyclerView = view.findViewById(R.id.search_recyclerView);
        suggestionList = new ArrayList<>();
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
        suggestionList.add("Laptops");
        suggestionList.add("Smart Watches");
        suggestionList.add("Smart Tv's");
        suggestionList.add("Tablets");;
        Log.d("myTag", "list populated");
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

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterSuggestions(newText);
                return true;
            }
        });
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