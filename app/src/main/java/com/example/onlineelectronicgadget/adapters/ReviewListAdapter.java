package com.example.onlineelectronicgadget.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineelectronicgadget.R;

import org.w3c.dom.Text;

import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter<ReviewListAdapter.ViewHolder> {
    private List<String> list;

    public ReviewListAdapter(List<String> list) {

        this.list = list;
    }

    @NonNull
    @Override
    public ReviewListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewListAdapter.ViewHolder holder, int position) {
        holder.tv_review.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_review;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_review = itemView.findViewById(R.id.tv_review);
        }
    }
}