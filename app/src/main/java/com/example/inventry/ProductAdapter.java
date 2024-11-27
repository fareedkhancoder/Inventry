package com.example.inventry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.inventry.Classes.Products;
import com.example.inventry.R;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final List<Products> productList;

    // Constructor
    public ProductAdapter(List<Products> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout for each product
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        // Bind data to each item
        Products product = productList.get(position);
        holder.productName.setText(product.getProductName());
        holder.purchaseRate.setText(String.format("Rate: %.2f", product.getPurchaseRate()));
        holder.availableQuantities.setText(String.format("Available: %d", product.getAvailableQuantities()));
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // ViewHolder class to hold item views
    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productName, purchaseRate, availableQuantities;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.text_product_name);
            purchaseRate = itemView.findViewById(R.id.text_purchase_rate);
            availableQuantities = itemView.findViewById(R.id.text_available_quantities);
        }
    }
}
