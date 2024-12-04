package com.example.inventry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventry.Classes.Supplier;
import com.example.inventry.R;
import com.example.inventry.Databases.CategoryDatabaseHelper;

import java.util.List;

public class SuppliersAdapter extends RecyclerView.Adapter<SuppliersAdapter.ViewHolder> {
    private final List<Supplier> suppliers;

    public SuppliersAdapter(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.party_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Supplier supplier = suppliers.get(position);
        holder.contactName.setText(supplier.getName());
        holder.contactPhone.setText(supplier.getContact());
    }

    @Override
    public int getItemCount() {
        return suppliers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView contactName, contactPhone;
        ImageView contactIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contactName = itemView.findViewById(R.id.contact_name);
            contactPhone = itemView.findViewById(R.id.contact_phone);
            contactIcon = itemView.findViewById(R.id.contact_icon);
        }
    }
}
