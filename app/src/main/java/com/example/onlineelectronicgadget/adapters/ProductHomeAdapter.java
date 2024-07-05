package com.example.onlineelectronicgadget.adapters;

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

public class ProductHomeAdapter extends RecyclerView.Adapter<ProductHomeAdapter.ViewHolder> {
    private List<Product> list;
    private ProductListAdapter.OnProductClickListener listener;

    public ProductHomeAdapter(List<Product> list, ProductListAdapter.OnProductClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHomeAdapter.ViewHolder holder, int position) {
        holder.productImage.setImageResource(R.drawable.ic_launcher_foreground);
        holder.bind(list.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView productImage;
        private TextView productName, productPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productName = itemView.findViewById(R.id.productName);
            productPrice = itemView.findViewById(R.id.productPrice);
        }

        public void bind(Product product, ProductListAdapter.OnProductClickListener listener) {
            productName.setText(product.getModel());
            productPrice.setText("₹ " + product.getPrice());
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
