package com.example.onlineelectronicgadget.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.database.DatabaseHelper;
import com.example.onlineelectronicgadget.models.Product;

import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {
    private List<Product> list;
    private DatabaseHelper db;
    private ProductListAdapter.OnProductClickListener listener;

    public CartListAdapter(List<Product> list, ProductListAdapter.OnProductClickListener listener) {
        this.listener = listener;
        this.list = list;
        this.db = new DatabaseHelper();
    }

    @NonNull
    @Override
    public CartListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartListAdapter.ViewHolder holder, int position) {
        holder.bind(list.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView product_name;
        private TextView product_color;
        private TextView price;
        private Button removeButton;
        private Button buyButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            product_name = itemView.findViewById(R.id.product_name);
            product_color = itemView.findViewById(R.id.product_color);
            price = itemView.findViewById(R.id.price);
            removeButton = itemView.findViewById(R.id.removeButton);
            buyButton = itemView.findViewById(R.id.buyButton);
        }

        public void bind(Product product, ProductListAdapter.OnProductClickListener listener) {
            product_name.setText(product.getBrand() + product.getModel());
            //product_color.setText(product.getStocks());
            price.setText(String.valueOf(product.getPrice()));
            if (product.getImagesId() != null && !product.getImagesId().isEmpty()) {
                Glide.with(itemView.getContext())
                        .load(product.getImagesId().get(0))
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(imageView);
            } else {
                Glide.with(itemView.getContext())
                        .load(R.drawable.ic_launcher_foreground)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(imageView);
            }

            removeButton.setOnClickListener(v -> {
                db.removeFromCart(product.getId(), flag -> {
                    if (flag) {
                        list.remove(product);
                        notifyDataSetChanged();
                    } else {
                        Log.d("myTag", "product not found");
                    }
                });
            });

            imageView.setOnClickListener(v -> {
                listener.onProductClick(product);
            });
        }
    }
}