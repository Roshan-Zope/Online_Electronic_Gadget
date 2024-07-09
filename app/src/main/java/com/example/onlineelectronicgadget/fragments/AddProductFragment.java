package com.example.onlineelectronicgadget.fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
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
import android.widget.ImageView;
import android.widget.Toast;

import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.models.Laptop;
import com.example.onlineelectronicgadget.models.Product;
import com.example.onlineelectronicgadget.models.SmartTv;
import com.example.onlineelectronicgadget.models.SmartWatches;
import com.example.onlineelectronicgadget.models.Tablets;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class AddProductFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private ImageView product_image;
    private TextInputEditText product_brand;
    private TextInputEditText product_model;
    private TextInputEditText product_price;
    private TextInputEditText product_description;
    private TextInputEditText product_stocks;
    private ChipGroup chipGroupAddProduct;
    private List<String> imagesUri;
    private Button save_button;
    private Button cancel_button;

    public AddProductFragment() {
        // Required empty public constructor
    }

    public static AddProductFragment newInstance(String param1, String param2) {
        AddProductFragment fragment = new AddProductFragment();
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
        View view = inflater.inflate(R.layout.fragment_add_product, container, false);
        initComponent(view);
        return view;
    }

    private void uploadImageToFirebase(Uri imageUri) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        StorageReference imageRef = storageReference.child("images/" + System.currentTimeMillis() + ".jpg");

        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String downloadUrl = uri.toString();
                    Log.d("myTag", "Image uploaded: " + downloadUrl);
                    storeImageUrlInFirestore(downloadUrl);
                }))
                .addOnFailureListener(exception -> {
                    Log.e("myTag", "Image upload failed", exception);
                });
    }

    private void storeImageUrlInFirestore(String downloadUrl) {
        imagesUri.add(downloadUrl);
    }

    private void initComponent(View view) {
        product_image = view.findViewById(R.id.product_image);
        product_brand = view.findViewById(R.id.product_brand);
        product_model = view.findViewById(R.id.product_model);
        product_price = view.findViewById(R.id.product_price);
        product_description = view.findViewById(R.id.product_description);
        product_stocks = view.findViewById(R.id.product_stocks);
        save_button = view.findViewById(R.id.save_button);
        cancel_button = view.findViewById(R.id.cancel_button);
        chipGroupAddProduct = view.findViewById(R.id.chipGroupAddProduct);
        imagesUri = new ArrayList<>();

        ActivityResultLauncher<PickVisualMediaRequest> media = registerForActivityResult(new ActivityResultContracts.PickMultipleVisualMedia(3),
                result -> {
                    if (result != null && !result.isEmpty()) {
                        for (Uri uri : result) {
                            Log.d("myTag", uri.toString());
                            uploadImageToFirebase(uri);
                        }
                    } else {
                        Log.d("myTag", "No media selected");
                    }
                });

        save_button.setOnClickListener(v -> onSaveButton());
        cancel_button.setOnClickListener(v -> onCancelButton());
        product_image.setOnClickListener(v -> addProductImages(media));
    }

    private void addProductImages(ActivityResultLauncher<PickVisualMediaRequest> media) {
        media.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE).build());
    }

    private void onCancelButton() {
        getParentFragmentManager().popBackStack();
    }

    private void onSaveButton() {
        String category = null;
        try {
            Chip chip = getActivity().findViewById(chipGroupAddProduct.getCheckedChipId());
            category = String.valueOf(chip.getText()).trim().toLowerCase();
        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "Please choice type of product", Toast.LENGTH_SHORT).show();
        }
        if (category == null) {
            Toast.makeText(getContext(), "Please choice type of product", Toast.LENGTH_SHORT).show();
            return;
        }

        switch (category) {
            case "laptop":
                Laptop laptop = new Laptop();
                fillProduct(laptop);
                loadFragment(new AddLaptopFragment(laptop));
                break;
            case "tablet":
                Tablets tablet = new Tablets();
                fillProduct(tablet);
                loadFragment(new AddTabFragment(tablet));
                break;
            case "tv":
                SmartTv smartTv = new SmartTv();
                fillProduct(smartTv);
                loadFragment(new AddTvFragment(smartTv));
                break;
            case "watch":
                SmartWatches smartWatch = new SmartWatches();
                fillProduct(smartWatch);
                loadFragment(new AddWatchFragment(smartWatch));
                break;
            default:
                Toast.makeText(getContext(), "Invalid product category.", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragment_layout, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void fillProduct(Product product) {
        try {
            product.setBrand(String.valueOf(product_brand.getText()).trim());
        } catch (NullPointerException e) {
            product.setBrand(null);
        }
        try {
            product.setModel(String.valueOf(product_model.getText()).trim());
        } catch (NullPointerException e) {
            product.setModel(null);
        }
        try {
            product.setPrice(Long.parseLong(String.valueOf(product_price.getText()).trim()));
        } catch (NullPointerException | NumberFormatException e) {
            product.setPrice(0);
        }
        try {
            product.setDescription(String.valueOf(product_description.getText()).trim());
        } catch (NullPointerException e) {
            product.setDescription(null);
        }
        try {
            product.setStocks(Integer.parseInt(String.valueOf(product_stocks.getText()).trim()));
        } catch (NullPointerException | NumberFormatException e) {
            product.setStocks(0);
        }
        try {
            Chip chip = getActivity().findViewById(chipGroupAddProduct.getCheckedChipId());
            product.setCategory(String.valueOf(chip.getText()).trim().toLowerCase());
        } catch (NullPointerException e) {
            Toast.makeText(getContext(), "Please choice type of product", Toast.LENGTH_SHORT).show();
        }
        try {
            product.setImagesId(imagesUri);
        } catch (NullPointerException e) {
            product.setImagesId(new ArrayList<>());
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}