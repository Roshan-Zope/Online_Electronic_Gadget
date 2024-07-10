package com.example.onlineelectronicgadget.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.models.Product;

import java.util.List;

public class ProductListAdapter extends RecyclerView.Adapter<ProductListAdapter.ViewHolder> {
    private List<Product> productList;
    private OnProductClickListener listener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public ProductListAdapter(List<Product> productList, OnProductClickListener listener) {
        this.productList = productList;
        this.listener = listener;
        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public ProductListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_list_view, parent, false);
        Log.d("myTag", "in onCreateViewHolder() => productListAdapter");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListAdapter.ViewHolder holder, int position) {

        try {
            Log.d("myTag","OnBind");
            holder.productImage.setImageResource(R.drawable.ic_launcher_foreground);
            holder.bind(productList.get(position), listener);
        } catch (Exception e) {
            Log.d("myTag",e.getMessage(),e);
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount() {
        Log.d("myTag","====>"+productList.size());


        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productName, productPrice, productDescription;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
            productDescription = itemView.findViewById(R.id.productDescription);
        }

        public void bind(Product product, OnProductClickListener listener) {

            Log.d("myTag",product.getBrand());
            productName.setText(product.getBrand());
            productPrice.setText("₹ " + String.valueOf(product.getPrice()));
            productDescription.setText(product.getDescription());
            if (product.getImagesId() != null && !product.getImagesId().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(product.getImagesId().get(0))
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(productImage);
            } else {
                Glide.with(itemView.getContext())
                        .load(R.drawable.ic_launcher_foreground)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(productImage);
            }
            itemView.setOnClickListener(v -> listener.onProductClick(product));
        }
    }
}
