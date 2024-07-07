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

public class OrdersProductListAdapter extends RecyclerView.Adapter<OrdersProductListAdapter.ViewHolder> {
    private List<Product> products;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onClick(Product product);
    }

    public OrdersProductListAdapter(List<Product> products, OnItemClickListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public OrdersProductListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_product_list_view, parent, false);
        Log.d("myTag", "in onCreateViewHolder() => OrdersProductListAdapter");
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersProductListAdapter.ViewHolder holder, int position) {
        holder.bind(products.get(position));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView productName;
        private TextView price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            productName = itemView.findViewById(R.id.productName);
            price = itemView.findViewById(R.id.price);
        }

        public void bind(Product product) {
            if (product != null) {
                if (product.getImagesId() != null && !product.getImagesId().isEmpty()) {
                    Glide
                        .with(itemView)
                        .load(product.getImagesId().get(0))
                        .into(imageView);
                } else {
                    Glide
                        .with(itemView)
                        .load(R.drawable.ic_launcher_foreground)
                        .into(imageView);
                }
                productName.setText(product.getModel());
                price.setText("₹ " + product.getPrice());
                Log.d("myTag", "in bind() => OrdersProductListAdapter");

                itemView.setOnClickListener(v -> listener.onClick(product));
            }
        }
    }
}
