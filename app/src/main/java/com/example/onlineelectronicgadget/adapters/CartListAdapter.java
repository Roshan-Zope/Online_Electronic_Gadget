package com.example.onlineelectronicgadget.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.onlineelectronicgadget.R;
import com.example.onlineelectronicgadget.database.DatabaseHelper;
import com.example.onlineelectronicgadget.fragments.CartFragment;
import com.example.onlineelectronicgadget.models.Product;
import java.util.List;

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.ViewHolder> {
    private List<Product> list;
    private DatabaseHelper db;
    private ProductListAdapter.OnProductClickListener listener;
    private OnCartEmptyListener cartEmptyListener;
    private OnPlaceOrderClickListener clickListener;
    private OnItemRemovedListener itemRemovedListener;
    private CartFragment.OnCartItemCountChangeListener countChangeListener;

    public interface OnPlaceOrderClickListener {
        void onClick(Product product);
    }

    public interface OnCartEmptyListener {
        void onCartEmpty();
    }

    public interface OnItemRemovedListener {
        void onItemRemoved();
    }

    public CartListAdapter(List<Product> list, ProductListAdapter.OnProductClickListener listener, OnCartEmptyListener cartEmptyListener, OnPlaceOrderClickListener clickListener, OnItemRemovedListener itemRemovedListener, CartFragment.OnCartItemCountChangeListener countChangeListener) {
        this.cartEmptyListener = cartEmptyListener;
        this.clickListener = clickListener;
        this.listener = listener;
        this.list = list;
        this.countChangeListener = countChangeListener;
        this.itemRemovedListener = itemRemovedListener;
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
        private ImageView imageView, removeButton;
        private TextView product_name, price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            product_name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.price);
            removeButton = itemView.findViewById(R.id.removeButton);
        }

        public void bind(Product product, ProductListAdapter.OnProductClickListener listener) {
            product_name.setText(product.getModel());
            price.setText("₹ " + product.getPrice());
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

            removeButton.setOnClickListener(v -> onRemoveButton(product));
            itemView.setOnClickListener(v -> {
                listener.onProductClick(product);
            });
        }

        private void onRemoveButton(Product product) {
            db.removeFromCart(list, flag -> {
                if (flag) {
                    list.remove(product);
                    notifyDataSetChanged();
                    if (list.isEmpty()) {
                        cartEmptyListener.onCartEmpty();
                    }
                    if (itemRemovedListener != null) {
                        itemRemovedListener.onItemRemoved();
                    }
                    if (countChangeListener != null) {
                        countChangeListener.onCartItemCountChange(list.size());
                    }
                } else {
                    Log.d("myTag", "product not found");
                }
            });
        }
    }
}
