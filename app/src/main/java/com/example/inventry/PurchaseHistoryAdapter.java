package com.example.inventry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.inventry.Classes.PurchaseHistory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PurchaseHistoryAdapter extends RecyclerView.Adapter<PurchaseHistoryAdapter.ViewHolder> {

    private final List<PurchaseHistory> historyList;

    public PurchaseHistoryAdapter(List<PurchaseHistory> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.purchase_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PurchaseHistory history = historyList.get(position);
        holder.dateTextView.setText(history.getDate());
        holder.rateTextView.setText(String.format("₹ %.2f", history.getRate()));
        holder.quantityTextView.setText("Qty: " + history.getQuantity());
        holder.supplierTextView.setText(history.getSupplierName()); // Set supplier name
        holder.serialNumber.setText("#" + history.getSerialNumber());

        // Extract and set the day from the date
        String day = getDayFromDate(history.getDate());
        holder.dayTextView.setText(day);
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView dateTextView, rateTextView, quantityTextView, dayTextView, supplierTextView, serialNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dateTextView = itemView.findViewById(R.id.date);
            rateTextView = itemView.findViewById(R.id.rate);
            quantityTextView = itemView.findViewById(R.id.quantity);
            dayTextView = itemView.findViewById(R.id.day);
            supplierTextView = itemView.findViewById(R.id.Supplier_Name); // Reference to supplier TextView
            serialNumber = itemView.findViewById(R.id.serial_number);
        }
    }
    // Helper method to extract the day from a date string
    private String getDayFromDate(String dateString) {
        try {
            // Assuming the date is in the format "yyyy-MM-dd"
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = inputFormat.parse(dateString);

            // Format to get the day name
            SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.getDefault());
            return dayFormat.format(date); // e.g., "Monday"
        } catch (Exception e) {
            e.printStackTrace();
            return ""; // Return empty string on error
        }
    }
}
