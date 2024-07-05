package com.example.onlineelectronicgadget.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineelectronicgadget.R;

import java.util.List;

public class SpecListAdapter extends RecyclerView.Adapter<SpecListAdapter.ViewHolder> {
    private List<String> list;

    public SpecListAdapter(List<String> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public SpecListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.specs_view_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecListAdapter.ViewHolder holder, int position) {
        holder.spec.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView spec;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            spec = itemView.findViewById(R.id.spec);
        }
    }
}
