package com.example.inventry.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.inventry.Classes.Products;
import com.example.inventry.Helpers.CategoryDatabaseHelper;
import com.example.inventry.Helpers.TempProductDatabaseHelper;
import com.example.inventry.databinding.FragmentHomeBinding;

import java.util.List;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TempProductDatabaseHelper dbHelper;
    private CategoryDatabaseHelper dbHelper2;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // Initialize the database helper to interact with the database
        dbHelper2 = CategoryDatabaseHelper.getInstance(getContext());

        // Fetch total stock value and top 3 products by stock value
        updateTotalStockValue();
        updateTop3ProductsByValue();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void updateTotalStockValue() {
        // Fetch the total stock value from the database
        double totalStockValue = dbHelper2.getTotalStockValue();

        // Update the Total Stock Value TextView
        binding.StockValue.setText("₹ " + totalStockValue);
    }

    private void updateTop3ProductsByValue() {
        // Fetch the top 3 products by stock value (quantity * purchase_rate) from the database
        List<Products> topProducts = dbHelper2.getTop3ProductsByStockValue();

        // Update the TextViews with product names and their total values
        if (topProducts.size() >= 1) {
            binding.no1Stock.setText("#1. " + topProducts.get(0).getName());
            binding.no1StockValue.setText("₹ " + topProducts.get(0).getAvailableQuantities() * topProducts.get(0).getPurchaseRate());
        }

        if (topProducts.size() >= 2) {
            binding.no2Stock.setText("#2. " + topProducts.get(1).getName());
            binding.no2StockValue.setText("₹ " + topProducts.get(1).getAvailableQuantities() * topProducts.get(1).getPurchaseRate());
        }

        if (topProducts.size() >= 3) {
            binding.no3Stock.setText("#3. " + topProducts.get(2).getName());
            binding.no3StockValue.setText("₹ " + topProducts.get(2).getAvailableQuantities() * topProducts.get(2).getPurchaseRate());
        }
    }
}
