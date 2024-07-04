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
import com.example.onlineelectronicgadget.models.Order;
import com.example.onlineelectronicgadget.models.Product;

import java.util.List;

public class OrdersListAdapter extends RecyclerView.Adapter<OrdersListAdapter.ViewHolder> {
    private List<Order> list;
    private OrderCallback listener;

    public OrdersListAdapter(List<Order> list, OrderCallback listener) {
        this.listener = listener;
        this.list = list;
    }

    public interface OrderCallback {
        void onCall(Product product);
    }

    @NonNull
    @Override
    public OrdersListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ordes_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersListAdapter.ViewHolder holder, int position) {
        holder.bind(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView product_name;
        //private TextView product_price;
        private TextView date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            product_name = itemView.findViewById(R.id.product_name);
            //product_price = itemView.findViewById(R.id.product_price);
            date = itemView.findViewById(R.id.date);
        }

        public void bind(Order order) {
            if (order != null) {
                if (order.getProduct().getImagesId() != null && !order.getProduct().getImagesId().isEmpty()) {
                    Glide
                            .with(itemView.getContext())
                            .load(order.getProduct().getImagesId().get(0))
                            .into(imageView);
                } else {
                    Glide
                            .with(itemView.getContext())
                            .load(R.drawable.ic_launcher_foreground)
                            .into(imageView);
                }
                product_name.setText(order.getProduct().getModel());
                //product_price.setText(String.valueOf(order.getProduct().getPrice()));
                date.setText(order.getDate());
                itemView.setOnClickListener(v -> listener.onCall(order.getProduct()));
            }

        }
    }
}
